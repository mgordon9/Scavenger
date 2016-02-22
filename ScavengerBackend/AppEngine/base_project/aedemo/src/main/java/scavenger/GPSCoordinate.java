package scavenger;

/**
 * A class representing a GPS coordinate.
 */
public class GPSCoordinate {
  private double _latitude;
  private double _longitude;

  public double getLatitude() {
    return _latitude;
  }

  public void set_latitude(double latitude) {
    this._latitude = latitude;
  }

  public double getLongitude() {
    return _longitude;
  }

  public void setLongitude(double longitude) {
    this._longitude = longitude;
  }

  public GPSCoordinate() {
    this._latitude = 0;
    this._longitude = 0;
  }

  public GPSCoordinate(double _latitude, double _longitude) {
    this._latitude = _latitude;
    this._longitude = _longitude;
  }
}
