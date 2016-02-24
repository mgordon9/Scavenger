package com.project.ece150.scavenger;

import java.util.List;

public interface IObjective {
    /* ID */
    String getObjectiveid();

    /* General Information */
    String getTitle();
    String getInfo();

    /* GPS Coordinates */
    double getLatitude();
    double getLongitude();

    /* Current Visited State */
    boolean isVisitedGPS();
    boolean isVisitedVisual();

    /* User Information */
    String getOwner();
    List<String> otherConfirmedUsers();

    /* TODO: Image of Object */
    //Image getImage();
}