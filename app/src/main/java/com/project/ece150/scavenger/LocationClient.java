package com.project.ece150.scavenger;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class LocationClient {

    private Context mContext;

    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mProvider;

    public LocationClient(Context context) {
        mContext = context;

        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        mCriteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(mCriteria, true);
    }

    public LatLng getCurrentPosition() {
        Location location = mLocationManager.getLastKnownLocation(mProvider);
        LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());

        return currentPos;
    }
}
