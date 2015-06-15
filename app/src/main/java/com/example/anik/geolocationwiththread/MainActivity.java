package com.example.anik.geolocationwiththread;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    TextView tvLocation;
    Button startThreadButton;

    Location location;
    LocationProvider locationProvider;
    Context context;
    String provider;

    Thread thread;
    CustomThread customThread;
    GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = getApplicationContext();

        tvLocation = (TextView) findViewById(R.id.geolocation);
        startThreadButton = (Button) findViewById(R.id.startThread);


        startThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPS gps = new GPS(MainActivity.this, MainActivity.this);
                thread = new Thread(gps);
                thread.start();
                startThreadButton.setEnabled(false);
            }
        });

    }

    private void showToast(final String message){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
