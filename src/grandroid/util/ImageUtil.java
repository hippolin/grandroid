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
import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

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
        if (uri.startsWith("http")) {
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
                Log.e("grandroid", null, e);
                throw e;
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            InputStream instream = bufHttpEntity.getContent();
            bitmap = BitmapFactory.decodeStream(instream);
        } catch (Exception e) {
            Log.e("grandroid", null, e);
            throw e;
        }
        return bitmap;
    }

    public static Bitmap downloadAndLoad(URL url, String path, String filename) throws Exception {
        /* 資料夾不在就先建立 */
        File f = new File(Environment.getExternalStorageDirectory(), path);

        if (!f.exists()) {
            f.mkdir();
        }
        /* 儲存相片檔 */
        File n = new File(f, filename);
        return downloadAndLoad(url, n);
    }

    public static Bitmap downloadAndLoad(URL url, File file) throws Exception {
        /* Open a connection to that URL. */
        URLConnection ucon = url.openConnection();

        /*
         * Define InputStreams to read from the URLConnection.
         */
        InputStream is = ucon.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);

        /*
         * Read bytes to the Buffer until there is nothing more to read(-1).
         */
        ByteArrayBuffer baf = new ByteArrayBuffer(50);
        int current = 0;
        while ((current = bis.read()) != -1) {
            baf.append((byte) current);
        }

        /* Convert the Bytes read to a String. */
        FileOutputStream fos = new FileOutputStream(file);
        byte[] imageByteArray = baf.toByteArray();
        fos.write(imageByteArray);
        fos.close();
        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
    }

    public static Bitmap loadBitmap(File file) throws FileNotFoundException {
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            throw new FileNotFoundException();
        }
    }

    public static String saveBitmap(Bitmap bitmap, String path) {
        long cnt = System.currentTimeMillis();
        return saveBitmap(bitmap, path, cnt + ".jpg", true, 100);
    }

    public static String saveBitmap(Bitmap bitmap, String path, String fileNamePrefix, String fileNameSuffix, boolean saveAsJPEG, int quality) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                /* 資料夾不在就先建立 */
                File f = new File(Environment.getExternalStorageDirectory(), path);
                if (!f.exists()) {
                    f.mkdir();
                }
                NumberFormat nf = new DecimalFormat("0000");
                for (int fileIndex = 1; fileIndex <= 9999; fileIndex++) {
                    String fileName = fileNamePrefix + nf.format(fileIndex) + fileNameSuffix;
                    File n = new File(f, fileName);
                    if (!n.exists()) {
                        return saveBitmap(bitmap, path, fileName, saveAsJPEG, quality);
                    }
                }
            } catch (Exception e) {
                Log.e("grandroid", null, e);
            }
        }
        return null;
    }

    public static String saveBitmap(Bitmap bitmap, String path, String fileName, boolean saveAsJPEG, int quality) {
        /* 儲存檔案 */
        if (bitmap != null) {
            /* 檢視SDCard是否存在 */
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                /* SD卡不存在，顯示Toast訊息 */
            } else {
                try {
                    /* 資料夾不在就先建立 */
                    File f = new File(
                            Environment.getExternalStorageDirectory(), path);

                    if (!f.exists()) {
                        f.mkdir();
                    }

                    /* 儲存相片檔 */
                    File n = new File(f, fileName);
                    FileOutputStream bos =
                            new FileOutputStream(n.getAbsolutePath());
                    /* 檔案轉換 */
                    if (saveAsJPEG) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                    } else {
                        bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
                    }
                    /* 呼叫flush()方法，更新BufferStream */
                    bos.flush();
                    /* 結束OutputStream */
                    bos.close();
                    return n.getAbsolutePath();
                } catch (Exception e) {
                    Log.e("grandroid", null, e);
                }
            }
        }
        return null;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap resizeBitmapMaxWidth(Bitmap bitmap, int width) {
        if (bitmap.getWidth() > width) {
            return resizeBitmap(bitmap, width / (float) bitmap.getWidth());
        } else {
            return bitmap;
        }
    }

    public static Bitmap resizeBitmapMaxHeight(Bitmap bitmap, int height) {
        if (bitmap.getHeight() > height) {
            return resizeBitmap(bitmap, height / (float) bitmap.getHeight());
        } else {
            return bitmap;
        }
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

    public static Bitmap cut(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if (w > h) {
            return cut(bmp, (w - h) / 2f, 0);
        } else if (h > w) {
            return cut(bmp, 0, (h - w) / 2f);
        } else {
            return bmp;
        }
    }

    public static Bitmap cut(Bitmap bmp, float cutMarginX, float cutMarginY) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp.getWidth() - Math.round(2 * cutMarginX), bmp.getHeight() - Math.round(2 * cutMarginY), bmp.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp, -cutMarginX, -cutMarginY, null);
        return bmOverlay;
    }

    public static Bitmap cut(Bitmap bmp, int cutLeft, int cutTop, int cutRight, int cutBottom) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp.getWidth() - (cutLeft + cutRight), bmp.getHeight() - (cutTop + cutBottom), bmp.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp, -cutLeft, -cutTop, null);
        return bmOverlay;
    }
}
