
package com.cachirulop.logmytrip.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.cachirulop.logmytrip.R;

public class SettingsFragment
        extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource (R.xml.preferences);
    }

    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences,
                                           String key)
    {
//        if (key.equals (SettingsManager.KEY_PREF_MAP_MODE)) {
//            Preference connectionPref = findPreference (key);
//
//            // Set summary to be the user-description for the selected value
//            connectionPref.setSummary (SettingsManager.getMapModeDesc ());
//        }

    }

    @Override
    public void onResume ()
    {
        super.onResume ();
        getPreferenceManager ().getSharedPreferences ().registerOnSharedPreferenceChangeListener (this);
    }

    @Override
    public void onPause ()
    {
        super.onPause ();
        getPreferenceScreen ().getSharedPreferences ().unregisterOnSharedPreferenceChangeListener (this);
    }
}