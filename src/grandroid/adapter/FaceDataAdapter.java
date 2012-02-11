/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import grandroid.database.GenericHelper;
import grandroid.database.Identifiable;

/**
 *
 * @param <T> 
 * @author Rovers
 */
public abstract class FaceDataAdapter<T extends Identifiable> extends ObjectAdapter<T> {

    /**
     * 
     */
    protected GenericHelper<T> helper;
    protected String where;
    protected boolean available = false;

    /**
     * 
     * @param context
     * @param helper
     */
    public FaceDataAdapter(Context context, GenericHelper<T> helper) {
        super(context, helper.select());
        this.helper = helper;
        available = true;
    }

    public FaceDataAdapter(Context context, GenericHelper<T> helper, String where, boolean ignoreFirstRefresh) {
        super(context, helper.select(where));
        this.where = where;
        this.helper = helper;
        available=!ignoreFirstRefresh;
    }

    /**
     * 
     */
    public void refresh() {
        if (available) {
            if (where != null && where.length() > 0) {
                list = helper.select(where);
            } else {
                list = helper.select();
            }
            this.notifyDataSetInvalidated();
        }else{
            available=true;
        }
    }

    public void requery(String where) {
        this.where = where;
        refresh();
    }

    /**
     * 
     * @return
     */
    public GenericHelper<T> getHelper() {
        return helper;
    }
}
