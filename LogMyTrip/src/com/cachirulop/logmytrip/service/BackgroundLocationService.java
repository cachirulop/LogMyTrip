package com.cachirulop.logmytrip.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.cachirulop.logmytrip.R;
import com.cachirulop.logmytrip.activity.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by danielealtomare on 8/30/13.
 */
public class BackgroundLocationService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener
{
    IBinder mBinder = new LocalBinder();

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    // Flag that indicates if a request is underway.
    private boolean mInProgress;
//    private boolean mActivityRecognitionInProgress;

    private Boolean servicesAvailable = false;

    private long mUpdateInterval = LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS;
    private long mUpdateFastestInterval = LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS;

    public class LocalBinder extends Binder
    {
        public BackgroundLocationService getServerInstance()
        {
            return BackgroundLocationService.this;
        }
    }


    @Override
    public void onCreate()
    {
        super.onCreate();

        mInProgress = false;

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the update interval
        mLocationRequest.setInterval(mUpdateInterval);

        // Set the fastest update interval
        mLocationRequest.setFastestInterval(mUpdateFastestInterval);

//        servicesAvailable = servicesConnected();

        // Create a new location client, using the enclosing class to handle callbacks.
        mLocationClient = new LocationClient(this, this, this);
    }


    private boolean servicesConnected()
    {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("BackgroundLocationService", "onStartCommand: " + startId);

        super.onStartCommand(intent, flags, startId);

        servicesAvailable = servicesConnected();

        if(!servicesAvailable || mLocationClient.isConnected() || mInProgress) // TODO
            return START_STICKY;

        setupClients();

        if(!mLocationClient.isConnected() || !mLocationClient.isConnecting() && !mInProgress)
        {
            mInProgress = true;
            mLocationClient.connect();
        }
        
        Notification note;
        Notification.Builder builder;
        Intent notificationIntent;
        PendingIntent pi;
        
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle ("Title");
        builder.setTicker("Ticker");
        builder.setContentText("Content text");

        notificationIntent = new Intent(this, MainActivity.class);
        pi = PendingIntent.getActivity (this, 0, notificationIntent, 0);
        
        builder.setContentIntent(pi);
        
        note = builder.build();
        
        startForeground(1337, note);
        
        return START_STICKY;
    }


    /*
     * Create a new location client, using the enclosing class to
     * handle callbacks.
     */
    private void setupClients()
    {
        if(mLocationClient == null)
            mLocationClient = new LocationClient(this, this, this);
    }


    // Define the callback method that receives location updates
    @Override
    public void onLocationChanged(Location location)
    {
        // Report to the UI that the location was updated
        Log.d("BackgroundLocationService", getTime() + ": " +
                Double.toString(location.getLatitude()) +
                "," + Double.toString(location.getLongitude()));
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }


    public String getTime()
    {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return mDateFormat.format(new Date());
    }


    @Override
    public void onDestroy()
    {
        // Turn off the request flag
        mInProgress = false;

        if(servicesAvailable && mLocationClient != null)
        {
            mLocationClient.removeLocationUpdates(this);

            // Destroy the current location client
            mLocationClient = null;
        }

        super.onDestroy();
    }


    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle)
    {
        // Request location updates using static settings
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }


    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected()
    {
        // Turn off the request flag
        mInProgress = false;

        // Destroy the current location client
        mLocationClient = null;
    }


    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        mInProgress = false;

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution())
        {
            // If no resolution is available, display an error dialog
        }
        else
        {

        }
    }
}