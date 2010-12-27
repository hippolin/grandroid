/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Rovers
 */
public class ImageUtil {

    public static Drawable getDrawableByName(Context context, String drawableName) {
        String uri = "drawable/" + drawableName;
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

        Drawable image = context.getResources().getDrawable(imageResource);
        return image;
    }

    public static Bitmap loadBitmap(String uri) throws Exception {
        if (uri.startsWith("http://")) {
            return loadBitmap(new URL(uri));
        } else {
            return loadBitmap(new File(uri));
        }
    }

    /**
     * 
     * @param url
     * @return
     * @throws Exception  
     */
    public static Bitmap loadBitmap(URL url) throws Exception {
        Bitmap bitmap = null;
        try {
//            myFileUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
//            //conn.setDoInput(true);
//            //conn.connect();
//            InputStream is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//            is.close();
//            conn.disconnect();
            HttpGet httpRequest = null;

            try {
                httpRequest = new HttpGet(url.toURI());
            } catch (URISyntaxException e) {
                Log.e("grandroid-image", null, e);
                throw e;
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            InputStream instream = bufHttpEntity.getContent();
            bitmap = BitmapFactory.decodeStream(instream);
        } catch (Exception e) {
            Log.e("grandroid-image", null, e);
            throw e;
        }
        return bitmap;
    }

    public static Bitmap loadBitmap(File file) throws FileNotFoundException {
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            throw new FileNotFoundException();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 
     * @param bmp1
     * @param bmp2
     * @param left
     * @param top
     * @return
     */
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2, float left, float top) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, left, top, null);
        return bmOverlay;
    }
}
