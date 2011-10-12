/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import grandroid.util.PhotoAgent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import grandroid.util.ImageUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 需搭配Face，方可正常運作
 * @author Rovers
 */
public abstract class PhotoAction extends PendingAction {

    protected Uri outputFileUri;
    protected String tempFileName;

    public PhotoAction(Context context, String actionName) {
        super(context, actionName, 65011);
        tempFileName = "tmp" + System.currentTimeMillis();
    }

    public PhotoAction(Context context) {
        super(context, 65011);
        tempFileName = "tmp" + System.currentTimeMillis();
    }

    @Override
    public Intent getActionIntent() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), tempFileName);
        outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        return intent;
    }

    @Override
    public boolean callback(boolean result, Intent data) {
        try {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                /* SD卡不存在，顯示Toast訊息 */
                Toast.makeText(context, "SD卡不存在!無法儲存相片", Toast.LENGTH_LONG).show();
                return false;
            } else {
                /* 資料夾不在就先建立 */

                Bitmap bmp = null;// = (Bitmap) data.getExtras().get("data");

                try {
                    bmp = ImageUtil.loadBitmap(outputFileUri.getPath());
                    new File(outputFileUri.getPath()).delete();
                } catch (FileNotFoundException e) {
                    Log.e("grandroid", null, e);
                }
                if (bmp == null) {
                    return false;
                } else {
                    execute(context, new PhotoAgent(bmp));
                }
            }
        } catch (Exception ex) {
            Log.d("grandroid", null, ex);
        }
        return true;
    }

    public abstract void execute(Context context, PhotoAgent photoAgent);
}