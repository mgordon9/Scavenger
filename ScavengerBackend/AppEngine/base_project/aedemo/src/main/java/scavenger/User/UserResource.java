package scavenger.User;


import scavenger.Objective.Objective;
import scavenger.Objective.util.ObjectiveDatastoreConnector;
import scavenger.User.util.UserDatastoreConnector;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class UserResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  String userid;

  public UserResource(UriInfo uriInfo, Request request, String userid) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.userid = userid;
  }

  // Get user data.
  @GET
  @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public User getUserHTML() {
    User user = UserDatastoreConnector.getInstance().get(userid);

    return user;
  }

  // Update user data.
  // Add a new entity to the datastore. URL Encoding
  // curl -H "Accept: application/json" -X POST --data "keyname=k&longitude=11.1&latitude=22.2" http://127.0.0.1:8080/rest/ds
  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public User addNewObjective(@FormParam("objectiveType") String objectiveType,
                                   @FormParam("objectiveId") String objectiveId,
                                   @Context HttpServletResponse servletResponse) throws IOException {

    System.out.println("objectiveType:" + objectiveType + ", objectiveId:" + objectiveId);

    User user = UserDatastoreConnector.getInstance().get(userid);
    Objective objectiveNew = ObjectiveDatastoreConnector.getInstance().get(objectiveId);
    if(objectiveNew == null) {
      return user;
    }

    if(objectiveType.equals("locationObjective")) {
      Set<Objective> objectives = user.getLocationObjectives();
      objectives.add(objectiveNew);
      user.setLocationObjectives(objectives);
    } else {
      Set<Objective> objectives = user.getVisualObjectives();
      objectives.add(objectiveNew);
      user.setVisualObjectives(objectives);
    }

    UserDatastoreConnector.getInstance().put(user);

    return user;
  }
}
