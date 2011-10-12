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
import android.os.Bundle;

/**
 *
 * @author Rovers
 */
public class NotifyAction extends ContextAction {

    protected String title;
    protected String msg;
    protected int icon;
    protected int group;
    protected Class target;
    protected Bundle extra;
    protected int flag;
    protected boolean autoCancel;
    protected boolean virbation;

    /**
     * 
     * @param context
     * @param actionName
     */
    public NotifyAction(Context context, String actionName) {
        super(context, actionName);
        title = "未設定";
        icon = R.drawable.ic_dialog_info;
        group = 0;
        target = null;
        flag = PendingIntent.FLAG_UPDATE_CURRENT;
        autoCancel = true;
    }

    /**
     * 
     * @param context
     */
    public NotifyAction(Context context) {
        this(context, "");
    }

    /**
     * 
     * @param title
     * @param msg
     * @return
     */
    public NotifyAction setContent(String title, String msg) {
        this.title = title;
        this.msg = msg;
        return this;
    }

    public NotifyAction setGroup(int group) {
        this.group = group;
        return this;
    }

    public NotifyAction setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public NotifyAction setTarget(Class target, Bundle extra) {
        this.target = target;
        this.extra = extra;
        return this;
    }

    public NotifyAction setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public NotifyAction setAutoCancel(boolean autoCancel) {
        this.autoCancel = autoCancel;
        return this;
    }

    public boolean isVirbation() {
        return virbation;
    }

    public NotifyAction setVirbation(boolean virbation) {
        this.virbation = virbation;
        return this;
    }

    /**
     * 
     * @param context
     * @return
     */
    @Override
    public boolean execute(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification no = new Notification(icon, msg, System.currentTimeMillis());
        if (autoCancel) {
            no.flags = Notification.FLAG_AUTO_CANCEL;;
        }
        CharSequence contentTitle = title;
        CharSequence contentText = msg;
        Intent notificationIntent = target != null ? new Intent(context, target) : null;
        if (notificationIntent != null) {
            notificationIntent.putExtras(extra);
            //notificationIntent.setFlags(flag);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, flag);

        no.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        if (virbation) {
            no.defaults |= Notification.DEFAULT_VIBRATE;
            long[] vibrate = {0, 100, 200, 300};
            no.vibrate = vibrate;
        }
        manager.notify(group, no);
        return true;
    }
}
