package com.project.ece150.scavenger;
 
import java.io.StringBufferInputStream;
import java.util.List;
 
public class Objective implements IObjective {

    private String _objectiveid;
    private String _title;
    private String _info;
    private Double _latitude;
    private Double _longitude;
    private boolean _isVisitedGPS;
    private boolean _isVisitedVisual;
    private String _owner;
    private List<String> _otherConfirmedUsers;

    @Override
    public String getObjectiveid() { return _objectiveid; }

    public void setObjectiveid(String objectiveid) { _objectiveid = objectiveid; }

    @Override
    public String getTitle() {
        return _title;
    }
 
    public void setTitle(String title) {
        _title = title;
    }
 
    @Override
    public String getInfo() {
        return _info;
    }
 
    public void setInfo(String info) {
        _info = info;
    }
 
    @Override
    public double getLatitude() {
        return _latitude;
    }
 
    public void setLatitude(Double latitude) {
        _latitude = latitude;
    }
 
    @Override
    public double getLongitude() {
        return _longitude;
    }
 
    public void setLongitude(Double longitude) {
        _longitude = longitude;
    }
 
    @Override
    public boolean isVisitedGPS() {
        return _isVisitedGPS;
    }
 
    public void setVisitedGPS(boolean isVisitedGPS) {
        _isVisitedGPS = isVisitedGPS;
    }
 
    @Override
    public boolean isVisitedVisual() {
        return _isVisitedVisual;
    }
 
    public void setVisitedVisual(boolean isVisitedVisual) {
        _isVisitedVisual = isVisitedVisual;
    }
 
    @Override
    public String getOwner() {
        return _owner;
    }
 
    public void setOwner(String owner) {
        _owner = owner;
    }
 
    @Override
    public List<String> otherConfirmedUsers() {
        return _otherConfirmedUsers;
    }
 
    public void setOtherConfirmedUsers(List<String> otherConfirmedUsers) {
        _otherConfirmedUsers = otherConfirmedUsers;
    }
}
