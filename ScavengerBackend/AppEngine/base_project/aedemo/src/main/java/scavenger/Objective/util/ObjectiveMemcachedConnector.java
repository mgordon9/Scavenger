package scavenger.Objective.util;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import scavenger.Objective.Objective;

/**
 * A Objective-specific connector for Memcached.
 */
public class ObjectiveMemcachedConnector {

  // Singleton
  private static ObjectiveMemcachedConnector classInstance;
  private ObjectiveMemcachedConnector() {  }
  public static ObjectiveMemcachedConnector getInstance() {
    if(classInstance == null) {
      classInstance = new ObjectiveMemcachedConnector();

    }
    return classInstance;
  }

  private MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

  public void put(Objective l) {
    syncCache.put(l.getKeyname(), l);
  }

  public Objective get(String keyname) {
    return (Objective) syncCache.get(keyname);
  }

  public void delete(String keyname) {
    syncCache.delete(keyname);
  }
}

