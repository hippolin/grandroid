/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import grandroid.DataAgent;

/**
 *
 * @author Rovers
 */
public class GoAction extends ContextAction {

    protected Bundle bundle;
    protected int flag = -1;

    public GoAction(Context context, String actionName, String cp) {
        super(context, actionName);
        Class c = null;
        try {
            c = Class.forName(cp);
        } catch (ClassNotFoundException ex) {
            Log.e(GoAction.class.getName(), null, ex);
        }
        this.args = new Object[]{c};
    }

    public GoAction(Context context, String actionName, Class c) {
        super(context, actionName);
        this.args = new Object[]{c};
    }

    public GoAction setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public GoAction addBundleObject(String key, String value) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(key, value);
        return this;
    }

    public GoAction addBundleObject(String key, int value) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(key, value);
        return this;
    }

    public GoAction addBundleObject(String key, String[] strarr) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putStringArray(key, strarr);
        return this;
    }

    public GoAction addBundleObject(String key, int[] intarr) {
        if (this.bundle == null) {
            bundle = new Bundle();
        }
        bundle.putIntArray(key, intarr);
        return this;
    }

    public GoAction setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public GoAction forgetCurrentFace() {
        return setFlag(Intent.FLAG_ACTIVITY_NO_HISTORY);
    }

    @Override
    public boolean execute(Context context) {

        if (context != null && args.length > 0 && args[0] instanceof Class) {
            Intent intent = new Intent();
            intent.setClass(context, (Class) args[0]);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (flag > 0) {
                intent.setFlags(flag);
            }
            context.startActivity(intent);
            return true;
        }

        return false;
    }
}
