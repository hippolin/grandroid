/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.util;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

/**
 *
 * @author Rovers
 */
public class LayoutUtil {

    /**
     * 
     */
    protected int width;
    /**
     * 
     */
    protected int height;
    protected DisplayMetrics metrics;
    protected boolean statusBar;
    protected boolean titleBar;
    protected boolean compatibleMode;
    protected int softSpace;
    protected int rigidSpace;
    protected int excluseSpace;
    protected int cumulatePadding;

    /**
     * default setting: statusBar=true, titleBar=false, compatibleMode=true;
     * @param activity
     */
    public LayoutUtil(Activity activity) {
        this(activity, true, false, true);
    }

    public LayoutUtil(Activity frame, boolean statusBar, boolean titleBar, boolean compatibleMode) {
        this.statusBar = statusBar;
        this.titleBar = titleBar;
        this.compatibleMode = compatibleMode;
        metrics = new DisplayMetrics();
        frame.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        //Log.d("grandroid", "titleBarHeight = " + titleBarHeight);
    }

    /**
     * 
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * 
     * @return
     */
    public int getWidth() {
        return width;
    }

    public int getRealHeight() {
        return Math.round(metrics.heightPixels * metrics.density);
    }

    public int getRealWidth() {
        return Math.round(metrics.widthPixels * metrics.density);
    }

    /**
     * 
     * @param size
     * @return
     */
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

    public int getExcluseSpace() {
        return excluseSpace;
    }

    public void setExcluseSpace(int excluseSpace) {
        this.excluseSpace = excluseSpace;
    }

    public int predictAvailableSpaceHeight(int fullHeight) {
        int contentHeight = 0;
        contentHeight = fullHeight;
        if (statusBar) {
            if (compatibleMode) {
                contentHeight -= 25;
            } else {
                if (fullHeight <= 360) {
                    contentHeight -= 19;
                } else if (fullHeight <= 480) {
                    contentHeight -= 25;
                } else {
                    contentHeight -= 38;
                }
            }
        }
        if (titleBar) {
            //未實測
            contentHeight -= 38;
        }
        return contentHeight;
    }

    public int correctPadding(int paddingWhenY800) {
        return correctPadding(paddingWhenY800, 0);
    }

    public int correctPadding(int paddingWhenY800, int extraMinusForSmallScreen) {
        if (height > 480) {
            extraMinusForSmallScreen = 0;
        }
        if (compatibleMode) {
            return Math.max(Math.round(paddingWhenY800 * ((float) predictAvailableSpaceHeight(height) - excluseSpace) / (predictAvailableSpaceHeight(533) - excluseSpace)) - extraMinusForSmallScreen, 0);
        } else {
            if (height == 800) {
                return paddingWhenY800;
            } else {
                float paddingScale = (480f / width) * ((float) predictAvailableSpaceHeight(height) - excluseSpace) / (predictAvailableSpaceHeight(800) - excluseSpace);
                return Math.max(Math.round(paddingWhenY800 * paddingScale), 0);
            }
        }
    }

    public int fixPadding(int paddingWhenY800, int deltaRigid) {
        return correctPadding(paddingWhenY800, deltaRigid - correctPadding(deltaRigid));
    }
}
