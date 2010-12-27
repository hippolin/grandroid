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

    /**
     * 
     * @param context
     * @param helper
     */
    public FaceDataAdapter(Context context, GenericHelper<T> helper) {
        super(context, helper.select());
        this.helper = helper;
    }

    /**
     * 
     */
    public void refresh() {
        list = helper.select();
        this.notifyDataSetChanged();
    }

    /**
     * 
     * @return
     */
    public GenericHelper<T> getHelper() {
        return helper;
    }
}
