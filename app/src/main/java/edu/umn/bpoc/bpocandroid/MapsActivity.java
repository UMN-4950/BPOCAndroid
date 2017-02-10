package edu.umn.bpoc.bpocandroid;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.vision.text.Line;

import bpocandroid.fragment.*;

public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private LocationController locationController;
    private Button getLocationButton;
    private Button moveToCampusButton;
    private TextView locationStatus;
    private GoogleApiClient mGoogleApiClient;
    private View mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * map/location section
         */
        locationController = new LocationController(getApplicationContext(), this);

        getLocationButton = (Button) findViewById(R.id.get_coordinate_button);
        moveToCampusButton = (Button) findViewById(R.id.move_to_campus_button);
        locationStatus = (TextView) findViewById(R.id.latLongStatus);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_sign_out) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    Util.generateToast("signed out", getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        LinearLayout buttons = (LinearLayout)findViewById(R.id.buttons);
        TextView latlongStatus = (TextView)findViewById(R.id.latLongStatus);

        if (id == R.id.nav_map) {
            buttons.setVisibility(View.VISIBLE);
            latlongStatus.setVisibility(View.VISIBLE);
            fragment = new MapFragment();
            TextView textView = (TextView)findViewById(R.id.toolbar_title);
            textView.setText("Map");
            //Handle the camera action
        } else if (id == R.id.nav_friendlist) {
            fragment = new FriendListFragment();
            TextView textView = (TextView)findViewById(R.id.toolbar_title);
            textView.setText("Friend List");
            buttons.setVisibility(View.GONE);
            latlongStatus.setVisibility(View.GONE);
        } else if (id == R.id.nav_eventlist) {
            fragment = new EventListFragment();
            TextView textView = (TextView)findViewById(R.id.toolbar_title);
            textView.setText("Event List");
            buttons.setVisibility(View.GONE);
            latlongStatus.setVisibility(View.GONE);
        } else if (id == R.id.nav_setting) {
            fragment = new SettingFragment();
            TextView textView = (TextView)findViewById(R.id.toolbar_title);
            textView.setText("Setting");
            buttons.setVisibility(View.GONE);
            latlongStatus.setVisibility(View.GONE);
        }

        if(fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        Util.generateToast("changed", getApplicationContext());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // startup view
        Location startLocation = locationController.getLocation(this.getCurrentFocus(), googleMap);
        if (startLocation != null) {
            locationStatus.setText(LocationHelper.locationString(startLocation, getApplicationContext()));
        }
        else {
            locationStatus.setText("null location");
        }

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Location currentLocation = locationController.getLocation(view, googleMap);
                if (currentLocation != null) {
                    LocationHelper.toastLocation(currentLocation, getApplicationContext());
                }
                else {
                    Util.generateToast("null current location", getApplicationContext());
                }
            }
        });

        moveToCampusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationController.moveToCampus(googleMap);
            }
        });

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0); // removes right anchor
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT); // aligns to center
            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0); // removes top anchor
            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(30, 30, 30, 30); // left, top, right, bottom
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
