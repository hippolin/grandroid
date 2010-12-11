/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.content.Context;
import grandroid.PhoneAgent;

/**
 *
 * @author Rovers
 */
public class DialAction extends ContextAction {

    protected String tel;

    public DialAction(Context context) {
        super(context);
    }

    public DialAction(Context context, String actionName) {
        super(context, actionName);
    }

    public DialAction(Context context, String actionName, String tel) {
        super(context, actionName);
        this.tel = tel;
    }

    @Override
    public boolean execute(Context context) {
        new PhoneAgent().dial(context, tel);
        return true;
    }
}
