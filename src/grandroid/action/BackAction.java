/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;

/**
 *
 * @author Rovers
 */
public class BackAction extends ContextAction {

    public BackAction(Context context) {
        super(context);
    }

    @Override
    public boolean execute(Context context) {
//        new Thread(new Runnable()    {
//
//            @Override
//            public void run() {
        KeyEvent backEvtDown = new KeyEvent(KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_BACK);
        KeyEvent backEvtUp = new KeyEvent(KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_BACK);
        ((Activity) context).dispatchKeyEvent(backEvtDown);
        ((Activity) context).dispatchKeyEvent(backEvtUp);
//            }
//        }).start();
        return true;
    }
}
