/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Rovers
 */
public class DataAgent {

    //protected static ConcurrentHashMap data;
    protected ConcurrentHashMap subject;
    protected SharedPreferences settings;

    public DataAgent(Context context) {
        subject = new ConcurrentHashMap();
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void keep(View obj) {
        keep(obj, true);
    }

    public void keep(Activity activity, int viewID) {
        keep(activity, viewID, true);
    }

    public void keep(Activity activity, int viewID, boolean fillView) {
        View obj = activity.findViewById(viewID);
        if (obj != null) {
            keep(obj, fillView);
        }
    }

    public void keep(View obj, boolean fillView) {
        if (fillView) {
            load(obj);
        }
        subject.put(obj.getTag(), obj);
    }

    public void digest() {
        if (subject.size() > 0) {
            for (Object obj : subject.values()) {
                if (obj instanceof View) {
                    save((View) obj);
                }
            }
            settings.edit().commit();
        }
    }

    protected void save(View obj) {
        Editor editor = settings.edit();
        if (obj instanceof TextView) {
            editor.putString(obj.getTag().toString(), ((TextView) obj).getText().toString());
        } else if (obj instanceof EditText) {
            editor.putString(obj.getTag().toString(), ((EditText) obj).getText().toString()).commit();
        }
        editor.commit();
    }

    protected Object load(View obj) {
        String value = settings.getString(obj.getTag().toString(), "");
        if (value != null) {
            if (obj instanceof TextView) {
                ((TextView) obj).setText(value);
            } else if (obj instanceof EditText) {
                ((EditText) obj).setText(value);
            }
        }
        return value;
    }

    public Object putPreference(String key, String value) {
        settings.edit().putString(key, value).commit();
        return value;
    }

    public String getPreference(String key) {
        return settings.getString(key, "");
    }

    public String getPreference(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }
}
