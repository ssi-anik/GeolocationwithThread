package com.example.anik.geolocationwiththread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class CustomThread implements Runnable{
    private boolean shouldRunThread = true;
    private int i = 0;
    GPSTracker gpsTracker;
    Context context;
    Tracker tracker;
    public CustomThread(Context context){
        this.context = context;
        //gpsTracker = new GPSTracker(context);
        tracker = new Tracker(context);
    }

    public void changeThreadState(){
        shouldRunThread = !shouldRunThread;
    }

    public void showAlertMessageDialog(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //gpsTracker.showSettingsAlert();
            }
        });
    }
    @Override
    public void run() {
        while(shouldRunThread){
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ie){
                ie.printStackTrace();
            }

            /*if(!tracker.canGetLocation()){
                Log.v("Tracker", "is switched OFF");
                //showAlertMessageDialog();
            } else {
                Log.v("Tracker", "is switched ON");
                *//*double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                String message = "Latitude: " + latitude + " & Longitude: " + longitude;
                Log.v("Tracker: ", message);*//*
            }*/

        }
    }
}