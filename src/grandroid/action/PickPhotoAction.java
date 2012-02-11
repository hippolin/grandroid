/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Rovers
 */
public abstract class PickPhotoAction extends PendingAction {

    protected int bitmapWidth;
    protected int bitmapHeight;

    public PickPhotoAction(Context context) {
        super(context, 65012);
    }

    public PickPhotoAction(Context context, int bitmapWidth, int bitmapHeight) {
        super(context, 65012);
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
    }

    public PickPhotoAction(Context context, String actionName) {
        super(context, actionName, 65012);
    }

    public PickPhotoAction(Context context, String actionName, int bitmapWidth, int bitmapHeight) {
        super(context, actionName, 65012);
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
    }

    @Override
    public Intent getActionIntent() {
        if (bitmapWidth > 0 && bitmapHeight > 0) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", bitmapWidth);
            intent.putExtra("outputY", bitmapHeight);
            intent.putExtra("return-data", true);

            return intent;
        } else {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            return i;
        }
    }

    @Override
    public boolean callback(boolean result, Intent data) {
        if (result) {
            try {
                Bitmap photo = null;
                if (bitmapWidth > 0 && bitmapHeight > 0) {
                    photo = data.getParcelableExtra("data");
                } else {
                    InputStream imageStream = null;
                    try {
                        Uri selectedImage = data.getData();
                        imageStream = context.getContentResolver().openInputStream(selectedImage);
                        photo = BitmapFactory.decodeStream(imageStream);
                        Log.d("grandroid", "android.os.Build.BRAND=" + android.os.Build.BRAND + ", android.os.Build.DEVICE=" + android.os.Build.DEVICE);
                        if (android.os.Build.DEVICE.equals("GT-I9000")) {
                            Matrix mtx = new Matrix();
                            mtx.postRotate(90);
                            photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), mtx, true);
                        }
                    } catch (FileNotFoundException ex) {
                        Log.e("grandroid", null, ex);
                    } finally {
                        try {
                            imageStream.close();
                        } catch (IOException ex) {
                            Log.e("grandroid", null, ex);
                        }
                    }
                }
//            if (android.os.Build.BRAND.equals("lge")) {
//                Matrix mtx = new Matrix();
//                mtx.postScale(-1, -1);
//                photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), mtx, true);
//            }
                execute(context, photo);
                return true;
            } catch (Exception e) {
                Log.e("grandroid", null, e);
            }
        }
        return false;
    }

    public abstract void execute(Context context, Bitmap bitmap);
}
