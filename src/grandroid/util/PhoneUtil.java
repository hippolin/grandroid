/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

/**
 *
 * @author Rovers
 */
public class PhoneUtil {

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable();
    }

    //需要權限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    public static String getDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
}
