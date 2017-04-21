package edu.umn.bpoc.bpocandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class LocationController {

    private Context locationContext;
    private Activity locationActivity;
    private LocationRequest locationRequest;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private GoogleApiClient mGoogleApiClient;
    private boolean doNotShowAgain = false;
    private boolean requestingPermission = false;
    private boolean loopRequestLocation = false;

    public LocationController(Context context, Activity activity) {
        locationContext = context;
        locationActivity = activity;
        locationManager = (LocationManager) locationContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }

    public void setLocationListener(LocationListener listener) {
        locationListener = listener;
    }

    public void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Check permissions
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else {
            if (!doNotShowAgain && !requestingPermission) {
                ActivityCompat.requestPermissions(locationActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
                requestingPermission = true;
            }
        }
    }

    public void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d("MAP_LOG", "Location settings are on");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not on, but we can fix it
                        try {
                            Log.d("MAP_LOG", "Attempting location settings resolution");
                            status.startResolutionForResult(locationActivity, 1); // supposed to be REQUEST_CHECK_SETTINGS, but usually that equals 1
                        } catch (IntentSender.SendIntentException e) {
                            Log.d("MAP_LOG", "IntentException caught from location settings resolution");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not on, but we have no way of fixing it.
                        Log.d("MAP_LOG", "Location settings are unavailable");
                        break;
                    default:
                        Log.d("MAP_LOG", "Location settings status unknown");
                }
            }
        });
    }

    public void settingsCallback(int requestCode, int resultCode, Intent data, View view, GoogleMap googleMap) {
        Log.d("MAP_LOG", "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" +
                resultCode + "], data = [" + data + "].");
        switch (requestCode) {
            case 1: /*REQUEST_CHECK_SETTINGS*/
                if (resultCode == 0) {
                    return;
                }
                createLocationRequest();

                loopRequestLocation = true;
                getLocation(view, googleMap);
                break;
            case 2: /*REQUEST_CODE_RESOLUTION*/
                // Do nothing?
                break;
            default:;
        }
    }

    public boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(locationContext, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void permissionsCallback(int requestCode, String permissions[], int[] grantResults, View view, GoogleMap googleMap) {
        requestingPermission = false;
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(locationActivity, permissions[i])) {
                        // TODO: Show message to user about denied permission request with "don't show again"
                        doNotShowAgain = true;
                    }
                    return;
                }
            }
        }

        getLocation(view, googleMap);
    }

    public Location getLocation(View view, GoogleMap googleMap) {
        // Check permissions
        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (!doNotShowAgain && !requestingPermission) {
                ActivityCompat.requestPermissions(locationActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
                requestingPermission = true;
            }
            return null;
        }

        checkLocationSettings();

        googleMap.setMyLocationEnabled(true);
        
        UiSettings googleMapUiSettings = googleMap.getUiSettings();
        googleMapUiSettings.setZoomControlsEnabled(false);
        googleMapUiSettings.setTiltGesturesEnabled(false);

        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            postLocation(location);

            loopRequestLocation = false;
            return location;
        }
        if (loopRequestLocation) { // If we want the location to be updated ASAP
            final View v = view;
            final GoogleMap g = googleMap;
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            getLocation(v, g);
                        }
                    }, 1000);
        }
        return null;
    }

    private void postLocation(Location location) {
        class PostLocationTask extends DatabaseTask {
            @Override
            protected void onPostExecute(String result) {
                if (responseCode != 200) {
                    Log.d("MAP_LOG", "Response Code: " + responseCode);
                    //TODO: Handle bad response
                    return;
                }
                else {
                    Log.d("MAP_LOG", "Post successful");
                }
            }
        }

        PostLocationTask task = new PostLocationTask();
        Gson gson = new Gson();
        task.setPostData(gson.toJson(location));
        task.call("users/updatelocation/" + UserAccount.getDBId());
    }

    public void moveToCampus(GoogleMap googleMap) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.9740, -93.2277), 14.0f));
    }
}
