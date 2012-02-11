/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import grandroid.util.ImageUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Rovers
 */
public class PhotoAgent {

    private Bitmap bmp;
    private File path;

    public PhotoAgent(Bitmap bmp) {
        this.bmp = bmp;
    }

    public PhotoAgent fixSize(int width, int height) {
        if (width / (float) height > bmp.getWidth() / (float) bmp.getHeight()) {
            //較bmp寬，裁上下
            fixWidth(width);
            return trimHeight(height);
        } else {
            //較bmp瘦，裁左右
            fixHeight(height);
            return trimWidth(width);
        }
    }

    public PhotoAgent fixWidth(int width) {
        bmp = ImageUtil.resizeBitmap(bmp, width / (float) bmp.getWidth());
        return this;
    }

    public PhotoAgent fixHeight(int height) {
        bmp = ImageUtil.resizeBitmap(bmp, height / (float) bmp.getHeight());
        return this;
    }

    public PhotoAgent trimWidth(int width) {
        if (bmp.getWidth() > width) {
            bmp = ImageUtil.cut(bmp, (bmp.getWidth() - width) / 2f, 0);
        }
        return this;
    }

    public PhotoAgent trimHeight(int height) {
        if (bmp.getHeight() > height) {
            bmp = ImageUtil.cut(bmp, 0, (bmp.getHeight() - height) / 2f);
        }
        return this;
    }

    public PhotoAgent trimEdge(int cutLeft, int cutTop, int cutRight, int cutBottom) {
        bmp = ImageUtil.cut(bmp, cutLeft, cutTop, cutRight, cutBottom);
        return this;
    }

    public Bitmap getBitmap() {
        return bmp;
    }

    public PhotoAgent square(float size) {
        if (bmp.getWidth() > bmp.getHeight()) {
            bmp = ImageUtil.cut(ImageUtil.resizeBitmap(bmp, size / bmp.getHeight()));
        } else {
            bmp = ImageUtil.cut(ImageUtil.resizeBitmap(bmp, size / bmp.getWidth()));
        }
        return this;
    }

    public File getStoredFile() {
        return path;
    }

    public PhotoAgent sdcard() {
        path = Environment.getExternalStorageDirectory();
        return this;
    }

    public PhotoAgent dir(String dir) {
        path = new File(path, dir);
        if (!path.exists()) {
            path.mkdir();
        }
        return this;
    }

    public File file(String fileName) {
        path = new File(path, fileName);
        return path;
    }

    public boolean save(File file) {
        return save(file, Bitmap.CompressFormat.JPEG);
    }

    public boolean save(File file, Bitmap.CompressFormat format) {
        FileOutputStream bos = null;
        try {
            bos = new FileOutputStream(file.getAbsolutePath());
            /*
             * 檔案轉換
             */
            bmp.compress(format, 100, bos);
            /*
             * 呼叫flush()方法，更新BufferStream
             */
            bos.flush();
            /*
             * 結束OutputStream
             */
            bos.close();
            path = file.getAbsoluteFile();
            return true;
        } catch (Exception ex) {
            Log.e("grandroid", null, ex);
            return false;
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                Log.e("grandroid", null, ex);
            }
        }
    }
}
