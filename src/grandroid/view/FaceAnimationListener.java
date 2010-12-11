/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ViewFlipper;

/**
 *
 * @author Rovers
 */
public class FaceAnimationListener implements AnimationListener {

    int indexIn;
    int indexOut;
    protected ViewFlipper faceFlipper;
    protected ArrayFace face;

    public FaceAnimationListener(ViewFlipper faceFlipper, ArrayFace face) {
        this.faceFlipper = faceFlipper;
        this.face = face;
    }

    public void onAnimationStart(Animation arg0) {
        System.out.println("onAnimationStart  vf.getDisplayedChild()=" + faceFlipper.getDisplayedChild());
    }

    public void onAnimationEnd(Animation arg0) {
        System.out.println("onAnimationEnd  vf.getDisplayedChild()=" + faceFlipper.getDisplayedChild());
        if (faceFlipper.getDisplayedChild() != 0) {
            faceFlipper.setInAnimation(null);
            faceFlipper.setOutAnimation(null);
            faceFlipper.showNext();
        }
        face.afterFaceChange(indexIn, indexOut);
    }

    public void onAnimationRepeat(Animation arg0) {
    }
}
