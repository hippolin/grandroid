/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 *
 * @author Rovers
 */
public class FaceTouchSensor extends SimpleOnGestureListener {

    protected Face face;
    protected ArrayPart arrayPart;
    private GestureDetector gestureScanner;

    //protected Face face;
    public FaceTouchSensor(Context context, ArrayPart arrayPart) {
        this.arrayPart = arrayPart;
        gestureScanner = new GestureDetector(context, this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return gestureScanner.onTouchEvent(event);
    }

    @Override
    public void onLongPress(MotionEvent me) {
        arrayPart.onLongPress(me);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityY) < 0.8 * Math.abs(velocityX) && Math.abs(e1.getX() - e2.getX()) > 100) {
            if (velocityX > 150) {
                arrayPart.previous();
                return true;
            } else if (velocityX < -150) {
                arrayPart.next();
                return true;
            }
        }
        return false;
    }
}
