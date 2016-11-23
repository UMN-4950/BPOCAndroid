package edu.umn.bpoc.bpocandroid.map;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

public class LocationHelper {

    public static void toastLocation(Location location, Context context) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        String toast = "Lat: " + lat + "\nLon: " + lon;
        Toast.makeText(context, toast,
                Toast.LENGTH_SHORT).show();
    }

    public static String locationString(Location location, Context context) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        return "Lat: " + lat + " - Lon: " + lon;
    }
}
