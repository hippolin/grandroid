/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 *
 * @author Rovers
 */
public class PhoneAgent {

    public PhoneAgent() {
    }

    public void dial(Context context) {
        dial(context, null);
    }

    public void dial(Context context, String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        if (tel != null && tel.length() > 0) {
            intent.setData(Uri.parse("tel:" + tel));
        }
        context.startActivity(intent);
    }
}
