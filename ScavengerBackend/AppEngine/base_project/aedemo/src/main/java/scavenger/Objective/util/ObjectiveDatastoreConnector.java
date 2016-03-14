package scavenger.Objective.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.*;

import scavenger.GPSCoordinate;
import scavenger.Objective.Objective;

/**
 * A Objective-specific connector for the Datastore.
 * Functionality covers put, get and getRange.
 */
public class ObjectiveDatastoreConnector {

  // Singleton
  private static ObjectiveDatastoreConnector classInstance;
  private ObjectiveDatastoreConnector() {  }
  public static ObjectiveDatastoreConnector getInstance() {
    if(classInstance == null) {
      classInstance = new ObjectiveDatastoreConnector();

    }
    return classInstance;
  }

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  public void put(Objective td) {
    Entity e = new Entity("Objective", td.getKeyname());
    e.setProperty("title", td.getTitle());
    e.setProperty("info", td.getInfo());
    e.setProperty("latitude", td.getLatitude());
    e.setProperty("longitude", td.getLongitude());
    e.setProperty("owner", td.getOwner());
    e.setProperty("otherConfirmedUsers", td.getOtherConfirmedUsers());
    e.setProperty("activity", td.getActivity());
    e.setProperty("thumbnail", new Blob(td.getThumbnail().getBytes()));
    e.setProperty("image", new Blob(td.getImage().getBytes()));
    e.setProperty("date", td.getDate());
    datastore.put(e);

    // Put into Memcached as well.
    ObjectiveMemcachedConnector.getInstance().put(td);
  }

  public Objective get(String keyname) {
    // Look for entity in Memcached first.
    Objective tdMem = ObjectiveMemcachedConnector.getInstance().get(keyname);
    if(tdMem != null) {
      return tdMem;
    }

    try {
      Key k = KeyFactory.createKey("Objective", keyname);
      Entity e = datastore.get(k);
      Objective td = parseEntityToObjective(e);
      return td;
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  public void delete(String keyname) {
    Key k = KeyFactory.createKey("Objective", keyname);
    datastore.delete(k);

    ObjectiveMemcachedConnector.getInstance().delete(keyname);
  }

  public List<Objective> getRange() {
    Query query = new Query("Objective");
    List<Entity> eList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

    List<Objective> tdList = new LinkedList<>();
    for(Entity e : eList) {
      Objective td = parseEntityToObjective(e);
      tdList.add(td);
    }

    return tdList;
  }

  public List<Objective> getRange(GPSCoordinate p1, GPSCoordinate p2, int maxLimit) {

    // Latitude Filter.
    double latMin = Math.min(p1.getLatitude(), p2.getLatitude());
    double latMax = Math.max(p1.getLatitude(), p2.getLatitude());

    Query.Filter latitudeMinFilter =
            new Query.FilterPredicate("latitude",
                    Query.FilterOperator.GREATER_THAN_OR_EQUAL,
                    latMin);
    Query.Filter latitudeMaxFilter =
            new Query.FilterPredicate("latitude",
                    Query.FilterOperator.LESS_THAN_OR_EQUAL,
                    latMax);
    Query.Filter latitudeRangeFilter =
            Query.CompositeFilterOperator.and(latitudeMinFilter, latitudeMaxFilter);

    // Longitude Filter.
    double longMin = Math.min(p1.getLongitude(), p2.getLongitude());
    double longMax = Math.max(p1.getLongitude(), p2.getLongitude());

    Query.Filter longitudeMinFilter =
            new Query.FilterPredicate("longitude",
                    Query.FilterOperator.GREATER_THAN_OR_EQUAL,
                    longMin);
    Query.Filter longitudeMaxFilter =
            new Query.FilterPredicate("longitude",
                    Query.FilterOperator.LESS_THAN_OR_EQUAL,
                    longMax);
    Query.Filter longitudeRangeFilter =
            Query.CompositeFilterOperator.and(longitudeMinFilter, longitudeMaxFilter);

    // Build Query.
    Query query = new Query("Objective");
    query.setFilter(latitudeRangeFilter);
    query.setFilter(longitudeRangeFilter);

    List<Entity> eList = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(maxLimit));

    List<Objective> tdList = new LinkedList<>();
    for(Entity e : eList) {
      Objective td = parseEntityToObjective(e);
    }

    return tdList;

  }

  private Objective parseEntityToObjective(Entity e) {

    String image = new String();
    if(e.getProperty("image") != null) {
      Blob b = (Blob) e.getProperty("image");
      image = new String(b.getBytes());
    }

    String thumbnail = new String();
    if(e.getProperty("thumbnail") != null) {
      Blob b = (Blob) e.getProperty("thumbnail");
      thumbnail = new String(b.getBytes());
    }

    Objective obj = new Objective(
            e.getKey().getName(),
            (String) e.getProperty("title"),
            (String) e.getProperty("info"),
            (Double) e.getProperty("latitude"),
            (Double) e.getProperty("longitude"),
            (String) e.getProperty("owner"),
            (String) e.getProperty("otherConfirmedUsers"),
            (String) e.getProperty("activity"),
            thumbnail,
            image,
            (Date) e.getProperty("date"));

    return obj;
  }
}

