
package com.cachirulop.logmytrip.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class LogMyTrackService
        extends Service
{
    @Override
    public IBinder onBind (Intent intent)
    {
        return null;
    }

    private final class MainServiceHandler
            extends Handler
    {
        // ConnectivityBroadcastReceiver _connectivityReceiver;

        public MainServiceHandler (Looper l)
        {
            super (l);
        }

        @Override
        public void handleMessage (Message msg)
        {
            // _connectivityReceiver = new ConnectivityBroadcastReceiver ();
            // registerReceiver (_connectivityReceiver,
            //                   new IntentFilter (ConnectivityManager.CONNECTIVITY_ACTION));
        }

    }

    private Looper             _serviceLooper;
    private MainServiceHandler _serviceHandler;

    @Override
    public void onCreate ()
    {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread ("ServiceStartArguments",
                                                  Process.THREAD_PRIORITY_BACKGROUND);
        thread.start ();

        // Get the HandlerThread's Looper and use it for our Handler
        _serviceLooper = thread.getLooper ();
        _serviceHandler = new MainServiceHandler (_serviceLooper);
    }

    @Override
    public int onStartCommand (Intent intent,
                               int flags,
                               int startId)
    {
        Toast.makeText (this,
                        "service starting",
                        Toast.LENGTH_SHORT).show ();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the
        // job
        Message msg = _serviceHandler.obtainMessage ();
        msg.arg1 = startId;
        _serviceHandler.sendMessage (msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy ()
    {
        Toast toast;

        toast = Toast.makeText (this,
                                "Terminating service",
                                Toast.LENGTH_LONG);
        toast.show ();
    }

}
