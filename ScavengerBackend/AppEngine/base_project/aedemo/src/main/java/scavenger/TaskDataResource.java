package scavenger;
 
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

import scavenger.utility.TaskdataDatastoreConnector;

public class TaskDataResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  String keyname;
 
  public TaskDataResource(UriInfo uriInfo, Request request, String kname) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.keyname = kname;
  }
  // for the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public TaskData getTaskDataHTML() {

    System.out.println("--------- " + this.keyname);
    System.out.println(TaskdataDatastoreConnector.getInstance().getAll().toString());

    TaskData td = TaskdataDatastoreConnector.getInstance().get(this.keyname);
    if(td == null) {
      throw new RuntimeException("Get: TaskData with " + keyname + " not found");
    }

    return td;
  }
  // for the application
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public TaskData getTaskData() {

    return getTaskDataHTML();
  }
 
  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public Response putTaskData(String val) {
    Response res = null;

    TaskData td = TaskdataDatastoreConnector.getInstance().get(this.keyname);
    if(td == null) {
      TaskData tdNew = new TaskData(this.keyname, val, new Date());
      TaskdataDatastoreConnector.getInstance().put(tdNew);

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

    TaskdataDatastoreConnector.getInstance().delete(keyname);
  }
 
} 
