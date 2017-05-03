package net.salah.locate;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by salahamassi on 4/20/17.
 */


public class Helper {

    public static boolean isGpsEnabled (Context mContext){
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
