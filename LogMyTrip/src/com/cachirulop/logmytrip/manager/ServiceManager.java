
package com.cachirulop.logmytrip.manager;

import android.content.Context;
import android.content.Intent;

import com.cachirulop.logmytrip.service.BluetoothReceiverService;

public class ServiceManager
{
    public static void startBluetoothService (Context ctx)
    {
        ctx.startService (new Intent (ctx,
                                      BluetoothReceiverService.class));
    }
    
    public static void stopBluetoothService (Context ctx) 
    {
        ctx.stopService (new Intent (ctx,
                                     BluetoothReceiverService.class));
    }
    
/*
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
*/    
}
