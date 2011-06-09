/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import grandroid.action.Action;
import java.util.List;

/**
 *
 * @author Rovers
 */
public class GPSUtil {

    protected static double lon;
    protected static double lat;

    /**
     * 取得最後的位置
     * @param context
     * @return [0]:Latitude [1]:Longitude，若完全無資料則回傳null
     */
    public static double[] getLastPosition(Context context) {
        if (lon > 0 && lat > 0) {
            return new double[]{lat, lon};
        }
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break                 out the loop*/
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            Location loc = lm.getLastKnownLocation(providers.get(i));
            if (loc != null) {
                if (l == null) {
                    l = loc;
                } else {
                    if (l.getTime() < loc.getTime()) {
                        l = loc;
                    }
                }
            }
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
            return gps;
        } else {
            return null;
        }
    }

    public static void locate(Context context, final Action action, final boolean repeat) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(locationManager.getBestProvider(locationCriteria, true), 0, 0, new LocationListener()                                                      {

            public void onLocationChanged(Location location) {
                if (!repeat) {
                    locationManager.removeUpdates(this);
                }
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("grandroid", "change to location (" + lat + "," + lon + ")");
                if (action != null) {
                    action.setArgs(location.getLatitude(), location.getLongitude()).execute();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        });
    }
}
