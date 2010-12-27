/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 *
 * @author Rovers
 */
public class AlertAction extends ContextAction {

    /**
     * 
     * @param context
     * @param actionName
     */
    public AlertAction(Context context, String actionName) {
        super(context, actionName);
    }

    /**
     * 
     * @param context
     */
    public AlertAction(Context context) {
        super(context);
    }

    /**
     * 
     */
    protected void init() {
        args = new Object[4];
        args[0] = "確認";
        args[1] = "測試訊息";
        args[2] = new Action("確定");
        args[3] = new Action("取消");
    }

    /**
     * 
     * @param title
     * @param msg
     * @param actPositive
     * @param actNegative
     * @return
     */
    public AlertAction setData(String title, String msg, Action actPositive, Action actNegative) {
        if (title != null) {
            args[0] = title;
        }
        if (msg != null) {
            args[1] = msg;
        }
        if (actPositive != null) {
            args[2] = actPositive;
        }
        if (actNegative != null) {
            args[3] = actNegative;
        }
        return this;
    }

    /**
     * 
     * @param context
     * @return
     */
    @Override
    public boolean execute(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle((String) args[0]).setMessage((String) args[1]);
        if ((Action) args[2] != null) {
            builder.setPositiveButton(((Action) args[2]).getActionName(), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    ((Action) args[2]).execute();
                }
            });
        }
        if ((Action) args[2] != null) {
            builder.setNegativeButton(((Action) args[3]).getActionName(), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    ((Action) args[3]).execute();
                }
            });
        }
        builder.show();
        return true;
    }
}
