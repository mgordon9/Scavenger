package com.project.ece150.scavenger;

import android.graphics.Bitmap;

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

    /* Image of Object */
    Bitmap getImage();
    Bitmap getThumbnail();
}