
package com.cachirulop.logmytrip.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cachirulop.logmytrip.R;
import com.cachirulop.logmytrip.service.BackgroundLocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class MainActivity
        extends Activity
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener
{
	LocationClient _locClient;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager ().beginTransaction ().add (R.id.container,
                                                           new PlaceholderFragment ()).commit ();
        }
        
        _locClient = new LocationClient(this, this, this);
    }

    @Override
    protected void onStart ()
    {
        super.onStart ();
        
        _locClient.connect ();
    }

    @Override
    protected void onStop ()
    {
        _locClient.disconnect ();
        
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment
            extends Fragment
    {

        public PlaceholderFragment ()
        {}

        @Override
        public View onCreateView (LayoutInflater inflater,
                                  ViewGroup container,
                                  Bundle savedInstanceState)
        {
            View rootView = inflater.inflate (R.layout.fragment_main,
                                              container,
                                              false);
            return rootView;
        }
    }
    
    public void onActivateServiceClick (View v) 
    {
        // Is the toggle on?
        boolean on = ((ToggleButton) v).isChecked();
        
        if (on) {
        	startService(new Intent(this, BackgroundLocationService.class));
        } else {
        	stopService(new Intent(this, BackgroundLocationService.class));
        }
    }
    
    public void onActivateServiceClick2 (View v) 
    {
        // Is the toggle on?
        boolean on = ((ToggleButton) v).isChecked();
        
        if (on) {
        	Location current;
        	
        	current = _locClient.getLastLocation ();
        	
        	if (current != null) {
        	    Toast.makeText(this, "Current location: " + current.getLatitude () + "-.-" + current.getLongitude (), Toast.LENGTH_SHORT).show();
        	}
        	else {
                Toast.makeText(this, "Current location is null", Toast.LENGTH_SHORT).show();
        	}
        	          
        }
    }
    
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Toast.makeText(this, "ERROR.",
                           Toast.LENGTH_SHORT).show();
        }
    }


}
