package com.example.anik.geolocationwiththread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPS implements Runnable, LocationListener {

    private Context context;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private Activity activity;

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean shouldRunThread;
    private boolean alertDisplayed;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    double latitude;
    double longitude;
    String provider;

    public GPS(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        shouldRunThread = true;
        alertDisplayed = false;
        canGetLocation();
        updateLocation();
    }

    public void changeThreadFlag(){
        shouldRunThread = false;
    }

    public void showMessage(final String type, final String message){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(type == "toast")
                    showToast(message);
                else if(type == "alert")
                    showSettingsAlert();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }

    public void showSettingsAlert(){
        if(alertDisplayed)
            return;
        if(canGetLocation())
            return;
        alertDisplayed = true;

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is disabled");

        // Setting Dialog Message
        alertDialog.setMessage("You must have to enable GPS.\nClick settings to enable.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                alertDisplayed = false;
                activity.startActivityForResult(intent, 1);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDisplayed = false;
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("On", "Activity result");
        if (resultCode == activity.RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    updateLocation();
                    break;
            }
        }
    }


    public boolean canGetLocation(){
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled /*&& !isNetworkEnabled*/){
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDisplayed = false;
        return false;
    }


    public Location updateLocation() {
        Log.v("Location", "Update");
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {/*
                if (location == null) {*/
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                }/*}*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public double getLatitude(){
        if(this.location != null){
            latitude = this.location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(this.location != null){
            longitude = this.location.getLongitude();
        }
        return longitude;
    }

    @Override
    public void run() {
        int i = 0;
        while(shouldRunThread){
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.v("Tread", "" + (++i));
            if(canGetLocation()){
                /*updateLocation();*/
                Log.v("Network", "Available");
                double lat = getLatitude();
                double longi = getLongitude();
                Log.v("Position", "Latitude " + lat + " Longitude " + longi);
                try{
                    showMessage("toast", "Latitude " + getLatitude() + "\nLongitude " + getLongitude() + "\nAddress: " + getGeoAddress(lat, longi));
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else{
                Log.v("Network", "Unavailable");
                showMessage("alert", null);
            }
        }
    }

    public String getGeoAddress(double latitude, double longitude) throws IOException {
        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList = myLocation.getFromLocation(latitude, longitude, 1);
        Address address = (Address) myList.get(0);
        String addressStr = "";
        addressStr += address.getAddressLine(0) + ", ";
        addressStr += address.getAddressLine(1);
        return addressStr;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        alertDisplayed = false;
        showMessage("toast", "Provider enabled");
        updateLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
        showMessage("toast", "Provider disabled");
        alertDisplayed = false;
    }
}
