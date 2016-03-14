package scavenger.Objective;
import com.google.appengine.api.datastore.Blob;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;


/**
 * This class represents a Objective entity that is stored
 * in the Google Datastore.
 */
@XmlRootElement
public class Objective implements Serializable {
  @XmlElement public String keyname;

  @XmlElement public String title;
  @XmlElement public String info;

  @XmlElement public Double latitude;
  @XmlElement public Double longitude;

  @XmlElement public String owner;
  @XmlElement public String otherConfirmedUsers;

  @XmlElement public Boolean isVisitedGPS;
  @XmlElement public Boolean isVisitedVisual;

  @XmlElement public String activity;

  @XmlElement public String thumbnail;
  @XmlElement public String image;

  public Date date;

  public Objective() {

  }

  public Objective(String keyname,
                   String title,
                   String info,
                   Double latitude,
                   Double longitude,
                   String owner,
                   String otherConfirmedUsers,
                   String activity,
                   String thumbnail,
                   String image,
                   Date date) {

    this.keyname = keyname;
    this.title = title;
    this.info = info;
    this.latitude = latitude;
    this.longitude = longitude;
    this.owner = owner;
    this.otherConfirmedUsers = otherConfirmedUsers;
    this.activity = activity;
    this.thumbnail = thumbnail;
    this.image = image;
    this.date = date;
  }

  public Objective(String keyname,
                   String title,
                   String info,
                   String latitude,
                   String longitude,
                   String owner,
                   String otherConfirmedUsers,
                   String activity,
                   String thumbnail,
                   String image,
                   Date date) {

    this( keyname,
          title,
          info,
          Double.parseDouble(latitude),
          Double.parseDouble(longitude),
          owner,
          otherConfirmedUsers,
          activity,
          thumbnail,
          image,
          date);
  }

  public String getKeyname() {
    return keyname;
  }
  public void setKeyname(String keyname) {
    this.keyname = keyname;
  }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getInfo() { return info; }
  public void setInfo(String info) { this.info = info; }

  public Double getLatitude() { return latitude; }
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public String getOwner() { return owner; }
  public void setOwner(String owner) { this.owner = owner; }

  public String getOtherConfirmedUsers() { return otherConfirmedUsers; }
  public void setOtherConfirmedUsers(String otherConfirmedUsers) { this.otherConfirmedUsers = otherConfirmedUsers; }

  public Boolean isVisitedGPS() { return isVisitedGPS; }
  public Boolean isVisitedVisual() { return isVisitedVisual; }

  public String getActivity() { return activity; }
  public void setActivity(String activity) { this.activity = activity; }

  public String getThumbnail() { return thumbnail; }
  public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

  public String getImage() { return image; }
  public void setImage(String image) { this.image = image; }

  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }

  public String toString() {
    return this.keyname + ", "
            + this.title + ", "
            + this.info + ", "
            + this.latitude + ", "
            + this.longitude + ", "
            + this.owner + ", "
            + this.otherConfirmedUsers + ", "
            + this.activity + ", "
            + (this.thumbnail != null) + ","
            + (this.image != null) + ","
            + this.date;
  }

  public boolean isCompletelyPopulated() {
    return this.keyname != null
            && this.title != null
            && this.info != null
            && this.latitude != null
            && this.longitude != null
            && this.owner != null
            && this.otherConfirmedUsers != null
            && this.activity != null
//            && this.thumbnail != null
//            && this.image != null
            && this.date != null;
  }
}