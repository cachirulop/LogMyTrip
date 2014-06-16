
package com.cachirulop.logmytrip.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cachirulop.logmytrip.R;

public class SettingsManager
{
    public static final String KEY_PREF_AUTO_SAVE_POSITION    = "pref_autoSavePosition";
    public static final String KEY_PREF_BLUETOOTH_DEVICE_LIST = "pref_bluetoothDeviceList";
    public static final String KEY_PREF_MAP_FILES_PATH        = "pref_mapFilesPath";
    public static final String KEY_PREF_MAP_MODE              = "pref_mapMode";


    private static SharedPreferences getSharedPrefs (Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences (ctx);
    }
}
