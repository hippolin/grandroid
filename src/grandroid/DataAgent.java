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
 * 記錄、載回app資料的物件，實作上採用Android的SharedPreference機制(類似Java的Properties)
 * 除了Service以外，Activity應繼承Face、呼叫getData()函數來取得實體
 * @author Rovers
 */
public class DataAgent {

    //protected static ConcurrentHashMap data;
    /**
     * 
     */
    protected ConcurrentHashMap subject;
    /**
     * 
     */
    protected SharedPreferences settings;

    /**
     * 
     * @param context
     */
    public DataAgent(Context context) {
        subject = new ConcurrentHashMap();
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 記錄下view的值 (目前只支援TextView及EditText)
     * @param obj 設定過Tag屬性的View
     */
    public void keep(View obj) {
        keep(obj, true);
    }

    /**
     * 記錄下view的值 (目前只支援TextView及EditText)
     * @param activity
     * @param viewID
     */
    public void keep(Activity activity, int viewID) {
        keep(activity, viewID, true);
    }

    /**
     * 記錄下view的值 (目前只支援TextView及EditText)
     * @param activity
     * @param viewID Resource ID
     * @param fillView 是否要載入先前的資料
     */
    public void keep(Activity activity, int viewID, boolean fillView) {
        View obj = activity.findViewById(viewID);
        if (obj != null) {
            keep(obj, fillView);
        }
    }

    /**
     * 記錄下view的值 (目前只支援TextView及EditText)
     * @param obj view物件
     * @param fillView 是否要載入先前的資料
     */
    public void keep(View obj, boolean fillView) {
        if (fillView) {
            load(obj);
        }
        subject.put(obj.getTag(), obj);
    }

    /**
     * 儲存目前Activity裡所有宣告過keep的view值
     */
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

    /**
     * 儲存view的值，需設定過tag
     * @param obj view物件
     */
    protected void save(View obj) {
        Editor editor = settings.edit();
        if (obj instanceof TextView) {
            editor.putString(obj.getTag().toString(), ((TextView) obj).getText().toString());
        } else if (obj instanceof EditText) {
            editor.putString(obj.getTag().toString(), ((EditText) obj).getText().toString()).commit();
        }
        editor.commit();
    }

    /**
     * 載入該view前次的值，需設定過tag
     * @param obj view物件
     * @return 回傳該載入的值，若沒有資料則為空字串(不是null)
     */
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

    /**
     * 將一組key-value放進SharedPreference
     * @param key
     * @param value
     * @return 回傳value本身
     */
    public Object putPreference(String key, String value) {
        settings.edit().putString(key, value).commit();
        return value;
    }

    /**
     * 從SharedPreference裡取出對應至該key的值
     * @param key
     * @return 對應至該key的值，若不存在則回傳空字串(非null)
     */
    public String getPreference(String key) {
        return settings.getString(key, "");
    }

    /**
     * 從SharedPreference裡取出對應至該key的值
     * @param key
     * @param defaultValue 預設值
     * @return 對應至該key的值，若不存在則回傳defaultValue參數 (發現不存在後，並不會存進SharedPreference)
     */
    public String getPreference(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }
}
