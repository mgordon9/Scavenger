package com.project.ece150.scavenger;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ObjectivesFragment.OnListFragmentInteractionListener,
        IRemoteClientObserver,
        SlidingUpPanelLayout.PanelSlideListener{

    static final int PICK_ACCOUNT_REQUEST = 1002;

    String mAccountName;

    RemoteClient mRemoteClient;
    LocationClient mLocationClient;

    SlidingUpPanelLayout mLayout;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for OpenCV
        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }

        showGoogleAccountPicker();

        mRemoteClient = new RemoteClient("http://scavenger-game.appspot.com");
        mRemoteClient.registerObserver(this);
        mLocationClient = new LocationClient(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        RemoteClient client = new RemoteClient("http://scavenger-game.appspot.com");
        client.registerObserver(this);
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
/*        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
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

        Fragment fragment = null;

        if(id == R.id.nav_map) {
            fragment = new MapFragment(this);
        } else if (id == R.id.nav_completedobjectives) {
            fragment = new CompletedObjectivesFragment();
        } else if (id == R.id.nav_createobjective) {
            fragment = new CreateObjectiveFragment();
            ((CreateObjectiveFragment) fragment).initialize(mRemoteClient, mLocationClient, mAccountName);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(IObjective item) {
        //TODO: slide the panel down and zoom to chosen scavenger location
//        Toast.makeText(this, "Clicked it!", Toast.LENGTH_SHORT).show();

/*        mMap.clear();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        LatLng objectivePos = new LatLng(item.getLatitude(), item.getLongitude());
        mMap.addMarker(new MarkerOptions().position(objectivePos).title(item.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(objectivePos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));*/
    }

    @Override
    public void onUserGetReceived(IUser user) {
        Toast.makeText(MainActivity.this, "update users", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_ACCOUNT_REQUEST:
                if (resultCode == RESULT_OK) {
                    mAccountName = data.getStringExtra(
                            AccountManager.KEY_ACCOUNT_NAME);

                    createUserIfNeccessary(mAccountName);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "This application requires a Google account.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showGoogleAccountPicker() {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(googlePicker, PICK_ACCOUNT_REQUEST);
    }

    private void createUserIfNeccessary(String accountName) {
        mRemoteClient.initUserCreateRequest(accountName);
    }
}
