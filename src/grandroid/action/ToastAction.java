/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.content.Context;
import android.widget.Toast;

/**
 *
 * @author Rovers
 */
public class ToastAction extends ContextAction {

    /**
     * 
     * @param context
     * @param actionName
     */
    public ToastAction(Context context, String actionName) {
        super(context, actionName);
        args = new Object[1];
    }

    /**
     * 
     * @param context
     */
    public ToastAction(Context context) {
        super(context);
        args = new Object[1];
    }

    /**
     * 
     * @param msg
     * @return
     */
    public ToastAction setMessage(String msg) {
        args[0] = msg;
        return this;
    }

    /**
     * 
     * @param context
     * @return
     */
    @Override
    public boolean execute(Context context) {
        Toast.makeText(context, (String) args[0], Toast.LENGTH_LONG).show();
        return true;
    }
}
