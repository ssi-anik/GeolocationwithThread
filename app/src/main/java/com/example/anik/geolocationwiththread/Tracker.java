package com.example.anik.geolocationwiththread;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Anik on 24-Apr-15, 024.
 */
public class Tracker extends Service implements LocationListener {

    Context context;
    Location location;
    LocationManager locationManager;
    Criteria criteria;
    String provider;

    public Tracker(Context context){
        this.context = context;
        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            Toast.makeText(context, "Provider " + provider + " has been selected", Toast.LENGTH_SHORT).show();
            onLocationChanged(location);
        } else {
            Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String message = "Latitude " + latitude + " Longitude " + longitude;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.v("Provider", provider + " Enabled");
        Toast.makeText(context, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.v("Provider", provider + " Disabled");
        Toast.makeText(context, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
