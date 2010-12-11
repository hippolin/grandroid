/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rovers
 */
public abstract class ThreadAction extends ContextAction implements Runnable {

    protected long delayMSecond = 0;//毫秒
    protected Handler handler;

    public ThreadAction(Context context) {
        this(context, "");
    }

    public ThreadAction(Context context, String actionName) {
        super(context, actionName);
        new Thread(this).start();
    }

    public ThreadAction(Context context, String actionName, String message) {
        this(context, actionName, message, new Action());
    }

    public ThreadAction(Context context, String actionName, String message, final Action callback) {
        super(context, actionName);
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage(message);
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                callback.execute();
            }
        };

        progress.show();
        new Thread(this).start();
    }

    public ThreadAction(Context context, long delayMSecond) {
        super(context);
        this.delayMSecond = delayMSecond;
        new Thread(this).start();
    }

    public void run() {
        if (delayMSecond > 0) {
            try {
                Thread.sleep(delayMSecond);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.execute();
        if (handler != null) {
            handler.sendEmptyMessage(0);
        }
    }
}
