package scavenger.utility;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import scavenger.TaskData;

/**
 * A TaskData-specific connector for Memcached.
 */
public class TaskdataMemcachedConnector {

  // Singleton
  private static TaskdataMemcachedConnector classInstance;
  private TaskdataMemcachedConnector() {  }
  public static TaskdataMemcachedConnector getInstance() {
    if(classInstance == null) {
      classInstance = new TaskdataMemcachedConnector();

    }
    return classInstance;
  }

  private MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

  public void put(TaskData td) {
    syncCache.put(td.getKeyname(), td);
  }

  public TaskData get(String keyname) {
    return (TaskData) syncCache.get(keyname);
  }

  public void delete(String keyname) {
    syncCache.delete(keyname);
  }
}
