package scavenger;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

// The Worker servlet should be mapped to the "/worker" URL.
public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get Datastore and Memcache
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

        // Parse keyname and value
        String keyname = request.getParameter("keyname");
        String value = request.getParameter("value");

        // Datastore
        Entity taskDataNew = new Entity("TaskData", keyname);
        taskDataNew.setProperty("value", value);
        datastore.put(taskDataNew);

        // Memcache
        syncCache.put(keyname, value);
    }
}
