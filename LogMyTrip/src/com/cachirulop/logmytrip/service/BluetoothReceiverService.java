
package com.cachirulop.logmytrip.service;

import com.cachirulop.logmytrip.R;
import com.cachirulop.logmytrip.manager.NotifyManager;
import com.cachirulop.logmytrip.manager.SettingsManager;
import com.cachirulop.logmytrip.receiver.BluetoothBroadcastReceiver;
import com.cachirulop.logmytrip.util.ToastHelper;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BluetoothReceiverService
        extends Service
{
    private BluetoothBroadcastReceiver _btReceiver;

    @Override
    public void onCreate ()
    {
        super.onCreate ();

        _btReceiver = new BluetoothBroadcastReceiver ();
    }
    
    @Override
    public int onStartCommand (Intent intent,
                               int flags,
                               int startId)
    {
        ToastHelper.showDebug (this,
                               "BluetoothReceiverService.onStartCommand: starting service");

        super.onStartCommand (intent,
                              flags,
                              startId);
        
        if (SettingsManager.getAutoStartLog (this)) {
            registerBluetoothReceiver ();
        }

        return START_STICKY;
    }
    

    @Override
    public void onDestroy ()
    {
        ToastHelper.showDebug (this,
                "BluetoothReceiverService.onDestroy: stoping service");

        unregisterBluetoothReceiver ();
        
        super.onDestroy ();
    }

    @Override
    public IBinder onBind (Intent intent)
    {
        return null;
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

        startForeground (NotifyManager.NOTIFICATION_ID,
                         note);
    }
    
    public void unregisterBluetoothReceiver ()
    {
        unregisterReceiver (_btReceiver);
        stopForeground (true);
    }
}
