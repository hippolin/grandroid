/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 *
 * @author Rovers
 */
public class GoAction extends ContextAction {

    /**
     * 
     */
    protected Bundle bundle;
    /**
     * 
     */
    protected int flag = -1;
    protected boolean isSubTask;
    protected Class c;
    protected int requestCode = 0;

    /**
     * 
     * @param context
     * @param c target activity
     */
    public GoAction(Context context, Class c) {
        super(context, "undefined");
        this.c = c;
    }

    /**
     * 
     * @param context
     * @param actionName
     * @param cp
     */
    public GoAction(Context context, String actionName, String cp) {
        super(context, actionName);
        try {
            c = Class.forName(cp);
        } catch (ClassNotFoundException ex) {
            Log.e(GoAction.class.getName(), null, ex);
        }
    }

    /**
     * 
     * @param context
     * @param actionName
     * @param c
     */
    public GoAction(Context context, String actionName, Class c) {
        super(context, actionName);
        this.c = c;
    }

    /**
     * 
     * @param bundle
     * @return
     */
    public GoAction setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public GoAction addBundleObject(String key, String value) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(key, value);
        return this;
    }

    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public GoAction addBundleObject(String key, int value) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(key, value);
        return this;
    }

    /**
     * 
     * @param key
     * @param strarr
     * @return
     */
    public GoAction addBundleObject(String key, String[] strarr) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putStringArray(key, strarr);
        return this;
    }

    /**
     * 
     * @param key
     * @param intarr
     * @return
     */
    public GoAction addBundleObject(String key, int[] intarr) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putIntArray(key, intarr);
        return this;
    }

    /**
     * 
     * @param flag
     * @return
     */
    public GoAction setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    /**
     * 
     * @return
     */
    public GoAction forgetCurrentFace() {
        return setFlag(Intent.FLAG_ACTIVITY_NO_HISTORY);
    }

    public GoAction setSubTask() {
        return setSubTask(0);
    }

    public GoAction setSubTask(int requestCode) {
        isSubTask = true;
        this.requestCode = requestCode;
        return this;
    }

    /**
     * 
     * @param context
     * @return
     */
    @Override
    public boolean execute(Context context) {
        if (context != null && c != null) {
            Intent intent = new Intent();
            intent.setClass(context, c);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (flag > 0) {
                intent.setFlags(flag);
            }
            if (isSubTask && context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            } else {
                context.startActivity(intent);
            }
            return true;
        }
        return false;
    }
}
