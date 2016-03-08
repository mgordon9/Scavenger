package com.project.ece150.scavenger;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class LocationClient implements LocationListener {

    private Context mContext;

    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mProvider;

    Location mCurrentLocation;

    public LocationClient(Context context) {
        mContext = context;

        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        mCriteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(mCriteria, true);

        long refreshTime = 1000;
        float minDistance = 0;
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, refreshTime, minDistance, this);

        mCurrentLocation = mLocationManager.getLastKnownLocation(mProvider);
    }

    public LatLng getCurrentPosition() {
        LatLng currentPos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        return currentPos;
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
