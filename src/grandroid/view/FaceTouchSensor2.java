/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

/**
 *
 * @author Rovers
 */
public class FaceTouchSensor2 extends SimpleOnGestureListener {

    /**
     * 
     */
    protected ArrayFace face;
    private GestureDetector gestureScanner;
    /**
     * 
     */
    protected Animation aniLeftIn;
    /**
     * 
     */
    protected Animation aniLeftOut;
    /**
     * 
     */
    protected Animation aniRightIn;
    /**
     * 
     */
    protected Animation aniRightOut;
    /**
     * 
     */
    protected FaceAnimationListener aniListener;
    /**
     * 
     */
    protected ViewFlipper faceFlipper;
    /**
     * 
     */
    protected LinearLayout activeLayout;
    /**
     * 
     */
    protected LinearLayout fakeLayout;

    //protected Face face;
    /**
     * 
     * @param face
     * @param animationArray
     */
    public FaceTouchSensor2(final ArrayFace face, int[] animationArray) {
        this.face = face;

        faceFlipper = new ViewFlipper(face);
        activeLayout = new LinearLayout(face);
        fakeLayout = new LinearLayout(face);

        activeLayout.setOrientation(LinearLayout.VERTICAL);
        fakeLayout.setOrientation(LinearLayout.VERTICAL);
        faceFlipper.addView(activeLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        faceFlipper.addView(fakeLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));


        gestureScanner = new GestureDetector(face, this);

        aniLeftIn = AnimationUtils.loadAnimation(face, animationArray[0]);
        aniLeftOut = AnimationUtils.loadAnimation(face, animationArray[1]);
        aniRightIn = AnimationUtils.loadAnimation(face, animationArray[2]);
        aniRightOut = AnimationUtils.loadAnimation(face, animationArray[3]);

        aniListener = new FaceAnimationListener(faceFlipper, face);
        aniLeftIn.setAnimationListener(aniListener);
        aniRightIn.setAnimationListener(aniListener);
        face.addContentView(faceFlipper, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    /**
     * 
     * @return
     */
    public LinearLayout getActiveLayout() {
        return activeLayout;
    }

    /**
     * 
     * @return
     */
    public LinearLayout getFakeLayout() {
        return fakeLayout;
    }

    /**
     * 
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        return gestureScanner.onTouchEvent(event);
    }

    /**
     * 
     * @param me
     */
    @Override
    public void onLongPress(MotionEvent me) {
        face.onLongPress(me);
    }

    /**
     * 
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityY) < Math.abs(velocityX)) {
            //System.out.println("V_X=" + velocityX + ", V_Y=" + velocityY);
            if (velocityX > 150) {
                face.previous();
                return true;
            } else if (velocityX < -150) {
                face.next();
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param indexIn
     * @param indexOut
     */
    public void playMovingLeft(int indexIn, int indexOut) {
        aniListener.indexIn = indexIn;
        aniListener.indexOut = indexOut;
        faceFlipper.setInAnimation(aniRightIn);
        faceFlipper.setOutAnimation(aniLeftOut);
        faceFlipper.showNext();
    }

    /**
     * 
     * @param indexIn
     * @param indexOut
     */
    public void playMovingRight(int indexIn, int indexOut) {
        aniListener.indexIn = indexIn;
        aniListener.indexOut = indexOut;
        faceFlipper.setInAnimation(aniLeftIn);
        faceFlipper.setOutAnimation(aniRightOut);
        faceFlipper.showPrevious();
    }
}
