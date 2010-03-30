/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import grandroid.action.CMD;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Rovers
 */
public class DataAgent {

    protected static DataAgent instance;
    protected ConcurrentHashMap data;
    protected ConcurrentHashMap subject;

    protected DataAgent() {
        data = new ConcurrentHashMap();
        subject = new ConcurrentHashMap();
    }

    public static DataAgent getInstance() {
        if (instance == null) {
            instance = new DataAgent();
        }
        return instance;
    }

    public void keep(View obj) {
        keep(obj, true);
    }

    public void keep(int viewID) {
        keep(viewID, true);
    }

    public void keep(int viewID, boolean fillView) {
        View obj = CMD.CURR_FRAME.findViewById(viewID);
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
        for (Object obj : subject.values()) {
            if (obj instanceof View) {
                save((View) obj);
            }
        }
    }

    public void save(View obj) {
        if (obj instanceof TextView) {
            data.put(obj.getTag(), ((TextView) obj).getText());
        } else if (obj instanceof EditText) {
            data.put(obj.getTag(), ((EditText) obj).getText());
        }

    }

    public Object load(View obj) {
        Object value = data.get(obj.getTag());
        if (value != null) {
            if (obj instanceof TextView) {
                ((TextView) obj).setText(value.toString());
            } else if (obj instanceof EditText) {
                ((EditText) obj).setText(value.toString());
            }
        }
        return value;
    }

    public Object putData(String key, Object obj) {
        data.put(key, obj);
        return obj;
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
