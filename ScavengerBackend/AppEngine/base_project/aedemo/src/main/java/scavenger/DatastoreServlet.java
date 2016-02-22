package scavenger;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      resp.setContentType("text/html");
      resp.getWriter().println("<html><body>");

      // Parse parameters.
      Enumeration<String> params = req.getParameterNames();
      String keyname = null;
      String value = null;
      boolean parameterError = false;

      while(params.hasMoreElements()) {
        String currentElement = params.nextElement();

        if(currentElement.equals("keyname")) {
          keyname = req.getParameterValues("keyname")[0];
        } else if(currentElement.equals("value")) {
          value = req.getParameterValues("value")[0];
        } else {
          parameterError = true;
        }
      }

      if(parameterError) {
        resp.getWriter().println("Parameter error occurred!");

      } else {
        // Get Datastore and Memcache
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

        // Put key and value
        if (keyname != null && value != null) {
          // Datastore
          Entity taskData = new Entity("TaskData", keyname);
          taskData.setProperty("value", value);
          datastore.put(taskData);

          resp.getWriter().println("Stored " + keyname + " and " + value + " in Datastore.<br>");

          // Memcache
          syncCache.put(keyname, value);
          resp.getWriter().println("Stored " + keyname + " and " + value + " in Memcache.<br>");
        }

        // Get value for given key
        else if (keyname != null && value == null) {
          String retrievedValue = "";

          // Memcache
          String valueMemcache = (String) syncCache.get(keyname);
          if (valueMemcache != null) {
            retrievedValue = valueMemcache;
            resp.getWriter().println("Memcache (both)<br>");

          // Datastore
          } else {
            Key keyInstance = KeyFactory.createKey("TaskData", keyname);
            try {
              Entity taskData = datastore.get(keyInstance);
              retrievedValue = taskData.getProperty("value").toString();
              resp.getWriter().println("Datastore<br>");

              // Put into Memcache
              syncCache.put(keyname, retrievedValue);
              resp.getWriter().println("Stored " + keyname + " and " + retrievedValue + " in Memcache.<br>");
            } catch (EntityNotFoundException e) {
              resp.getWriter().println("Neither<br>");
            }
          }

          resp.getWriter().println("key=" + keyname + ", value=" + retrievedValue);
        }

        // Output all TaskData entities
        else if(keyname == null && value == null) {
          // Datastore: Get all
          Query queryAll = new Query("TaskData");
          List<Entity> taskDataAll = datastore.prepare(queryAll).asList(FetchOptions.Builder.withDefaults());

          resp.getWriter().println("<h1>Contents of Datastore</h1>");
          for(Entity taskData : taskDataAll) {
            resp.getWriter().println("key=" + taskData.getKey().getName()
                                 + ", value=" + taskData.getProperty("value")
                                 + "<br>" );
          }

          // Memcache: Get all
          resp.getWriter().println("<h1>Contents of Memcache</h1>");
          for(Entity taskData : taskDataAll) {
            String keyNameInDatastore = taskData.getKey().getName();
            TaskData tdMem = (TaskData) syncCache.get(keyNameInDatastore);
            if (tdMem != null) {
              resp.getWriter().println("key=" + taskData.getKey().getName()
                                   + ", value=" + tdMem.getValue()
                                   + "<br>" );
            }
          }
        }
      }

      resp.getWriter().println("</body></html>");
  }
}
