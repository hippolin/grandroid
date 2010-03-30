/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid;

import android.content.Intent;
import grandroid.action.CMD;

/**
 *
 * @author Rovers
 */
public class PhoneAgent {

    protected static PhoneAgent instance;

    protected PhoneAgent() {
    }

    public static PhoneAgent getInstance() {
        if (instance == null) {
            instance = new PhoneAgent();
        }
        return instance;
    }

    public void dial(){
        Intent intent = new Intent("android.intent.action.CALL_BUTTON");
        CMD.CURR_FRAME.startActivity(intent);
    }
}
