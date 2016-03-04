package com.project.ece150.scavenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;


public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ObjectivesFragment.OnListFragmentInteractionListener,
        IRemoteClientObserver,
        View.OnClickListener{

    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mProvider;
    private UiSettings mUiSettings;
    private SlidingUpPanelLayout mLayout;
    private RecyclerView mRecyclerView;
    private RemoteClient mClient;
    private boolean scavengerHuntActive;
    private ObjectiveListDialogFragment mObjectiveListDialogFragment;
    private IObjective mCurrentObjective;
    private TextView mTextButton;

    public MapFragment()
    {
    }

    public void initialize(RemoteClient remoteClient)
    {
        mClient = remoteClient;
        mClient.registerObserver(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        mCriteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(mCriteria, true);

        View view = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);

        mTextButton = (TextView)view.findViewById(R.id.open_dialog_text);
        mTextButton.setClickable(true);
        mTextButton.setOnClickListener(this);
        mTextButton.setText("No Objective Selected");

        return view;
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
        mMap.clear();
        LatLng objectivePos = new LatLng(item.getLatitude(), item.getLongitude());
        mMap.addMarker(new MarkerOptions().position(objectivePos).title(item.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(objectivePos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
        mObjectiveListDialogFragment.dismiss();
        scavengerHuntActive = true;
        mCurrentObjective = item;
        mTextButton.setText("Objective Active");
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
        mObjectiveListDialogFragment.onObjectiveGetReceived(objective);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getChildFragmentManager();
        mObjectiveListDialogFragment = new ObjectiveListDialogFragment();
        mObjectiveListDialogFragment.setListener(this);
        if(!scavengerHuntActive) {
            mObjectiveListDialogFragment.setTitle("Available Objectives");
            mClient.initObjectivesGetRequest();
            mObjectiveListDialogFragment.show(fm, "objectives_list");
        }
        else {
            mObjectiveListDialogFragment.setTitle("Current Objective");
            mClient.initObjectiveGetRequest(mCurrentObjective.getObjectiveid());
            mObjectiveListDialogFragment.show(fm, "active_objective");
        }

    }
}