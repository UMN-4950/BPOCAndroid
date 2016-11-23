package edu.umn.bpoc.bpocandroid.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.map.LocationController;

import static edu.umn.bpoc.bpocandroid.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationController locationController;
    private Button getLocationButton;
    private Button moveToCamusButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationController = new LocationController(getApplicationContext(), this);

        getLocationButton = (Button) findViewById(R.id.get_coordinate_button);
        moveToCamusButton = (Button) findViewById(R.id.move_to_campus_button);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final Location currentLocation = locationController.getLocation(this.getCurrentFocus(), googleMap);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationController.toastLocation(currentLocation);
            }
        });

        moveToCamusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationController.moveToCampus(googleMap);
            }
        });
    }

}
