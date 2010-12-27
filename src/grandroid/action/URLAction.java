/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 *
 * @author Rovers
 */
public class URLAction extends ContextAction {

    String url;

    /**
     * 
     * @param context
     * @param actionName
     * @param url
     */
    public URLAction(Context context, String actionName, String url) {
        super(context, actionName);
        this.url = url;
    }

    /**
     * 
     * @param context
     * @param url
     */
    public URLAction(Context context, String url) {
        super(context);
        this.url = url;
    }

    /**
     * 
     * @param context
     * @return
     */
    @Override
    public boolean execute(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
        return true;
    }
}
