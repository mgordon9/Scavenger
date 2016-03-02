package com.project.ece150.scavenger;

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
import android.view.MenuItem;
import android.widget.Toast;

import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        IRemoteClientObserver{

    RemoteClient mRemoteClient;
    LocationClient mLocationClient;
    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            fragment = new MapFragment();
            ((MapFragment)fragment).initialize(mRemoteClient);
        } else if (id == R.id.nav_completedobjectives) {
            fragment = new CompletedObjectivesFragment();
        } else if (id == R.id.nav_createobjective) {
            fragment = new CreateObjectiveFragment();
            ((CreateObjectiveFragment) fragment).initialize(mRemoteClient, mLocationClient);
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
}
