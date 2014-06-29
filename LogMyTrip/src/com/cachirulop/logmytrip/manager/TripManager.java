
package com.cachirulop.logmytrip.manager;

import java.text.DateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cachirulop.logmytrip.R;
import com.cachirulop.logmytrip.data.LogMyTripDataHelper;
import com.cachirulop.logmytrip.entity.Trip;

public class TripManager
{
    private static final String CONST_TRIP_TABLE_NAME     = "trip";
    private static final String CONST_LOCATION_TABLE_NAME = "trip_location";

    public static Trip getCurrentTrip (Context ctx)
    {
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = new LogMyTripDataHelper (ctx).getReadableDatabase ();

            c = db.rawQuery (ctx.getString (R.string.SQL_get_last_active_trip),
                             null);

            if (c != null && c.moveToFirst ()) {
                return createTrip (c);
            }
            else {
                Trip result;

                result = new Trip ();
                result.setTripDate (new Date ());
                result.setDescription (getDefaultDescription (ctx,
                                                              result.getTripDate ()));
                result.setFinishDate (null);

                return saveTrip (ctx,
                                 result,
                                 true);
            }
        }
        finally {
            if (c != null) {
                c.close ();
            }

            if (db != null) {
                db.close ();
            }
        }
    }

    public static Trip saveTrip (Context ctx,
                                 Trip t,
                                 boolean isInsert)
    {
        SQLiteDatabase db = null;

        try {
            db = new LogMyTripDataHelper (ctx).getWritableDatabase ();

            ContentValues values;

            values = new ContentValues ();

            values.put ("trip_date",
                        t.getTripDate ().getTime ());
            values.put ("description",
                        t.getDescription ());
            values.put ("finish_date",
                        t.getFinishDate ().getTime ());

            if (isInsert) {
                db.insert (CONST_TRIP_TABLE_NAME,
                           null,
                           values);
            }
            else {
                db.update (CONST_TRIP_TABLE_NAME,
                           values,
                           "id = ?",
                           new String[] { Long.toString (t.getId ()) });
            }

            t.setId (getLastIdTrip (ctx));

            return t;
        }
        finally {
            if (db != null) {
                db.close ();
            }
        }
    }

    public static void finishTrip (Context ctx,
                                   Trip t)
    {
        t.setFinishDate (new Date ());
        saveTrip (ctx,
                  t,
                  false);
    }

    private static Trip createTrip (Cursor c)
    {
        return null;
    }

    private static String getDefaultDescription (Context ctx,
                                                 Date d)
    {
        DateFormat timeFormatter;
        DateFormat dateFormatter;

        timeFormatter = android.text.format.DateFormat.getTimeFormat (ctx);
        dateFormatter = android.text.format.DateFormat.getMediumDateFormat (ctx);

        return String.format ("[%s - %s]",
                              dateFormatter.format (d),
                              timeFormatter.format (d));
    }

    /**
     * Gets the maximum identifier of the trips table
     * 
     * @return
     */
    private static long getLastIdTrip (Context ctx)
    {
        return new LogMyTripDataHelper (ctx).getLastId ("trip");
    }

}
