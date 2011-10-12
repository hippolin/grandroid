/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import grandroid.view.Face;

/**
 *
 * @author Rovers
 */
public abstract class PendingAction extends ContextAction {

    protected int requestCode;

    public PendingAction(Context context, String actionName, int requestCode) {
        super(context, actionName);
        this.requestCode = requestCode;
    }

    public PendingAction(Context context, int requestCode) {
        super(context);
        this.requestCode = requestCode;
    }

    @Override
    public final boolean execute(Context context) {
        if (context instanceof Face) {
            ((Face) context).waitingForCallback(this);
            Intent intent = getActionIntent();
            ((Activity) context).startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    public abstract Intent getActionIntent();

    public abstract boolean callback(boolean result, Intent data);

    public final boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (data != null) {
                callback(resultCode == Activity.RESULT_OK, data);
            }
            return true;
        }
        return false;
    }
}
