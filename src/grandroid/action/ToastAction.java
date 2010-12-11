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

    public ToastAction(Context context, String actionName) {
        super(context, actionName);
        args = new Object[1];
    }

    public ToastAction(Context context) {
        super(context);
        args = new Object[1];
    }

    public ToastAction setMessage(String msg) {
        args[0] = msg;
        return this;
    }

    @Override
    public boolean execute(Context context) {
        Toast.makeText(context, (String) args[0], Toast.LENGTH_LONG).show();
        return true;
    }
}