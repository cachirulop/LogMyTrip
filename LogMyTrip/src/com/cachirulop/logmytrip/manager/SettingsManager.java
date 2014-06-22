
package com.cachirulop.logmytrip.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsManager
{
    public static final String KEY_PREF_AUTO_START_LOG        = "pref_autoStartLog";
    public static final String KEY_PREF_BLUETOOTH_DEVICE_LIST = "pref_bluetoothDeviceList";

    private static SharedPreferences getSharedPrefs (Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences (ctx);
    }

    public static boolean getAutoStartLog (Context ctx)
    {
        return getSharedPrefs(ctx).getBoolean (KEY_PREF_AUTO_START_LOG, false);
    }
}
