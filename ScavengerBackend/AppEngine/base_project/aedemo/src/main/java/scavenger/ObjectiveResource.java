package scavenger;
 
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

import scavenger.utility.ObjectiveDatastoreConnector;

public class ObjectiveResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  String keyname;
 
  public ObjectiveResource(UriInfo uriInfo, Request request, String kname) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.keyname = kname;
  }
  // for the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public Objective getObjectiveHTML() {

    System.out.println("--------- " + this.keyname);
    System.out.println(ObjectiveDatastoreConnector.getInstance().getRange().toString());

    Objective l = ObjectiveDatastoreConnector.getInstance().get(this.keyname);
    if(l == null) {
      throw new RuntimeException("Get: Objective with " + keyname + " not found");
    }

    return l;
  }
  // for the application
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Objective getObjective() {

    return getObjectiveHTML();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public Response putObjective(String title,
                               String info,
                               String latitude,
                               String longitude,
                               String owner,
                               String otherConfirmedUsers,
                               String activity) {
    Response res = null;

    Objective td = ObjectiveDatastoreConnector.getInstance().get(this.keyname);
    if(td == null) {
      Objective tdNew = new Objective(this.keyname,
                                      title,
                                      info,
                                      Double.parseDouble(latitude),
                                      Double.parseDouble(longitude),
                                      owner,
                                      otherConfirmedUsers,
                                      activity,
                                      new Date());
      ObjectiveDatastoreConnector.getInstance().put(tdNew);

      //signal that we created the entity in the datastore
      res = Response.created(uriInfo.getAbsolutePath()).build();
    } else {
      //else signal that we updated the entity
      res = Response.noContent().build();
    }
  
    return res;
  }
 
  @DELETE
  public void deleteIt() {

    ObjectiveDatastoreConnector.getInstance().delete(keyname);
  }
 
} 

