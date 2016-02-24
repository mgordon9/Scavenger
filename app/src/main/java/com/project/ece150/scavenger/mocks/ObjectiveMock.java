package com.project.ece150.scavenger.mocks;

import com.project.ece150.scavenger.IObjective;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ObjectiveMock implements IObjective {
    private LinkedList<String> _users;
    private double _longitude;
    private double _latitude;

    public ObjectiveMock() {
        // Init _users
        _users = new LinkedList<String>();
        for(int i=0; i<10; i++) {
            _users.push("User" + Integer.toString(i));
        }

        // Init coordinates
        Random r = new Random();
        _latitude = 34.425061 + (34.449005 - 34.425061) * r.nextDouble();
        _longitude = -119.845214 + (-119.771957 - -119.845214) * r.nextDouble();
    }

    @Override
    public String getObjectiveid() { return "<myId>"; }

    @Override
    public String getTitle() {
        return "MyTitle";
    }

    @Override
    public String getInfo() {
        return "MyInfo";
    }

    @Override
    public double getLatitude() {
        return _latitude;
    }

    @Override
    public double getLongitude() {
        return _longitude;
    }

    @Override
    public boolean isVisitedGPS() {
        return false;
    }

    @Override
    public boolean isVisitedVisual() {
        return false;
    }

    @Override
    public String getOwner() {
        return "MyOwner";
    }

    @Override
    public List<String> otherConfirmedUsers() {

        return _users;
    }

    public String toString() {
        String ret = "";

        boolean first = true;
        for(String username : _users) {
            if(first) {
                ret += this.getTitle() + ": ";

                first = false;
            } else {
                ret += ", ";
            }

            ret += username;
        }

        return ret;
    }
}