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

    /**
     * 
     */
    protected String tel;

    /**
     * 
     * @param context
     */
    public DialAction(Context context) {
        super(context);
    }

    /**
     * 
     * @param context
     * @param actionName
     */
    public DialAction(Context context, String actionName) {
        super(context, actionName);
    }

    /**
     * 
     * @param context
     * @param actionName
     * @param tel
     */
    public DialAction(Context context, String actionName, String tel) {
        super(context, actionName);
        this.tel = tel;
    }

    /**
     * 
     * @param context
     * @return
     */
    @Override
    public boolean execute(Context context) {
        new PhoneAgent().dial(context, tel);
        return true;
    }
}
