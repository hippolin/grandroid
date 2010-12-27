/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 協助執行電話相關功能的類別
 * @author Rovers
 */
public class PhoneAgent {

    /**
     * 
     */
    public PhoneAgent() {
    }

    /**
     * 跳出撥號介面
     * @param context
     */
    public void dial(Context context) {
        dial(context, null);
    }

    /**
     * 跳出撥號介面，並撥號給tel這個號碼
     * @param context
     * @param tel 撥號的號碼
     */
    public void dial(Context context, String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        if (tel != null && tel.length() > 0) {
            intent.setData(Uri.parse("tel:" + tel));
        }
        context.startActivity(intent);
    }
}
