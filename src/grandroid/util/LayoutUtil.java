/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;

/**
 *
 * @author Rovers
 */
public class LayoutUtil {

    protected int width;
    protected int height;

    public LayoutUtil(Activity frame) {
        DisplayMetrics metrics = new DisplayMetrics();
        frame.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] layoutWidth(int[] size) {
        int totalNeed = 0;
        int availWidth = width;
        int takeAllSpace = -1;
        for (int i = 0; i < size.length; i++) {
            if (size[i] > 0) {
                availWidth -= size[i];
            } else if (size[i] < 0) {
                totalNeed += size[i];
            } else {
                takeAllSpace = i;
            }
        }
        int[] result = new int[size.length];

        for (int i = 0; i < size.length; i++) {
            if (size[i] > 0) {
                result[i] = size[i];
            } else if (size[i] < 0) {
                result[i] = takeAllSpace < 0 ? (int) (availWidth * (size[i] / (float) totalNeed)) : -size[i];
            } else {
                result[i] = Math.max(0, availWidth + totalNeed);
            }
        }
        return result;
    }
}
