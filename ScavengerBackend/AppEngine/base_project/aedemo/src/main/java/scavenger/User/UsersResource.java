package scavenger.User;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import scavenger.GPSCoordinate;
import scavenger.Objective.Objective;
import scavenger.User.util.UserDatastoreConnector;

@Path("/users")
public class UsersResource {
  // Allows to insert contextual objects into the class,
  // e.g. ServletContext, Request, Response, UriInfo
  @Context
  UriInfo uriInfo;
  @Context
  Request request;


  // Add a new entity to the datastore. JSON Encoding of request body.
  // curl -H "Accept: application/json" -X POST --data "keyname=k&longitude=11.1&latitude=22.2" http://127.0.0.1:8080/rest/ds
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public User createUser(final User user) throws IOException {

    User u = UserDatastoreConnector.getInstance().get(user.getUserid());
    if(u != null) {
      return u;
    }

    if(user.getScore() == null) {
      user.setScore(0.0);
    }
    UserDatastoreConnector.getInstance().put(user);

    return user;
  }

  @Path("{userid}")
  public scavenger.User.UserResource getEntity(@PathParam("userid") String userid) {

    System.out.println("GETting Objective for " + userid);
    return new scavenger.User.UserResource(uriInfo, request, userid);
  }
}
