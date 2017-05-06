package edu.umn.bpoc.bpocandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.umn.bpoc.bpocandroid.LocationController;
import edu.umn.bpoc.bpocandroid.LocationHelper;
import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.resource.FakeFriend;
import edu.umn.bpoc.bpocandroid.utilities.CircleAnimation;
import edu.umn.bpoc.bpocandroid.utilities.LatLngInterpolator;
import edu.umn.bpoc.bpocandroid.utilities.Util;
import edu.umn.bpoc.bpocandroid.fragment.*;

import static java.lang.Math.pow;

public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LocationController locationController;
    private Button getLocationButton;
    private Button moveToCampusButton;
    private TextView locationStatus;
    private GoogleApiClient mGoogleApiClient;
    private View mapView;
    private GoogleMap mGoogleMap;
    private boolean requestingPermission = false; // Prevents toasts from being generated when requesting permissions

    private List<Circle> circles;
    private List<FakeFriend> fakeFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationController.setGoogleApiClient(mGoogleApiClient);
        locationController.setLocationListener(this);
        locationController.createLocationRequest();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_maps_menu, menu);
//        return true;
//    }

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
                        if (!requestingPermission)
                            Util.generateToast("signed out", getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                    }
                });
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
        } else if(id == R.id.nav_notification) {
            fragment = new NotificationFragment();
            TextView textView = (TextView)findViewById(R.id.toolbar_title);
            textView.setText("Notification");
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        locationController.permissionsCallback(requestCode, permissions, grantResults, this.getCurrentFocus(), mGoogleMap);
        requestingPermission = false;

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationController.settingsCallback(requestCode, resultCode, data, this.getCurrentFocus(), mGoogleMap);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("CONNECT_LOG", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("CONNECT_LOG", "Connection failed");
    }

    @Override
    public void onConnected(Bundle bundle) {
        requestingPermission = !locationController.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (!requestingPermission)
            Util.generateToast("Connected", getApplicationContext());
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle b) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        circles = new ArrayList<Circle>();
        requestingPermission = !locationController.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

        // startup view
        Location startLocation = locationController.getLocation(this.getCurrentFocus(), mGoogleMap);
        if (startLocation != null) {
            locationStatus.setText(LocationHelper.locationString(startLocation, getApplicationContext()));
        } else {
            locationStatus.setText("null location");
        }

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Location currentLocation = locationController.getLocation(view, mGoogleMap);
                requestingPermission = !locationController.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (currentLocation != null && !requestingPermission) {
                    LocationHelper.toastLocation(currentLocation, getApplicationContext());
                } else if (!requestingPermission) {
                    Util.generateToast("null current location", getApplicationContext());
                }
            }
        });

        moveToCampusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationController.moveToCampus(mGoogleMap);
            }
        });

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom

            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0); // removes right anchor
            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT); // aligns to center
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0); // removes top anchor
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30); // left, top, right, bottom
        }

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                float zoom = googleMap.getCameraPosition().zoom;
                float sqrZoom = googleMap.getCameraPosition().zoom*googleMap.getCameraPosition().zoom;
                for (int i = 0; i < circles.size(); i++) {
                    circles.get(i).setRadius(750000/pow(2, zoom));
                    circles.get(i).setStrokeWidth(2.5f);
                }

            }
        });

        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                Random r = new Random();
                CircleAnimation.animateCircleToGB(circle,
                        new LatLng(44.9740 + r.nextFloat()*.5 - .25, -93.2277 + r.nextFloat()*.5 - .25),
                        new LatLngInterpolator.Linear());
            }
        });

        // test circle
        Random r = new Random();
        circles.add(mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(44.9740, -93.2277))
                .radius(750000/pow(2, mGoogleMap.getCameraPosition().zoom))
                .strokeWidth(2.5f)
                .strokeColor(Color.WHITE)
                .fillColor(Color.RED)
                .clickable(true)));

        // populate FakeFriends
        double[] lat1 = {44.974580000, 44.974630000, 44.974800000, 44.974900000, 44.974900000, 44.974900000, 44.974890000, 44.974890000, 44.975670000, 44.976020000, 44.976020000, 44.976020000, 44.976020000, 44.976040000, 44.976060000, 44.976070000, 44.976080000, 44.976090000, 44.976100000, 44.976110000, 44.976130000, 44.976140000, 44.976150000, 44.976160000, 44.976180000, 44.976240000, 44.976370000, 44.976640000, 44.976720000, 44.976830000, 44.976940000, 44.977020000, 44.977030000, 44.977110000, 44.977210000, 44.977280000, 44.977380000, 44.977420000, 44.977460000, 44.977500000, 44.977550000, 44.977610000, 44.977690000, 44.978000000, 44.978110000, 44.978130000, 44.978150000, 44.978170000, 44.978170000, 44.978180000, 44.978200000, 44.978220000, 44.978410000, 44.978450000, 44.978480000, 44.978500000, 44.978520000, 44.978530000, 44.978530000, 44.978530000, 44.978670000, 44.978710000, 44.978760000, 44.979230000, 44.979380000, 44.979380000, 44.979420000, 44.979530000, 44.979530000, 44.979560000, 44.979590000, 44.979690000, 44.979780000, 44.979920000, 44.980070000, 44.980490000, 44.981470000, 44.982030000};
        double[] lat2 = {44.970630000, 44.970600000, 44.970600000, 44.970640000, 44.970670000, 44.970700000, 44.970730000, 44.970770000, 44.970820000, 44.970820000, 44.970800000, 44.970800000, 44.971160000, 44.971260000, 44.971430000, 44.971440000, 44.971450000, 44.971450000, 44.971460000, 44.971470000, 44.971470000, 44.972140000, 44.972160000, 44.972170000, 44.972180000, 44.972180000, 44.972180000, 44.972180000, 44.972180000, 44.972180000, 44.972180000, 44.972280000, 44.972320000, 44.972360000, 44.972480000, 44.972530000, 44.972580000, 44.972620000, 44.972680000, 44.972680000, 44.972700000, 44.972750000, 44.972880000, 44.973000000, 44.973180000, 44.973260000, 44.973290000, 44.973310000, 44.973380000, 44.973410000, 44.973410000, 44.973380000, 44.973360000, 44.973350000, 44.973330000, 44.973320000, 44.973290000, 44.973240000, 44.973210000, 44.973200000, 44.973200000, 44.973190000, 44.973150000, 44.973150000, 44.973140000, 44.973140000, 44.973150000, 44.973150000, 44.973150000, 44.973150000, 44.973230000, 44.973230000, 44.973440000, 44.973590000, 44.973590000, 44.973590000, 44.973590000, 44.973600000, 44.973590000, 44.973590000, 44.973590000, 44.973590000, 44.973590000, 44.973590000, 44.973600000, 44.973610000, 44.973620000, 44.973610000, 44.973610000, 44.973610000, 44.973600000, 44.973600000, 44.973640000, 44.973680000, 44.973720000, 44.973850000, 44.974080000, 44.974120000, 44.974200000, 44.974360000, 44.974520000, 44.974740000, 44.974810000, 44.974880000, 44.974920000, 44.974950000, 44.975100000, 44.975160000, 44.975510000, 44.975570000, 44.975900000, 44.975900000, 44.975800000, 44.975760000, 44.975760000, 44.975680000, 44.975570000, 44.975530000, 44.975490000, 44.975480000, 44.975450000, 44.975450000};
        double[] lat3 = {44.971970000, 44.971940000, 44.971900000, 44.971870000, 44.971850000, 44.971830000, 44.971810000, 44.971670000, 44.971640000, 44.971450000, 44.971450000, 44.971440000, 44.971430000, 44.971410000, 44.971410000, 44.971590000, 44.971860000, 44.972140000, 44.972280000, 44.972350000, 44.972530000, 44.972810000, 44.973140000, 44.973180000, 44.973600000, 44.973630000, 44.973730000, 44.973750000, 44.973750000, 44.973750000, 44.973750000, 44.973750000, 44.973870000, 44.973900000, 44.973920000, 44.973940000, 44.973980000, 44.974000000, 44.974010000, 44.974020000, 44.974030000, 44.974040000, 44.974060000, 44.974070000, 44.974090000, 44.974120000, 44.974400000, 44.974420000, 44.974580000, 44.974630000, 44.974800000, 44.974900000, 44.974900000, 44.974900000, 44.974890000, 44.974890000, 44.974890000, 44.974890000, 44.975230000, 44.975330000, 44.975650000, 44.975710000, 44.975710000, 44.975710000, 44.975740000, 44.975740000, 44.975740000, 44.975740000, 44.975740000, 44.975750000, 44.975770000, 44.975860000, 44.975860000, 44.975870000, 44.975870000, 44.976030000, 44.976030000, 44.976040000, 44.976380000, 44.976530000, 44.976740000, 44.976830000, 44.977060000, 44.977150000, 44.977150000, 44.977180000, 44.977200000, 44.977230000, 44.977280000, 44.977340000, 44.977340000, 44.977320000, 44.977330000, 44.977370000, 44.977370000, 44.977170000, 44.977120000, 44.977110000, 44.977090000, 44.977080000, 44.977070000, 44.977030000};
        double[] lat4 = {44.978850000, 44.979080000, 44.979190000, 44.979290000, 44.979530000, 44.979780000, 44.979830000, 44.979830000, 44.979540000, 44.979380000, 44.979230000, 44.978840000, 44.978400000, 44.978120000, 44.977860000, 44.977860000, 44.977630000, 44.977590000, 44.977570000, 44.977550000, 44.977520000, 44.977490000, 44.977460000, 44.977430000, 44.977320000, 44.977310000, 44.977200000, 44.977050000, 44.976910000, 44.976720000, 44.976650000, 44.976650000, 44.976650000, 44.976650000, 44.976370000, 44.976100000, 44.976100000, 44.976030000, 44.976030000, 44.976030000, 44.975860000, 44.975860000, 44.975860000, 44.975860000, 44.975760000, 44.975750000, 44.975740000, 44.975740000, 44.975740000, 44.975740000, 44.975710000, 44.975710000, 44.975710000, 44.975650000, 44.975650000, 44.975660000, 44.975660000, 44.975280000, 44.975280000, 44.975280000, 44.975280000, 44.975270000, 44.975240000, 44.975240000, 44.975240000, 44.975240000, 44.975230000, 44.975210000, 44.975210000, 44.975210000, 44.975210000, 44.975210000, 44.975120000, 44.975040000, 44.975030000, 44.974860000, 44.974730000, 44.974600000, 44.974130000, 44.974010000, 44.973880000, 44.973560000, 44.973540000, 44.973480000, 44.973460000, 44.973430000, 44.973420000, 44.973370000, 44.973350000, 44.973340000, 44.973310000, 44.973200000, 44.973010000, 44.972960000, 44.972910000, 44.972860000, 44.972770000, 44.972640000, 44.972620000, 44.972400000, 44.972160000, 44.972160000, 44.972130000, 44.972110000, 44.972070000, 44.972070000, 44.972070000};
        double[] lat5 = {44.975000000, 44.974890000, 44.974850000, 44.974380000, 44.974280000, 44.974270000, 44.974260000, 44.974250000, 44.974210000, 44.974210000, 44.974200000, 44.974190000, 44.974180000, 44.974140000, 44.974120000, 44.974110000, 44.974100000, 44.974090000, 44.974080000, 44.974080000, 44.974060000, 44.973990000, 44.973970000, 44.973960000, 44.973950000, 44.973940000, 44.973930000, 44.973920000, 44.973910000, 44.973870000, 44.973790000, 44.973790000, 44.973780000, 44.973780000, 44.973770000, 44.973750000, 44.973750000, 44.973750000, 44.973740000, 44.973740000, 44.973710000, 44.973710000, 44.973700000, 44.973700000, 44.973710000, 44.973710000, 44.973750000, 44.973760000, 44.973770000, 44.973790000, 44.973800000, 44.973800000, 44.973800000, 44.974020000, 44.974070000, 44.974070000, 44.974080000, 44.974080000, 44.973870000, 44.973780000, 44.973780000, 44.973780000, 44.973780000, 44.973780000, 44.973770000, 44.973710000, 44.973660000, 44.973620000, 44.973600000, 44.973580000, 44.973550000, 44.973480000, 44.973410000, 44.972910000, 44.972870000, 44.972860000, 44.972840000, 44.972810000, 44.972780000, 44.972720000, 44.972690000, 44.972680000, 44.972670000, 44.972670000, 44.972680000, 44.972680000, 44.972780000, 44.972800000, 44.972800000, 44.972830000, 44.972830000, 44.972830000, 44.972830000, 44.972830000, 44.972840000, 44.972850000, 44.972870000, 44.972890000, 44.972920000, 44.972980000, 44.973330000, 44.973360000, 44.973360000, 44.973370000, 44.973370000, 44.973370000, 44.973370000, 44.973380000};

        double[] long1 = {-93.232750000, -93.232820000, -93.233050000, -93.233200000, -93.233200000, -93.233260000, -93.233860000, -93.233860000, -93.233870000, -93.233880000, -93.233880000, -93.233960000, -93.233960000, -93.233960000, -93.233960000, -93.233960000, -93.233970000, -93.233970000, -93.233970000, -93.233980000, -93.233990000, -93.234000000, -93.234010000, -93.234030000, -93.234050000, -93.234140000, -93.234310000, -93.234690000, -93.234760000, -93.234710000, -93.235070000, -93.235030000, -93.235010000, -93.234940000, -93.234870000, -93.234810000, -93.234760000, -93.234750000, -93.234750000, -93.234740000, -93.234750000, -93.234770000, -93.234790000, -93.234930000, -93.234970000, -93.234990000, -93.235000000, -93.235020000, -93.235030000, -93.235040000, -93.235060000, -93.235070000, -93.235140000, -93.235160000, -93.235190000, -93.235210000, -93.235250000, -93.235280000, -93.235290000, -93.235290000, -93.235630000, -93.235740000, -93.235830000, -93.236590000, -93.236650000, -93.236650000, -93.236630000, -93.236550000, -93.236550000, -93.236530000, -93.236510000, -93.236430000, -93.236360000, -93.236240000, -93.236130000, -93.235780000, -93.235010000, -93.234570000};
        double[] long2 = {-93.244540000, -93.244480000, -93.244480000, -93.244360000, -93.244290000, -93.244250000, -93.244220000, -93.244200000, -93.244180000, -93.244180000, -93.244060000, -93.244060000, -93.244060000, -93.244060000, -93.244060000, -93.244060000, -93.244050000, -93.244040000, -93.244020000, -93.243130000, -93.243130000, -93.243090000, -93.243090000, -93.243070000, -93.243050000, -93.243020000, -93.242950000, -93.242910000, -93.242910000, -93.242640000, -93.242640000, -93.242680000, -93.242690000, -93.242690000, -93.242690000, -93.242430000, -93.242140000, -93.241850000, -93.241430000, -93.241390000, -93.241310000, -93.241020000, -93.240230000, -93.239530000, -93.238470000, -93.238140000, -93.238000000, -93.237900000, -93.237520000, -93.237290000, -93.237290000, -93.237280000, -93.237270000, -93.237240000, -93.237180000, -93.237110000, -93.236810000, -93.236610000, -93.236490000, -93.236470000, -93.236450000, -93.236420000, -93.236230000, -93.236190000, -93.235960000, -93.235710000, -93.235340000, -93.234990000, -93.234750000, -93.234750000, -93.234750000, -93.234750000, -93.234430000, -93.234220000, -93.234220000, -93.234050000, -93.233860000, -93.233030000, -93.232840000, -93.232030000, -93.231850000, -93.231810000, -93.231700000, -93.231590000, -93.230290000, -93.228840000, -93.228680000, -93.228350000, -93.227300000, -93.227210000, -93.227110000, -93.227110000, -93.227110000, -93.227110000, -93.227110000, -93.227110000, -93.227120000, -93.227110000, -93.227110000, -93.227110000, -93.227110000, -93.227100000, -93.227080000, -93.227060000, -93.227040000, -93.227010000, -93.226900000, -93.226860000, -93.226580000, -93.226530000, -93.226290000, -93.226290000, -93.226320000, -93.226320000, -93.226320000, -93.226080000, -93.225760000, -93.225620000, -93.225500000, -93.225440000, -93.225320000, -93.225250000};
        double[] long3 = {-93.229500000, -93.229510000, -93.229510000, -93.229510000, -93.229500000, -93.229500000, -93.229500000, -93.229490000, -93.229500000, -93.229500000, -93.229500000, -93.229560000, -93.229640000, -93.230280000, -93.230280000, -93.230280000, -93.230280000, -93.230280000, -93.230290000, -93.230290000, -93.230290000, -93.230290000, -93.230280000, -93.230280000, -93.230290000, -93.230290000, -93.230290000, -93.230290000, -93.230290000, -93.231860000, -93.232100000, -93.232100000, -93.232100000, -93.232100000, -93.232110000, -93.232130000, -93.232150000, -93.232160000, -93.232170000, -93.232170000, -93.232160000, -93.232160000, -93.232170000, -93.232170000, -93.232170000, -93.232180000, -93.232490000, -93.232520000, -93.232750000, -93.232820000, -93.233050000, -93.233200000, -93.233200000, -93.233260000, -93.233860000, -93.234500000, -93.234990000, -93.234990000, -93.234980000, -93.234980000, -93.235000000, -93.234990000, -93.235370000, -93.235370000, -93.235370000, -93.235370000, -93.235520000, -93.235530000, -93.235540000, -93.235540000, -93.235550000, -93.235550000, -93.235550000, -93.235730000, -93.235730000, -93.235720000, -93.235720000, -93.235950000, -93.236440000, -93.236560000, -93.236500000, -93.236470000, -93.236410000, -93.236380000, -93.236380000, -93.236500000, -93.236550000, -93.236590000, -93.236650000, -93.236700000, -93.236700000, -93.237130000, -93.237190000, -93.237470000, -93.237470000, -93.237570000, -93.237610000, -93.237610000, -93.237630000, -93.237650000, -93.237680000, -93.237880000};
        double[] long4 = {-93.228420000, -93.229000000, -93.229260000, -93.229520000, -93.230100000, -93.230710000, -93.230840000, -93.230840000, -93.231070000, -93.231200000, -93.231320000, -93.231650000, -93.232000000, -93.232220000, -93.232430000, -93.232430000, -93.232610000, -93.232640000, -93.232660000, -93.232680000, -93.232700000, -93.232730000, -93.232760000, -93.232800000, -93.232950000, -93.232960000, -93.233130000, -93.233330000, -93.233520000, -93.233670000, -93.233710000, -93.233710000, -93.233920000, -93.233920000, -93.234310000, -93.234700000, -93.234700000, -93.234700000, -93.234700000, -93.234990000, -93.234990000, -93.234990000, -93.235190000, -93.235190000, -93.235190000, -93.235190000, -93.235200000, -93.235210000, -93.235370000, -93.235370000, -93.235370000, -93.235370000, -93.235730000, -93.235730000, -93.236780000, -93.236880000, -93.236880000, -93.236890000, -93.236890000, -93.237200000, -93.237210000, -93.237220000, -93.237250000, -93.237260000, -93.237540000, -93.237590000, -93.237630000, -93.237660000, -93.237680000, -93.237710000, -93.237850000, -93.237850000, -93.237850000, -93.237860000, -93.237860000, -93.237870000, -93.237870000, -93.237880000, -93.237890000, -93.237890000, -93.237890000, -93.237900000, -93.237900000, -93.237900000, -93.237900000, -93.237900000, -93.237900000, -93.237900000, -93.237900000, -93.237900000, -93.237900000, -93.237910000, -93.237910000, -93.237910000, -93.237910000, -93.237910000, -93.237920000, -93.237900000, -93.237890000, -93.237830000, -93.237740000, -93.237740000, -93.237700000, -93.237670000, -93.237640000, -93.237640000, -93.236900000};
        double[] long5 = {-93.227440000, -93.227490000, -93.227510000, -93.227730000, -93.227850000, -93.227870000, -93.227880000, -93.227920000, -93.228000000, -93.228010000, -93.228020000, -93.228020000, -93.228020000, -93.228010000, -93.228010000, -93.228010000, -93.228020000, -93.228050000, -93.228070000, -93.228080000, -93.228090000, -93.228090000, -93.228100000, -93.228110000, -93.228140000, -93.228160000, -93.228170000, -93.228190000, -93.228200000, -93.228220000, -93.228260000, -93.228260000, -93.228420000, -93.228530000, -93.228690000, -93.230290000, -93.231860000, -93.232100000, -93.233740000, -93.233860000, -93.234220000, -93.234320000, -93.234530000, -93.234690000, -93.235450000, -93.235450000, -93.235450000, -93.235470000, -93.235480000, -93.235500000, -93.235550000, -93.235670000, -93.235740000, -93.235710000, -93.235720000, -93.235720000, -93.236690000, -93.236690000, -93.236690000, -93.236690000, -93.236790000, -93.236860000, -93.236900000, -93.236960000, -93.237090000, -93.237230000, -93.237380000, -93.237440000, -93.237470000, -93.237510000, -93.237630000, -93.237920000, -93.238210000, -93.241020000, -93.241260000, -93.241320000, -93.241410000, -93.241600000, -93.241890000, -93.242290000, -93.242490000, -93.242560000, -93.242620000, -93.242650000, -93.242680000, -93.242680000, -93.242680000, -93.242680000, -93.242680000, -93.242720000, -93.242740000, -93.242760000, -93.242800000, -93.242900000, -93.242940000, -93.242980000, -93.243030000, -93.243070000, -93.243110000, -93.243200000, -93.243690000, -93.243730000, -93.243750000, -93.243780000, -93.243820000, -93.243890000, -93.243930000, -93.243990000};

        FakeFriend fakeFriend1 = new FakeFriend(1, "bob1", "status1", 2.0, lat1, long1);
        FakeFriend fakeFriend2 = new FakeFriend(2, "bob2", "status2", 2.0, lat2, long2);
        FakeFriend fakeFriend3 = new FakeFriend(3, "bob3", "status3", 2.0, lat3, long3);
        FakeFriend fakeFriend4 = new FakeFriend(4, "bob4", "status4", 2.0, lat4, long4);
        FakeFriend fakeFriend5 = new FakeFriend(5, "bob5", "status5", 2.0, lat5, long5);

        fakeFriends.add(fakeFriend1);
        fakeFriends.add(fakeFriend2);
        fakeFriends.add(fakeFriend3);
        fakeFriends.add(fakeFriend4);
        fakeFriends.add(fakeFriend5);
    }
}
