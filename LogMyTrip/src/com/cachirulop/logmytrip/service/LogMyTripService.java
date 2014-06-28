
package com.cachirulop.logmytrip.service;

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
import com.cachirulop.logmytrip.util.ToastHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

    private LocationClient             _locationClient;
    private LocationRequest            _locationRequest;

    private BluetoothBroadcastReceiver _btReceiver;
    private Object                     _lckReceiver                     = new Object ();

    IBinder                            _binder                          = new LogMyTrackServiceLocalBinder ();

    @Override
    public void onCreate ()
    {
        super.onCreate ();

        initLocation ();
    }

    @Override
    public void onDestroy ()
    {
        if (_locationClient != null) {
            stopLog ();
        }

        super.onDestroy ();
    }

    private void initLocation ()
    {
        _locationRequest = LocationRequest.create ();
        _locationRequest.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);
        _locationRequest.setInterval (LOCATION_UPDATE_INTERVAL);
        _locationRequest.setFastestInterval (LOCATION_UPDATE_FASTEST_INTERVAL);

        ensureLocationClient ();
    }

    private void ensureLocationClient ()
    {
        if (_locationClient == null) {
            _locationClient = new LocationClient (this,
                                                  this,
                                                  this);
        }
    }

    @Override
    public int onStartCommand (Intent intent,
                               int flags,
                               int startId)
    {

        if (!isGooglePlayServicesAvailable ()) {
            ToastHelper.showLong (this,
                                  getString (R.string.msg_GooglePlayServicesUnavailable));

            return Service.START_NOT_STICKY;
        }
        else {
            ToastHelper.showDebug (this,
                                   "LogMyTripService.onStartCommand: starting service");

        }

        super.onStartCommand (intent,
                              flags,
                              startId);

        boolean bluetooth;
        boolean logs;

        bluetooth = SettingsManager.getAutoStartLog (this);
        logs = SettingsManager.getLogTrip (this);

        synchronized (_lckReceiver) {
            if (bluetooth) {
                registerBluetoothReceiver ();
            }
            else {
                unregisterBluetoothReceiver ();
            }
        }

        if (logs) {
            startLog ();
        }
        else {
            stopLog ();
        }

        if (bluetooth || logs) {
            startForegroundService (bluetooth,
                                    logs);
        }
        else {
            stopForegroundService ();
        }

        return START_STICKY;
    }

    private void startLog ()
    {
        ensureLocationClient ();
        if (!_locationClient.isConnected () && !_locationClient.isConnecting ()) {
            _locationClient.connect ();
        }
    }

    private void stopLog ()
    {
        ensureLocationClient ();
        if (_locationClient.isConnected () || _locationClient.isConnecting ()) {
            _locationClient.removeLocationUpdates (this);
        }
    }

    private void stopForegroundService ()
    {
        stopForeground (true);
        stopSelf ();
    }

    private void startForegroundService (boolean bluetooth,
                                         boolean logTrip)
    {
        Notification note;
        int contentId;

        if (bluetooth) {
            contentId = R.string.notif_ContentWaitingBluetooth;
        }
        else {
            contentId = R.string.notif_ContentSavingTrip;
        }

        // TODO: Specify the correct icon
        note = NotifyManager.createNotification (this,
                                                 contentId);

        startForeground (NotifyManager.NOTIFICATION_ID,
                         note);
    }

    private void registerBluetoothReceiver ()
    {
        if (_btReceiver == null) {
            _btReceiver = new BluetoothBroadcastReceiver ();

            registerReceiver (_btReceiver,
                              new IntentFilter (BluetoothDevice.ACTION_ACL_DISCONNECTED));

            registerReceiver (_btReceiver,
                              new IntentFilter (BluetoothDevice.ACTION_ACL_CONNECTED));
        }
    }

    private void unregisterBluetoothReceiver ()
    {
        if (_btReceiver != null) {
            unregisterReceiver (_btReceiver);

            _btReceiver = null;
        }
    }

    private boolean isGooglePlayServicesAvailable ()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable (this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onLocationChanged (Location loc)
    {
        // TODO: Save to the database
        ToastHelper.showShortDebug (this,
                                    "LogMyTripService.onLocationChanged: " +
                                            loc.getLatitude () + "-.-" +
                                            loc.getLongitude ());

    }

    @Override
    public void onConnectionFailed (ConnectionResult arg0)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnected (Bundle arg0)
    {
        _locationClient.requestLocationUpdates (_locationRequest,
                                                this);
    }

    @Override
    public void onDisconnected ()
    {
        _locationClient = null;
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
