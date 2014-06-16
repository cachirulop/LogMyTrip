
package com.cachirulop.logmytrip.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothBroadcastReceiver
        extends BroadcastReceiver
{

    @Override
    public void onReceive (Context context,
                           Intent intent)
    {
        String action;
        BluetoothDevice device;

        action = intent.getAction ();
        device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);

        Toast.makeText (context,
                        "Bluetooth broadcast receive: action: " + action + ", device: " + device.getAddress () + " - " + device.getName (),
                        Toast.LENGTH_SHORT).show ();

        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals (action)) {
            Toast.makeText (context, "Device disconnected", Toast.LENGTH_LONG).show ();
        }
    }

}