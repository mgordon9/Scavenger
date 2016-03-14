package scavenger.User.util;

import com.google.appengine.api.datastore.*;

import com.google.appengine.repackaged.com.google.common.base.Flag;
import scavenger.Objective.Objective;
import scavenger.Objective.util.ObjectiveDatastoreConnector;
import scavenger.User.User;

import java.util.*;

/**
 * A User-specific connector for the Datastore.
 * Functionality covers put, get and getRange.
 */
public class UserDatastoreConnector {

  // Singleton
  private static UserDatastoreConnector classInstance;
  private UserDatastoreConnector() {  }
  public static UserDatastoreConnector getInstance() {
    if(classInstance == null) {
      classInstance = new UserDatastoreConnector();

    }
    return classInstance;
  }

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


  public void put(User td) {
    Entity e = new Entity("User", td.getUserid());
    e.setProperty("score", td.getScore());

    e.setProperty("locationObjectives", td.getLocationObjectivesAsStringSet());
    e.setProperty("visualObjectives", td.getVisualObjectivesAsStringSet());

    datastore.put(e);
  }

  // Retrieves user object and all associated objective objects from the Datastore.
  public User get(String userid) {

    try {
      Key k = KeyFactory.createKey("User", userid);
      Entity e = datastore.get(k);

      // Build base object
      User user = new User(
              userid,
              (Double) e.getProperty("score"),
              new HashSet<Objective>(),
              new HashSet<Objective>());

      // Retrieve associated objectives by key name
      // (1) locationObjectives
      List<String> locationObjectivesStrList = (List<String>) e.getProperty("locationObjectives");
      if(locationObjectivesStrList != null) {
        Set<String> locationObjectivesStrSet = new HashSet<String>(locationObjectivesStrList);
        Set<Objective> locationObjectives = getObjectiveSetFromStrSet(locationObjectivesStrSet, false);
        user.setLocationObjectives(locationObjectives);
      }

      // (2) visualObjectives
      List<String> visualObjectivesStrList = (List<String>) e.getProperty("visualObjectives");
      if(visualObjectivesStrList != null) {
        Set<String> visualObjectivesStrSet = new HashSet<String>(visualObjectivesStrList);
        Set<Objective> visualObjectives = getObjectiveSetFromStrSet(visualObjectivesStrSet, false);
        user.setVisualObjectives(visualObjectives);
      }

      return user;
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  public void delete(String userid) {
    Key k = KeyFactory.createKey("User", userid);
    datastore.delete(k);
  }

  private Set<Objective> getObjectiveSetFromStrSet(Set<String> objectivesStrSet, boolean withImages) {
    Set<Objective> objectives = new HashSet<Objective>();

    for (String objectiveStr : objectivesStrSet) {
      Objective o = ObjectiveDatastoreConnector.getInstance().get(objectiveStr);
      if(!withImages) {
        o.setImage("<removed for size>");
      }
      objectives.add(o);
    }

    return objectives;
  }
}
