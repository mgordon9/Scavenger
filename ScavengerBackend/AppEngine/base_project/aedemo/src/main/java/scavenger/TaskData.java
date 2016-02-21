package scavenger;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;
 
@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
public class TaskData implements Serializable {
  private String keyname;
  private String value;
  private Date date;

  public TaskData() {

  }

  public TaskData(String keyname, String value, Date date) {
    this.keyname = keyname;
    this.value = value;
    this.date = date;
  }

  public String getKeyname() {
    return keyname;
  }

  public void setKeyname(String keyname) {
    this.keyname = keyname;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String toString() {
    return this.keyname + ", "
            + this.value + ", "
            + this.date;
  }
} 
