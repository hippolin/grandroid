/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import java.util.List;

/**
 *
 * @param <T> 
 * @author Rovers
 */
public abstract class ObjectAdapter<T> extends BaseAdapter implements ItemClickable<T> {

    /**
     * 
     */
    protected Context context;
    /**
     * 
     */
    protected List<T> list;

    /**
     * 
     * @param context
     * @param list
     */
    public ObjectAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 
     * @return
     */
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    /**
     * 
     * @param index
     * @return
     */
    public T getItem(int index) {
        return list.get(index);
    }

    /**
     * 
     * @param index
     * @return
     */
    public long getItemId(int index) {
        return index;
    }

    /**
     * 
     * @param index
     * @param view
     * @param parent
     * @return
     */
    public View getView(int index, View view, ViewGroup parent) {
        if (view == null) {
            view = createRowView(index, list.get(index));
        }
        fillRowView(index, view, list.get(index));
        return view;
    }

    /**
     * 
     * @param index
     * @param item
     * @return
     */
    public abstract View createRowView(int index, T item);

    /**
     * 
     * @param index
     * @param cellRenderer
     * @param item
     */
    public abstract void fillRowView(int index, View cellRenderer, T item);
    
    /**
     * 
     * @param index
     * @param view
     * @param item
     */
    public void onClickItem(int index, View view, T item) {
        //Toast.makeText(context, "not override method 'onClickItem' at JSONAdapter instance yet!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 
     * @param index
     * @param view
     * @param item
     */
    public void onLongPressItem(int index, View view, T item) {
        //Toast.makeText(context, "not override method 'onLongPressItem' at JSONAdapter instance yet!", Toast.LENGTH_SHORT).show();
    }
}
