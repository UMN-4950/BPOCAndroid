package edu.umn.bpoc.bpocandroid;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class LocationController {

    private Context locationContext;
    private Activity locationActivity;

    public LocationController(Context context, Activity activity) {
        locationContext = context;
        locationActivity = activity;
    }

    public Location getLocation(View view, GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(locationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(locationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(locationActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                askForLocationPermission();
                Log.d("MAP_LOG", "Location access denied");
            } else {
                askForLocationPermission();
            }
            return null;
        }

        googleMap.setMyLocationEnabled(true);
        
        UiSettings googleMapUiSettings = googleMap.getUiSettings();
        googleMapUiSettings.setZoomControlsEnabled(true);
        googleMapUiSettings.setTiltGesturesEnabled(false);

        LocationManager locationManager = (LocationManager) locationContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            return location;
        }
        return null;
    }

    public void moveToCampus(GoogleMap googleMap) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.9740, -93.2277), 14.0f));
    }

    private void askForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(locationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(locationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(locationActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("MAP_LOG", "Location access denied");
            } else {
                ActivityCompat.requestPermissions(locationActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        }
    }

}
