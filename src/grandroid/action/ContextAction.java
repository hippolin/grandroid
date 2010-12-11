/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.content.Context;

/**
 *
 * @author Rovers
 */
public abstract class ContextAction extends Action {

    protected Context context;

    public ContextAction(Context context) {
        this.context = context;
    }

    public ContextAction(Context context, String actionName) {
        super(actionName);
        this.context = context;
    }

    @Override
    public final boolean execute() {
        return this.execute(context);
    }

    public abstract boolean execute(Context context);
}
