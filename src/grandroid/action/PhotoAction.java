/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.app.Activity;
import grandroid.util.PhotoAgent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import grandroid.util.ImageUtil;
import grandroid.util.LayoutUtil;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * 需搭配Face，方可正常運作
 *
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
                /*
                 * SD卡不存在，顯示Toast訊息
                 */
                Toast.makeText(context, "SD卡不存在!無法儲存相片", Toast.LENGTH_LONG).show();
                return false;
            } else {
                /*
                 * 資料夾不在就先建立
                 */

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
                    Matrix mtx = null;
                    if (new LayoutUtil((Activity) context).getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT && bmp.getWidth() > bmp.getHeight()) {
                        mtx = new Matrix();
                        mtx.postRotate(90);
                    }
                    byte[] photo = data.getExtras().getByteArray("PHOTO");
                    bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                    //Log.d("Starbucks","brand="+android.os.Build.BRAND+","+android.os.Build.DEVICE);
                    Log.d("grandroid", "android.os.Build.BRAND=" + android.os.Build.BRAND);
                    if (android.os.Build.BRAND.equals("lge")) {
                        if (mtx == null) {
                            mtx = new Matrix();
                        }
                        mtx.postScale(-1, -1);
                    }
                    if (mtx != null) {
                        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mtx, false);
                    }

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