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

  // Return the list of filtered entities to applications in JSON.
  // curl -H "Accept: application/json" -X GET http://127.0.0.1:8080/rest/ds
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Objective> getFilteredEntities(@QueryParam("latitude1") String latitude1,
                                             @QueryParam("longitude1") String longitude1,
                                             @QueryParam("latitude2") String latitude2,
                                             @QueryParam("longitude2") String longitude2,
                                             @QueryParam("maxLimit") String maxLimit){

    List<Objective> objectivesList;

    if(latitude1 == null || longitude1 == null || latitude2 == null || longitude2 == null) {
      objectivesList = ObjectiveDatastoreConnector.getInstance().getRange();
    } else {
      objectivesList = ObjectiveDatastoreConnector.getInstance().getRange(
              new GPSCoordinate(Double.parseDouble(latitude1), Double.parseDouble(longitude1)),
              new GPSCoordinate(Double.parseDouble(latitude2), Double.parseDouble(longitude2)),
              maxLimit != null ? Integer.parseInt(maxLimit) : 100
      );
    }

    for(Objective o : objectivesList) {
      o.setImage("<removed for size>");
    }
    return objectivesList;
  }

  // Add a new entity to the datastore. Request has JSON body.
  // curl -H "Accept: application/json" -X POST --data "keyname=k&longitude=11.1&latitude=22.2" http://127.0.0.1:8080/rest/ds
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Objective newObjective(final Objective objective) throws IOException {

    objective.setDate(new Date());
    objective.setKeyname(objective.getKeyname() + objective.getDate().getTime());
    System.out.println("Posting new Objective: " + objective.toString());

    if (objective.isCompletelyPopulated()) {
      ObjectiveDatastoreConnector.getInstance().put(objective);

      // Reduce Size!!
      objective.setImage("<remove for size>");

      return objective;
    } else {
      System.out.println("Objective not stored -  at least one Parameter is missing.");
    }

    return null;
  }
 
  @Path("{keyname}")
  public scavenger.Objective.ObjectiveResource getEntity(@PathParam("keyname") String keyname) {
    System.out.println("GETting Objective for " + keyname);
    return new scavenger.Objective.ObjectiveResource(uriInfo, request, keyname);
  }
}
