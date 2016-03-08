package com.project.ece150.scavenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.project.ece150.scavenger.remote.EObjectiveConfirmedType;
import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;

import java.util.List;


public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ObjectivesFragment.OnListFragmentInteractionListener,
        IRemoteClientObserver,
        View.OnClickListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final long UPDATE_INTERVAL = 3000;
    public static final int RADIUS = 100;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mProvider;
    private UiSettings mUiSettings;
    private RemoteClient mClient;
    private boolean scavengerHuntActive;
    private ObjectiveListDialogFragment mObjectiveListDialogFragment;
    private ActiveObjectiveDialogFragment mActiveObjectiveDialogFragment;
    private IObjective mCurrentObjective;
    private TextView mTextButton;
    private LocationRequest mLocationRequest;
    private Location mBestReading;
    public static GoogleApiClient mGoogleApiClient;
    private String mAccountName;
    private boolean locationConfirmed;

    public MapFragment()
    {
    }

    public void initialize(RemoteClient remoteClient, String accountName)
    {
        mAccountName = accountName;
        mClient = remoteClient;
        mClient.registerObserver(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationConfirmed = false;
        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mProvider = mLocationManager.getBestProvider(mCriteria, true);

        View view = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);

        mTextButton = (TextView)view.findViewById(R.id.open_dialog_text);
        mTextButton.setClickable(true);
        mTextButton.setOnClickListener(this);
        mTextButton.setText("No Objective Selected");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
//        checkPlayServices();

        return view;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        1000).show();
            } else {
                Toast.makeText(getContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
//                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        startMap();
        scavengerHuntActive = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        Location location = mLocationManager.getLastKnownLocation(mProvider);

        LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

        mMap.setOnMyLocationButtonClickListener(this);
        mUiSettings = mMap.getUiSettings();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    private void startMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onListFragmentInteraction(IObjective item) {
        mObjectiveListDialogFragment.dismiss();
        scavengerHuntActive = true;
        mCurrentObjective = item;

        //randomizing the circles center
        double randLat = ((Math.random() * 2.0 - 1.0) * (double)RADIUS / 111320.0) + mCurrentObjective.getLatitude();
        double randLong = ((Math.random() * 2.0 - 1.0) * (double)RADIUS / 111320.0) + mCurrentObjective.getLongitude();
        LatLng objectiveArea = new LatLng(randLat, randLong);

        mMap.clear();
        mMap.addCircle(new CircleOptions()
                .center(objectiveArea)
                .radius(RADIUS)
                .strokeColor(Color.BLUE)
                .strokeWidth((float) 0)
                .fillColor(0x8087CEFA));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentObjective.getLatitude(), mCurrentObjective.getLongitude())).title(item.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(objectiveArea));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        double lat = Math.abs(mCurrentObjective.getLatitude() - location.getLatitude());
        double longitude = Math.abs(mCurrentObjective.getLongitude() - location.getLongitude());
        double distance = Math.sqrt(lat * lat + longitude * longitude);
        int distanceMeters = (int)(distance * 111320.0);

        mTextButton.setText("Objective Active     Distance: " + ((Integer) distanceMeters).toString() + " meters");
    }

    @Override
    public void onUserGetReceived(IUser user) {

    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {
        mObjectiveListDialogFragment.onObjectivesGetReceived(objectives);
    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {
        mCurrentObjective = objective;
        mActiveObjectiveDialogFragment.onObjectiveGetReceived(objective);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getChildFragmentManager();
        if(!scavengerHuntActive) {
            mObjectiveListDialogFragment = new ObjectiveListDialogFragment();
            mObjectiveListDialogFragment.setListener(this);
            mObjectiveListDialogFragment.setTitle("Available Objectives");
            mObjectiveListDialogFragment.show(fm, "objectives_list");
            mClient.initObjectivesGetRequest();
        }
        else {

            mActiveObjectiveDialogFragment = new ActiveObjectiveDialogFragment();
            mActiveObjectiveDialogFragment.setListener(this);
            mActiveObjectiveDialogFragment.setTitle("Current Objective");
            mActiveObjectiveDialogFragment.show(fm, "active_objective");
            mClient.initObjectiveGetRequest(mCurrentObjective.getObjectiveid());
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Toast.makeText(getActivity(), "OnConnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(scavengerHuntActive) {
            double lat = Math.abs(mCurrentObjective.getLatitude() - location.getLatitude());
            double longitude = Math.abs(mCurrentObjective.getLongitude() - location.getLongitude());

            double distance = Math.sqrt(lat * lat + longitude * longitude);
            int distanceMeters = (int)(distance * 111320.0);
            if(distanceMeters <= 10 && !locationConfirmed)
            {
                locationConfirmed = true;
                mClient.initUserAddObjectiveRequest(mAccountName, mCurrentObjective.getObjectiveid(), EObjectiveConfirmedType.LOCATIONCONFIRMED);
                Toast.makeText(getActivity(), "Location Confirmed, Congratulations!!", Toast.LENGTH_SHORT).show();
            }
            mTextButton.setText("Objective Active\nDistance: " + ((Integer) distanceMeters).toString() + " meters");
        }
//        Toast.makeText(getActivity(), location.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    public IObjective getCurrentObjective() {
        return mCurrentObjective;
    }
}