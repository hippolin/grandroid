/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import grandroid.view.Face;

/**
 *
 * @author Rovers
 */
public class NotifyAction extends ContextAction {

    public NotifyAction(Context context, String actionName) {
        super(context, actionName);
    }

    public NotifyAction(Context context) {
        super(context);
        args = new Object[3];
    }

    public NotifyAction setData(String title, String msg) {
        args[0] = title;
        args[1] = msg;
        return this;
    }

    @Override
    public boolean execute(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification no = new Notification(R.drawable.ic_dialog_info, (String) args[1], System.currentTimeMillis());

        CharSequence contentTitle = (String) args[0];
        CharSequence contentText = (String) args[1];
        Intent notificationIntent = new Intent(context, Face.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        no.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        manager.notify(0, no);
        return true;
    }
}
