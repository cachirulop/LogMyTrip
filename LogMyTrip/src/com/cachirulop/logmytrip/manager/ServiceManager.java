
package com.cachirulop.logmytrip.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cachirulop.logmytrip.service.LogMyTripService;
import com.cachirulop.logmytrip.service.LogMyTripService.LogMyTrackServiceLocalBinder;

public class ServiceManager
{

    public static void startService (Context ctx)
    {
        ctx.startService (new Intent (ctx,
                                      LogMyTripService.class));
    }
    
    public static void registerBluetoothBroadcastReceiver (Context ctx) 
    {
        final ServiceConnection _connection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                LogMyTrackServiceLocalBinder binder = (LogMyTrackServiceLocalBinder) service;
                LogMyTripService tlService;

                tlService = binder.getServerInstance();
                tlService.registerBluetoothReceiver ();
            }

            public void onServiceDisconnected(ComponentName className) {
            }
        };
        
        Intent intent = new Intent(ctx, LogMyTripService.class);
        ctx.bindService(intent, _connection, Context.BIND_AUTO_CREATE);
    }
    
    public static void unregisterBluetoothBroadcastReceiver (Context ctx) 
    {
        final ServiceConnection _connection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                LogMyTrackServiceLocalBinder binder = (LogMyTrackServiceLocalBinder) service;
                LogMyTripService tlService;

                tlService = binder.getServerInstance();
                tlService.unregisterBluetoothReceiver ();
            }

            public void onServiceDisconnected(ComponentName className) {
            }
        };
        
        Intent intent = new Intent(ctx, LogMyTripService.class);
        ctx.bindService(intent, _connection, Context.BIND_AUTO_CREATE);
    }
}
