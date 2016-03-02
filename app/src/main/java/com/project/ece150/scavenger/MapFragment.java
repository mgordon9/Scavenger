package com.project.ece150.scavenger;

import android.Manifest;
import android.content.Context;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;


public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        SlidingUpPanelLayout.PanelSlideListener,
        ObjectivesFragment.OnListFragmentInteractionListener {

    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Context mContext;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mProvider;
    private UiSettings mUiSettings;
    SlidingUpPanelLayout mLayout;
    RecyclerView mRecyclerView;

    public MapFragment()
    {
    }

//    public MapFragment(Context context) {
//        mContext = context;
//    }

    @Override
    public void onCreate(Bundle savedIntanceState)
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        mCriteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(mCriteria, true);


        View view = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);

        startMap();

//        ArrayList<IObjective> data = new ArrayList<IObjective>();
//        Objective d0 = new Objective();
//        d0.setInfo("info 0");
//        d0.setOwner("Matthew");
//        Objective d1 = new Objective();
//        d1.setInfo("info 1");
//        d1.setOwner("Matthew");
//        data.add(d0);
//        data.add(d1);
//
//        RecyclerView mRecyclerView = (RecyclerView) getView().findViewById(R.id.list);
//        ObjectiveRecyclerViewAdapter adapter = new ObjectiveRecyclerViewAdapter(data, this);
//
//        mRecyclerView.setAdapter(adapter);
//
//        mLayout = (SlidingUpPanelLayout) getView().findViewById(R.id.sliding_layout);
//        mLayout.setPanelSlideListener(this);



        return view;

    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState)
//    {
//
//    }


    @Override
    public void onPanelSlide(View panel, float slideOffset) {
    }

    @Override
    public void onPanelExpanded(View panel) {
//                Toast.makeText(MainActivity.this, "open sesame!", Toast.LENGTH_SHORT).show();

//        //Backend
//        RemoteClient client = new RemoteClient(this, "http://scavenger-game.appspot.com");
//        client.initObjectivesGetRequest();
    }

    @Override
    public void onPanelCollapsed(View panel) {

    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
//
//        // Inflate the layout for this fragment
//        return rootView;
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        FragmentManager fm = MainActivity.fragmentManager;
        SupportMapFragment mapFragment = (SupportMapFragment) MainActivity.fragmentManager
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onListFragmentInteraction(IObjective item) {
        //TODO: call API for Objective
//        Toast.makeText(this, "Clicked it!", Toast.LENGTH_SHORT).show();

/*        mMap.clear();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        LatLng objectivePos = new LatLng(item.getLatitude(), item.getLongitude());
        mMap.addMarker(new MarkerOptions().position(objectivePos).title(item.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(objectivePos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));*/
    }

    public void updateObjectiveCoordinates(List<IObjective> objectives)
    {
        ObjectiveRecyclerViewAdapter adapter = new ObjectiveRecyclerViewAdapter(objectives, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.invalidate();
    }
}