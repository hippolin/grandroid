/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.dialog;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import grandroid.util.LayoutMaker;
import grandroid.util.LayoutUtil;

/**
 *
 * @author Rovers
 */
public abstract class DialogMask {

    protected GDialog dialog;
    protected Context context;
    protected GDialog.Builder builder;
    
    public DialogMask(Context context) {
        this.context = context;
        builder = new GDialog.Builder(context);
    }

    public abstract boolean setupMask(Context context, GDialog.Builder builder, LayoutMaker maker) throws Exception;

    public GDialog getDialog(){
        return dialog;
    }
    
    public boolean isShowing(){
        return dialog.isShowing();
    }
    
    public void show() {
        try {
            LayoutMaker maker = new LayoutMaker(context, false);
            builder.beforeDialogContent(maker);
            setupMask(context, builder, maker);
            dialog = builder.create(maker);
            dialog.show();
        } catch (Exception ex) {
            Log.e(DialogMask.class.getName(), null, ex);
        }
    }
    
    protected LinearLayout.LayoutParams getMaxSizeLayoutParams(){
        LayoutUtil lu = new LayoutUtil((Activity) context);
        return new LinearLayout.LayoutParams(lu.getWidth() - 60, (int) (0.8 * lu.getHeight()));
    }
}