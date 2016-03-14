package scavenger.Objective;
 
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

import com.google.appengine.api.datastore.Blob;
import scavenger.Objective.util.ObjectiveDatastoreConnector;

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
  @Produces(MediaType.APPLICATION_JSON)
  public Objective getObjectiveHTML() {

    System.out.println("--------- " + this.keyname);
    System.out.println(ObjectiveDatastoreConnector.getInstance().getRange().toString());

    Objective l = ObjectiveDatastoreConnector.getInstance().get(this.keyname);
    if(l == null) {
      throw new RuntimeException("Get: Objective with " + keyname + " not found");
    }

    return l;
  }
 
  @DELETE
  public void deleteIt() {

    ObjectiveDatastoreConnector.getInstance().delete(keyname);
  }
 
} 

