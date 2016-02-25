package com.project.ece150.scavenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.ece150.scavenger.mocks.ObjectiveMock;
import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ObjectivesFragment.OnListFragmentInteractionListener,
        IRemoteClientObserver,
        SlidingUpPanelLayout.PanelSlideListener{

    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private UiSettings mUiSettings;

    LocationManager mLocationManager;
    Criteria mCriteria;
    String mProvider;
    SlidingUpPanelLayout mLayout;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Backend
        Objective obj = new Objective();
        obj.setObjectiveid("objididid1456384118597");
        RemoteClient client = new RemoteClient(this, "http://scavenger-game.appspot.com");
        //RemoteClient client = new RemoteClient(this, "http://10.0.2.2:8090");
        //client.initObjectivesCreateRequest(obj);
        client.initObjectiveGetRequest(obj);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startMap();
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mCriteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(mCriteria, true);
        

        ArrayList<IObjective> data = new ArrayList<IObjective>();
        Objective d0 = new Objective();
        d0.setInfo("info 0");
        d0.setOwner("Matthew");
        Objective d1 = new Objective();
        d1.setInfo("info 1");
        d1.setOwner("Matthew");
        data.add(d0);
        data.add(d1);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        ObjectiveRecyclerViewAdapter adapter = new ObjectiveRecyclerViewAdapter(data, this);

        mRecyclerView.setAdapter(adapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(this);

//        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//            }
//
//            @Override
//            public void onPanelExpanded(View panel) {
////                Toast.makeText(MainActivity.this, "open sesame!", Toast.LENGTH_SHORT).show();
//
//                //dummy data
//                ArrayList<IObjective> newList = new ArrayList<IObjective>();
//                Objective d0 = new Objective();
//                d0.setInfo("info 9");
//                d0.setOwner("Matthew");
//                d0.setLatitude(50.51);
//                d0.setLongitude(34.24);
//                d0.setTitle("Objective 0");
//                Objective d1 = new Objective();
//                d1.setInfo("info 7");
//                d1.setOwner("Matthew");
//                d1.setLatitude(37.24);
//                d1.setLongitude(123.51);
//                d1.setTitle("Objective 1");
//                newList.add(d0);
//                newList.add(d1);
//                ObjectiveRecyclerViewAdapter adapter = new ObjectiveRecyclerViewAdapter(newList, MainActivity.this);
//                mRecyclerView.setAdapter(adapter);
//                mRecyclerView.invalidate();
//
//                //Backend
//                ObjectivesClient client = new ObjectivesClient(MainAcivity.this, "http://scavenger-game.appspot.com/rest/ds");
//                client.initDataRequest();
//            }
//
//            @Override
//            public void onPanelCollapsed(View panel) {
//
//            }
//
//            @Override
//            public void onPanelAnchored(View panel) {
//
//            }
//
//            @Override
//            public void onPanelHidden(View panel) {
//
//            }
//        });

    }


    @Override
    public void onPanelSlide(View panel, float slideOffset) {
    }

    @Override
    public void onPanelExpanded(View panel) {
//                Toast.makeText(MainActivity.this, "open sesame!", Toast.LENGTH_SHORT).show();

        //Backend
        RemoteClient client = new RemoteClient(this, "http://scavenger-game.appspot.com");
        client.initObjectivesGetRequest();
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

    private void startMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Location location = mLocationManager.getLastKnownLocation(mProvider);

        LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

        mMap.setOnMyLocationButtonClickListener(this);
        mUiSettings = mMap.getUiSettings();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMap();
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onListFragmentInteraction(IObjective item) {
        //TODO: slide the panel down and zoom to chosen scavenger location
//        Toast.makeText(this, "Clicked it!", Toast.LENGTH_SHORT).show();

        mMap.clear();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        LatLng objectivePos = new LatLng(item.getLatitude(), item.getLongitude());
        mMap.addMarker(new MarkerOptions().position(objectivePos).title(item.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(objectivePos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
    }

    @Override
    public void onUserGetReceived(IUser user) {

    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {
        Toast.makeText(MainActivity.this, "update objectives", Toast.LENGTH_SHORT).show();

        ObjectiveRecyclerViewAdapter adapter = new ObjectiveRecyclerViewAdapter(objectives, MainActivity.this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.invalidate();
    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {
        Toast.makeText(MainActivity.this, "update objective", Toast.LENGTH_SHORT).show();
    }
}
