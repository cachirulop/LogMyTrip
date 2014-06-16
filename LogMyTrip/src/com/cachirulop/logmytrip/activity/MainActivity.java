
package com.cachirulop.logmytrip.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import com.cachirulop.logmytrip.R;
import com.cachirulop.logmytrip.fragment.MainFragment;
import com.cachirulop.logmytrip.service.BackgroundLocationService;

public class MainActivity
        extends Activity
{
    private final static int ACTIVITY_RESULT_SETTINGS = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager ().beginTransaction ().add (R.id.container,
                                                           new MainFragment ()).commit ();
        }
    }

    @Override
    protected void onStart ()
    {
        super.onStart ();
    }

    @Override
    protected void onStop ()
    {
        super.onStop ();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.main,
                                    menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId ()) {
            case R.id.action_settings:
                showPreferences ();
                return true;

            default:
                return super.onOptionsItemSelected (item);
        }
    }

    private void showPreferences ()
    {
        startActivityForResult (new Intent (this,
                                            SettingsActivity.class),
                                ACTIVITY_RESULT_SETTINGS);
    }

    public void onActivateServiceClick (View v)
    {
        // Is the toggle on?
//        boolean on = ((ToggleButton) v).isChecked ();
//
//        if (on) {
//            startService (new Intent (this,
//                                      BackgroundLocationService.class));
//        }
//        else {
//            stopService (new Intent (this,
//                                     BackgroundLocationService.class));
//        }
    }
}
