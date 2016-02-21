package scavenger.utility;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.*;

import scavenger.TaskData;

/**
 * A TaskData-specific connector for the Datastore.
 * Functionality covers put, get and getRange.
 */
public class TaskdataDatastoreConnector {

  // Singleton
  private static TaskdataDatastoreConnector classInstance;
  private TaskdataDatastoreConnector() {  }
  public static TaskdataDatastoreConnector getInstance() {
    if(classInstance == null) {
      classInstance = new TaskdataDatastoreConnector();

    }
    return classInstance;
  }

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  public void put(TaskData td) {
    Entity e = new Entity("TaskData", td.getKeyname());
    e.setProperty("value", td.getValue());
    e.setProperty("date", td.getDate());
    datastore.put(e);

    // Put into Memcached as well.
    TaskdataMemcachedConnector.getInstance().put(td);
  }

  public TaskData get(String keyname) {
    // Look for entity in Memcached first.
    TaskData tdMem = TaskdataMemcachedConnector.getInstance().get(keyname);
    if(tdMem != null) {
      return tdMem;
    }

    try {
      Key k = KeyFactory.createKey("TaskData", keyname);
      Entity e = datastore.get(k);
      TaskData td = new TaskData(
              e.getKey().getName(),
              (String) e.getProperty("value"),
              (Date) e.getProperty("date"));
      return td;
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  public void delete(String keyname) {
    Key k = KeyFactory.createKey("TaskData", keyname);
    datastore.delete(k);

    TaskdataMemcachedConnector.getInstance().delete(keyname);
  }

  public List<TaskData> getAll() {
    Query queryAll = new Query("TaskData");
    List<Entity> eList = datastore.prepare(queryAll).asList(FetchOptions.Builder.withDefaults());

    List<TaskData> tdList = new LinkedList<>();
    for(Entity e : eList) {
      TaskData td = new TaskData(
              e.getKey().getName(),
              (String) e.getProperty("value"),
              (Date) e.getProperty("date"));
      tdList.add(td);
    }

    return tdList;
  }
}
