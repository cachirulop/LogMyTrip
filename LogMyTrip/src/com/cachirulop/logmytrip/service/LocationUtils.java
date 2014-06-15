package com.cachirulop.logmytrip.service;

import android.content.Context;
import android.location.Location;

/**
 * Defines app-wide constants and utilities
 */
public final class LocationUtils
{
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 5;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    public static final int DETECTION_INTERVAL_SECONDS = 30;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    // Create an empty string for initializing strings
    public static final String EMPTY_STRING = new String();


    // Shared Preferences repository name
    public static final String SHARED_PREFERENCES =
            "com.fasteque.life.SHARED_PREFERENCES";

    // Key in the repository for the previous activity
    public static final String KEY_PREVIOUS_ACTIVITY_TYPE =
            "com.fasteque.life.activityrecognition.KEY_PREVIOUS_ACTIVITY_TYPE";


    /**
     * Get the latitude and longitude from the Location object returned by
     * Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return The latitude and longitude of the current location, or null if no
     * location is available.
     */
//    public static String getLatLng(Context context, Location currentLocation) {
//        // If the location is valid
//        if (currentLocation != null) {
//
//            // Return the latitude and longitude as strings
//            return context.getString(
//                    R.string.latitude_longitude,
//                    currentLocation.getLatitude(),
//                    currentLocation.getLongitude());
//        } else {
//
//            // Otherwise, return the empty string
//            return EMPTY_STRING;
//        }
//    }
}