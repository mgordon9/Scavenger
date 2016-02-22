import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
 
import org.glassfish.jersey.client.ClientConfig;

public class DSTester {
  public static void main(String[] args) {
 
 
    ClientConfig config = new ClientConfig();
    Client client = ClientBuilder.newClient(config);
    WebTarget service = client.target(getBaseURI());
 
    //Delete an entity from  the Datastore
    String keyname = "dstester1";
    service.path("rest").path("ds").path(keyname).request().delete();

    //Create an new entity with this keyname in the Datastore
    String val = "101";
    Response response = service.path("rest").path("ds").path(keyname).request(MediaType.APPLICATION_XML).put(Entity.entity(val,MediaType.APPLICATION_XML),Response.class);

    // Return code should be 201 == created resource
    int status = response.getStatus();
    if (status != 201) {
        System.out.println("Test 1 Failed: Expected status 201, got: "+status);
    } else {
        System.out.println("Test 1 Passed");
    }
/////////////////////////////////////////////////////////

    //Update this same entity with a new value
    val = "102";
    response = service.path("rest").path("ds").path(keyname).request(MediaType.APPLICATION_XML).put(Entity.entity(val,MediaType.APPLICATION_XML),Response.class);

    // Return code should be 204 == no response
    status = response.getStatus();
    if (status != 204) {
        System.out.println("Test 2 Failed: Expected status 204, got: "+status);
    } else {
        System.out.println("Test 2 Passed");
    }

/////////////////////////////////////////////////////////
    // Get the TaskData's from the datastore (browser test)
    boolean passed = true;
    String s;
    try {
        s = service.path("rest").path("ds").request().accept(MediaType.TEXT_XML).get(String.class);
    } catch (Exception e ) {
        passed = false;
        System.out.println("Test 3 Failed with exception: "+ e);
    }
    if (passed) {
        System.out.println("Test 3 Passed");
        //System.out.println(s);
    }

/////////////////////////////////////////////////////////
//    // Get JSON for application
    passed = true;
    try {
        s = service.path("rest").path("ds").request().accept(MediaType.APPLICATION_JSON).get(String.class);

    } catch (Exception e ) {
        passed = false;
        System.out.println("Test 4 Failed with exception: "+ e);
    }
    if (passed) {
        System.out.println("Test 4 Passed");
        //System.out.println(s);
    }

/////////////////////////////////////////////////////////
    // Get XML for application
    passed = true;
    try {
        s = service.path("rest").path("ds").request().accept(MediaType.APPLICATION_XML).get(String.class);
    } catch (Exception e ) {
        passed = false;
        System.out.println("Test 5 Failed with exception: "+ e);
    }
    if (passed) {
        System.out.println("Test 5 Passed");
        //System.out.println(s);
    }

/////////////////////////////////////////////////////////
    //Get TaskData with id dstester1
    passed = true;
    try {
        s = service.path("rest").path("ds").path(keyname).request().accept(MediaType.APPLICATION_XML).get(String.class);
        //s = service.path("rest").path("ds").path(keyname).request(MediaType.APPLICATION_XML).get(String.class);
    } catch (Exception e ) {
        passed = false;
        System.out.println("Test 6 Failed with exception: "+ e);
    }
    if (passed) {
        System.out.println("Test 6 Passed");
        //System.out.println(s);
    }

/////////////////////////////////////////////////////////
    //Delete TaskData with id 1
    service.path("rest").path("ds").path(keyname).request().delete();
 
    //Get TaskData with id dstester1 again
    passed = false;
    try {
        s = service.path("rest").path("ds").path(keyname).request().accept(MediaType.APPLICATION_XML).get(String.class);
    } catch (Exception e ) {
        passed = true;
        System.out.println("Test 7 Passed");
        //System.out.println(s);
    }
    if (!passed) {
        System.out.println("Test 7 Failed: "+ keyname + " found in datastore following delete.  DB dump:");
        //dumping DB
        System.out.println(service.path("rest").path("ds").request().accept(MediaType.APPLICATION_XML).get(String.class));
    }
 
/////////////////////////////////////////////////////////
    //Make sure this keyname is not in datastore
    keyname = "dstester2";
    service.path("rest").path("ds").path(keyname).request().delete();
    //Create a TaskData
    Form form =new Form();
    form.param("keyname", keyname);
    form.param("keyname","100");
    response = service.path("rest").path("ds").request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED),Response.class);

    // Return code should be 204 == no response
    status = response.getStatus();
    if (status != 204) {
        System.out.println("Test 8 Failed: Expected status 204, got: "+status + ", ");
    } else {
        System.out.println("Test 8 Passed");
    }

    //Get TaskData with id dstester3
    passed = true;
    try {
        s = service.path("rest").path("ds").path(keyname).request().accept(MediaType.APPLICATION_XML).get(String.class);
    } catch (Exception e ) {
        passed = false;
        System.out.println("Test 9 Failed with exception: "+ e);
    }
    if (passed) {
        System.out.println("Test 9 Passed");
        //System.out.println(s);
    }
 
  }
 
/////////////////////////////////////////////////////////
  private static URI getBaseURI() {
//    return UriBuilder.fromUri("http://127.0.0.1:8080/").build();
    return UriBuilder.fromUri("http://cs263-ae-demo-119421.appspot.com/").build();
  }
} 
