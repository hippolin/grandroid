/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.json;

import android.util.Log;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Rovers
 */
public class JSONConverter {

    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    public <T> T toObject(JSONObject jo, Class<T> c) {
        String processField = "";
        try {
            T obj = c.newInstance();
            Field[] farr = c.getDeclaredFields();
            for (int i = 0; i < farr.length; i++) {
                farr[i].setAccessible(true);
                processField = farr[i].getName();
                if (farr[i].getName().equals("mongoID")) {
                    farr[i].set(obj, jo.getJSONObject("_id").getString("$oid"));
                } else if (!farr[i].getName().equals("_id")) {
                    if (jo.has(farr[i].getName()) && !jo.isNull(farr[i].getName())) {
                        if (farr[i].getType().equals(Date.class)) {
                            farr[i].set(obj, sdf.parse(jo.getString(farr[i].getName())));
                        } else if (farr[i].getType().equals(String.class)) {
                            farr[i].set(obj, jo.get(farr[i].getName()).toString());
                        } else {
                            farr[i].set(obj, jo.get(farr[i].getName()));
                        }
                    }
                }
            }
            return obj;
        } catch (Exception ex) {
            Log.e("grandroid", "Field:" + processField, ex);
        }
        return null;
    }

    public <T> JSONObject fromObject(T obj) {
        try {
            Class<T> c = (Class<T>) obj.getClass();
            JSONObject jo = new JSONObject();
            Field[] farr = c.getDeclaredFields();
            for (int i = 0; i < farr.length; i++) {
                farr[i].setAccessible(true);
                if (farr[i].getName().equals("mongoID")) {
                    if (farr[i].get(obj) != null) {
                        jo.put("_id", new JSONObject().put("$oid", farr[i].get(obj)));
                    }
                } else if (!farr[i].getName().equals("_id")) {
                    if (farr[i].getType().equals(Date.class)) {
                        jo.put(farr[i].getName(), sdf.format((Date) farr[i].get(obj)));
                    } else {
                        if (farr[i].get(obj) instanceof String) {
                            if (((String) farr[i].get(obj)).startsWith("[")) {
                                jo.put(farr[i].getName(), new JSONArray((String) farr[i].get(obj)));
                            } else if (((String) farr[i].get(obj)).startsWith("{")) {
                                jo.put(farr[i].getName(), new JSONObject((String) farr[i].get(obj)));
                            } else {
                                jo.put(farr[i].getName(), farr[i].get(obj));
                            }
                        } else {
                            jo.put(farr[i].getName(), farr[i].get(obj));
                        }
                    }
                }
            }
            return jo;
        } catch (Exception ex) {
            Log.e("grandroid", null, ex);
        }
        return null;
    }

    public String formatDate(Date date) {
        return sdf.format(date);
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    }
}
