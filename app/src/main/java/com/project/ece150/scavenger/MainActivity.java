package com.project.ece150.scavenger;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.project.ece150.scavenger.remote.EObjectiveConfirmedType;
import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;

import org.opencv.android.OpenCVLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        IRemoteClientObserver{

    static final int PICK_ACCOUNT_REQUEST = 1002;
    static final int CONFIRM_OBJECTIVE_REQUEST = 1009;

    public String mAccountName;

    RemoteClient mRemoteClient;
    LocationClient mLocationClient;

    MapFragment mMapFragment;
    CompletedObjectivesFragment mCompletedObjectivesFragment;
    CreateObjectiveFragment mCreateObjectiveFragment;

    public static FragmentManager fragmentManager;

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

        fragmentManager = getSupportFragmentManager();

        // Select Map View as Default.
        Fragment fragment = getMapFragment();
        selectFragment(fragment);
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

        if(id == R.id.nav_map){
            fragment = getMapFragment();
        } else if (id == R.id.nav_completedobjectives) {
            fragment = getCompletedObjectivesFragment();
        } else if (id == R.id.nav_createobjective) {
            fragment = getCreateObjectiveFragment();
        }

        selectFragment(fragment);

        return true;
    }

    @Override
    public void onUserGetReceived(IUser user) {
        Toast.makeText(MainActivity.this, "update users", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {
        Toast.makeText(MainActivity.this, "update objectives", Toast.LENGTH_SHORT).show();
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

            case CONFIRM_OBJECTIVE_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    mRemoteClient.initUserAddObjectiveRequest(
                            mAccountName,
                            mMapFragment.getCurrentObjective().getObjectiveid(),
                            EObjectiveConfirmedType.VISUALLYCONFIRMED);
                }
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

    private Fragment getMapFragment() {
        if(mMapFragment == null) {
            mMapFragment = new MapFragment();
            mMapFragment.initialize(mRemoteClient, mAccountName);
        }

        return mMapFragment;
    }

    private Fragment getCompletedObjectivesFragment() {
        if(mCompletedObjectivesFragment == null) {
            mCompletedObjectivesFragment = new CompletedObjectivesFragment();
            mCompletedObjectivesFragment.initialize(mRemoteClient,"user1");
        }

        return mCompletedObjectivesFragment;
    }

    private Fragment getCreateObjectiveFragment() {
        if(mCreateObjectiveFragment == null) {
            mCreateObjectiveFragment = new CreateObjectiveFragment();
            ((CreateObjectiveFragment) mCreateObjectiveFragment).initialize(mRemoteClient, mLocationClient, mAccountName);
        }

        return mCreateObjectiveFragment;
    }

    private void selectFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
