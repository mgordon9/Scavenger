package scavenger.Objective;
 
import java.io.*;
import java.util.*;
 
import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import scavenger.GPSCoordinate;
import scavenger.Objective.util.ObjectiveDatastoreConnector;

@Path("/objectives")
public class ObjectivesResource {
  // Allows to insert contextual objects into the class,
  // e.g. ServletContext, Request, Response, UriInfo
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  // Return the list of filtered entities to applications
  // curl -H "Accept: application/json" -X GET http://127.0.0.1:8080/rest/ds
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Objective> getFilteredEntities(@QueryParam("latitude1") String latitude1,
                                             @QueryParam("longitude1") String longitude1,
                                             @QueryParam("latitude2") String latitude2,
                                             @QueryParam("longitude2") String longitude2,
                                             @QueryParam("maxLimit") String maxLimit){

    if(latitude1 == null || longitude1 == null || latitude2 == null || longitude2 == null) {
      return ObjectiveDatastoreConnector.getInstance().getRange();
    } else {
      return ObjectiveDatastoreConnector.getInstance().getRange(
              new GPSCoordinate(Double.parseDouble(latitude1), Double.parseDouble(longitude1)),
              new GPSCoordinate(Double.parseDouble(latitude2), Double.parseDouble(longitude2)),
              maxLimit != null ? Integer.parseInt(maxLimit) : 100
      );
    }
  }


  // Add a new entity to the datastore. URL Encoding
  // curl -H "Accept: application/json" -X POST --data "keyname=k&longitude=11.1&latitude=22.2" http://127.0.0.1:8080/rest/ds
  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Objective newObjective(@FormParam("keyname") String keyname,
                                @FormParam("title") String title,
                                @FormParam("info") String info,
                                @FormParam("latitude") String latitude,
                                @FormParam("longitude") String longitude,
                                @FormParam("owner") String owner,
                                @FormParam("otherConfirmedUsers") String otherConfirmedUsers,
                                @FormParam("activity") String activity,
                                @Context HttpServletResponse servletResponse) throws IOException {

    Date date = new Date();

    // Store objective with latitude and longitude in database.
    Objective loc = new Objective(keyname + date.getTime(),
                                  title,
                                  info,
                                  latitude,
                                  longitude,
                                  owner,
                                  otherConfirmedUsers,
                                  activity,
                                  date);
    System.out.println("Posting new Objective: " + loc.toString());

    if (loc.isCompletelyPopulated()) {
      ObjectiveDatastoreConnector.getInstance().put(loc);
      return loc;
    } else {
      System.out.println("Objective not stored -  at least one Parameter is missing.");
    }

    return null;
  }

  // Add a new entity to the datastore. JSON Encoding
  // curl -H "Accept: application/json" -X POST --data "keyname=k&longitude=11.1&latitude=22.2" http://127.0.0.1:8080/rest/ds
  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes("application/json")
  public Objective newObjective(final Objective loc) throws IOException {

    loc.setDate(new Date());
    loc.setKeyname(loc.getKeyname() + loc.getDate().getTime());
    System.out.println("Posting new Objective: " + loc.toString());

    if (loc.isCompletelyPopulated()) {
      ObjectiveDatastoreConnector.getInstance().put(loc);
      return loc;
    } else {
      System.out.println("Objective not stored -  at least one Parameter is missing.");
    }

    return null;
  }
 
  //The @PathParam annotation says that keyname can be inserted as parameter after this class's route /ds
  @Path("{keyname}")
  public scavenger.Objective.ObjectiveResource getEntity(@PathParam("keyname") String keyname) {
    System.out.println("GETting Objective for " + keyname);
    return new scavenger.Objective.ObjectiveResource(uriInfo, request, keyname);
  }
}
