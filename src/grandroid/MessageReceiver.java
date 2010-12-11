/*
 * AndroidManifest.xml必須修改
 * <receiver android:name="grandroid.phone.MessageReceiver">
 * <intent-filter>
 * <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
 * </intent-filter>
 * </receiver>
 * <uses-permission android:name="android.permission.RECEIVE_SMS" />
 *
 */
package grandroid;

import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import grandroid.action.Action;
import grandroid.phone.SMSHelper;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Rovers
 */
public class MessageReceiver extends BroadcastReceiver {

    protected String actionClass;
    protected ConcurrentHashMap<String, Action> actionMap;

    public MessageReceiver() {
        actionMap = new ConcurrentHashMap<String, Action>();
    }

    public void registerAllEvent(Context context) {
        for (String key : actionMap.keySet()) {
            context.registerReceiver(this, new IntentFilter(key));
        }
    }

    public void addEvent(String event, Action action) {
        actionMap.put(event, action);
    }

    public boolean containsEvent(String event) {
        return actionMap.containsKey(event);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (actionMap.containsKey(intent.getAction())) {
            Action action = actionMap.get(intent.getAction());
            if (intent.getAction().equals(SMSHelper.SMS_REC)) {
                String message = new SMSHelper().retrieveSMS(context, intent);
                action.setArgs(message).execute();
            } else {
                action.execute();
            }
        }

//        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
//            Logger.getLogger(MessageReceiver.class.getName()).log(Level.INFO, "******************catched ACTION_USER_PRESENT action!!**************");
//            Toast.makeText(context, "catched ACTION_USER_PRESENT action!!", Toast.LENGTH_LONG).show();
//        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//            Logger.getLogger(MessageReceiver.class.getName()).log(Level.INFO, "******************catched ACTION_SCREEN_ON action!!**************");
//            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//            km.newKeyguardLock("DingSchool").disableKeyguard();
//
//        }
    }
}
