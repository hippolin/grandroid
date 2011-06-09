/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Rovers
 */
public class JSONAdapter extends BaseAdapter implements ItemClickable<JSONObject> {

    /**
     * 
     */
    protected Context context;
    /**
     * 
     */
    protected JSONArray array;

    /**
     * 
     * @param context
     * @param array
     */
    public JSONAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
        this.notifyDataSetChanged();
    }

    /**
     * 
     * @return
     */
    public int getCount() {
        if (array != null) {
            return array.length();
        } else {
            return 0;
        }
    }

    /**
     * 
     * @param index
     * @return
     */
    public Object getItem(int index) {
        if (array != null) {
            try {
                return array.getJSONObject(index);
            } catch (JSONException ex) {
                Log.e("facepaper-friendadapter", ex.getMessage());
            }
        }
        return null;
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
     * @param cellRenderer
     * @param parent
     * @return
     */
    public View getView(int index, View cellRenderer, ViewGroup parent) {
        try {
            JSONObject obj = array.getJSONObject(index);
            if (cellRenderer == null) {
                // create the cell renderer
                cellRenderer = createRowView(index, obj);
            }
            fillRowView(index, cellRenderer, obj);
        } catch (JSONException ex) {
            Log.e("facepaper-friendadapter", ex.getMessage());
        }

        return cellRenderer;
    }

    /**
     * 
     * @param index
     * @param item
     * @return
     */
    public View createRowView(int index, JSONObject item) {
        TextView cellRendererView = new TextView(context);

        cellRendererView.setTextColor(Color.DKGRAY);
        cellRendererView.setTextSize(18);
        cellRendererView.setGravity(Gravity.CENTER);
        cellRendererView.setPadding(10, 2, 10, 2);
        return cellRendererView;
    }

    /**
     * 
     * @param index
     * @param cellRenderer
     * @param item
     * @throws JSONException
     */
    public void fillRowView(int index, View cellRenderer, JSONObject item) throws JSONException {
        ((TextView) cellRenderer).setText(item.getString("name"));
    }

    /**
     * 
     * @param index
     * @param view
     * @param item
     */
    public void onClickItem(int index, View view, JSONObject item) {
        //Toast.makeText(context, "not override method 'onClickItem' at JSONAdapter instance yet!", Toast.LENGTH_SHORT).show();
    }

    public void onLongPressItem(int index, View view, JSONObject item) {
        //Toast.makeText(context, "not override method 'onLongPressItem' at JSONAdapter instance yet!", Toast.LENGTH_SHORT).show();
    }
}
