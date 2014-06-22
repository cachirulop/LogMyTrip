
package com.cachirulop.logmytrip.service;

import util.ToastHelper;
import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.cachirulop.logmytrip.R;
import com.cachirulop.logmytrip.manager.NotifyManager;
import com.cachirulop.logmytrip.manager.SettingsManager;
import com.cachirulop.logmytrip.receiver.BluetoothBroadcastReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LogMyTripService
        extends Service
        implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener
{
    private static final long          LOCATION_UPDATE_INTERVAL         = 5000;
    private static final long          LOCATION_UPDATE_FASTEST_INTERVAL = 5000;
    private static final int           FOREGROUND_ID                    = 33233;

    private LocationClient             _locationClient;
    private LocationRequest            _locationRequest;

    private BluetoothBroadcastReceiver _btReceiver;

    IBinder                            _binder                          = new LogMyTrackServiceLocalBinder ();

    @Override
    public void onCreate ()
    {
        super.onCreate ();

        initLocation ();
        initBluetooth ();
    }

    private void initLocation ()
    {
        _locationRequest = LocationRequest.create ();
        _locationRequest.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);
        _locationRequest.setInterval (LOCATION_UPDATE_INTERVAL);
        _locationRequest.setFastestInterval (LOCATION_UPDATE_FASTEST_INTERVAL);

        _locationClient = new LocationClient (this,
                                              this,
                                              this);
    }

    private void initBluetooth ()
    {
        _btReceiver = new BluetoothBroadcastReceiver ();
    }

    @Override
    public int onStartCommand (Intent intent,
                               int flags,
                               int startId)
    {
        ToastHelper.showDebug (this,
                               "LogMyTripService.onStartCommand: starting service");

        super.onStartCommand (intent,
                              flags,
                              startId);
        
        if (SettingsManager.getAutoStartLog (this)) {
            registerBluetoothReceiver ();
        }

        return START_STICKY;
    }
    
    public void startLog () 
    {
        if (!_locationClient.isConnected () && !_locationClient.isConnecting ()) {
            _locationClient.connect ();

            // TODO: update the notification message
        }
    }

    public void registerBluetoothReceiver ()
    {
        registerReceiver (_btReceiver,
                          new IntentFilter (BluetoothDevice.ACTION_ACL_DISCONNECTED));

        registerReceiver (_btReceiver,
                          new IntentFilter (BluetoothDevice.ACTION_ACL_CONNECTED));
        
        Notification note;

        note = NotifyManager.createNotification (this,
                                                 R.string.notif_ContentWaitingBluetooth);

        startForeground (FOREGROUND_ID,
                         note);
    }

    public void unregisterBluetoothReceiver ()
    {
        unregisterReceiver (_btReceiver);
        stopForeground (true);
    }
    
    @Override
    public void onLocationChanged (Location arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed (ConnectionResult arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected (Bundle arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnected ()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind (Intent intent)
    {
        return _binder;
    }

    public class LogMyTrackServiceLocalBinder
            extends Binder
    {
        public LogMyTripService getServerInstance ()
        {
            return LogMyTripService.this;
        }
    }

}
