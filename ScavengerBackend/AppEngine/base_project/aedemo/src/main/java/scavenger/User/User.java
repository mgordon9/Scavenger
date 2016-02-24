package scavenger.User;

import scavenger.Objective.Objective;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;


/**
 * This class represents a User entity that is stored
 * in the Google Datastore.
 */
@XmlRootElement
public class User implements Serializable {
  @XmlElement public String userid;
  @XmlElement public Double score;

  @XmlElement public Set<Objective> locationObjectives;
  @XmlElement public Set<Objective> visualObjectives;

  public User(String userid,
              Double score,
              Set<Objective> locationObjectives,
              Set<Objective> visualObjectives) {

    this.userid = userid;
    this.score = score;
    this.locationObjectives = locationObjectives;
    this.visualObjectives = visualObjectives;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Set<Objective> getLocationObjectives() {
    return locationObjectives;
  }

  public Set<String> getLocationObjectivesAsStringSet() {
    return getObjectivesAsStringSet(this.locationObjectives);
  }

  public void setLocationObjectives(Set<Objective> locationObjectives) {
    this.locationObjectives = locationObjectives;
  }

  public Set<Objective> getVisualObjectives() {
    return visualObjectives;
  }

  public void setVisualObjectives(Set<Objective> visualObjectives) {
    this.visualObjectives = visualObjectives;
  }

  public Set<String> getVisualObjectivesAsStringSet() {
    return getObjectivesAsStringSet(this.visualObjectives);
  }

  private Set<String> getObjectivesAsStringSet(Set<Objective> objectives) {
    Set<String> set = new HashSet<String>();

    if(objectives == null) {
      return set;
    }

    for(Objective objective : objectives) {
      set.add(objective.getKeyname());
    }
    return set;
  }

  public String toString() {
    if(this.locationObjectives == null || this.visualObjectives == null) {
      return this.userid + ", "
              + this.score + ", ";
    }

    return this.userid + ", "
            + this.score + ", "
            + "(" + this.locationObjectives.toString() + "), "
            + "(" + this.visualObjectives.toString() + "), ";
  }
}