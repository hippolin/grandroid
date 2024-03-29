/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.anim;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

/**
 *
 * @author Rovers
 */
public class FlipperController implements AnimationListener {

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
    protected ViewFlipper viewFlipper;
    /**
     * 
     */
    protected View activeView;
    /**
     * 
     */
    protected View fakeView;
    /**
     * 
     */
    protected AnimationEventHandler handler;

    /**
     * 
     * @param animationArray
     * @param context
     */
    public FlipperController(int[] animationArray, Context context) {
        viewFlipper = new ViewFlipper(context);

        aniLeftIn = AnimationUtils.loadAnimation(context, animationArray[0]);
        aniLeftOut = AnimationUtils.loadAnimation(context, animationArray[1]);
        aniRightIn = AnimationUtils.loadAnimation(context, animationArray[2]);
        aniRightOut = AnimationUtils.loadAnimation(context, animationArray[3]);

        aniLeftIn.setAnimationListener(this);
        aniRightIn.setAnimationListener(this);
    }

    /**
     * 
     * @return
     */
    public ViewFlipper getViewFlipper() {
        return viewFlipper;
    }

    /**
     * 
     * @param activeView
     * @param fakeView
     */
    public void setupViews(View activeView, View fakeView) {
        this.activeView = activeView;
        this.fakeView = fakeView;
        viewFlipper.removeAllViews();
        viewFlipper.addView(activeView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        viewFlipper.addView(fakeView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    /**
     * 
     * @return
     */
    public View getActiveView() {
        return activeView;
    }

    /**
     * 
     * @return
     */
    public View getFakeView() {
        return fakeView;
    }

    /**
     * 
     * @return
     */
    public AnimationEventHandler getHandler() {
        return handler;
    }

    /**
     * 
     * @param handler
     */
    public void setHandler(AnimationEventHandler handler) {
        this.handler = handler;
    }

    /**
     * 
     */
    public void playMovingLeft() {
        viewFlipper.setInAnimation(aniRightIn);
        viewFlipper.setOutAnimation(aniLeftOut);
        viewFlipper.showNext();
    }

    /**
     * 
     */
    public void playMovingRight() {
        viewFlipper.setInAnimation(aniLeftIn);
        viewFlipper.setOutAnimation(aniRightOut);
        viewFlipper.showPrevious();
    }

    /**
     * 
     */
    public void showNext() {
        if (viewFlipper.getDisplayedChild() != 0) {
            viewFlipper.setInAnimation(null);
            viewFlipper.setOutAnimation(null);
            viewFlipper.showNext();
        }
    }
    /**
     * 
     */
    public void showPrevious() {
        if (viewFlipper.getDisplayedChild() != 0) {
            viewFlipper.setInAnimation(null);
            viewFlipper.setOutAnimation(null);
            viewFlipper.showPrevious();
        }
    }
    /**
     * 
     * @param arg0
     */
    public void onAnimationStart(Animation arg0) {
        if (handler != null) {
            handler.beforeAnimation();
        }
    }

    /**
     * 
     * @param arg0
     */
    public void onAnimationEnd(Animation arg0) {
        if (handler != null) {
            handler.afterAnimation();
        }
    }

    /**
     * 
     * @param arg0
     */
    public void onAnimationRepeat(Animation arg0) {
    }
}
