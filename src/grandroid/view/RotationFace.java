/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import grandroid.action.GoAction;

/**
 *
 * @author Rovers
 */
public abstract class RotationFace extends Face {

    protected ViewGroup faceGroup;
    protected LinearLayout activeLayout;
    protected LinearLayout fakeLayout;
    protected Class nextFrameClass;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faceGroup = new LinearLayout(this);
        ((LinearLayout) faceGroup).setBaselineAligned(false);

        if (getActiveLayoutResource() > 0) {
            activeLayout = (LinearLayout) this.loadLayout(getActiveLayoutResource());
        } else {
            activeLayout = new LinearLayout(this);
            activeLayout.setOrientation(LinearLayout.VERTICAL);
        }
        faceGroup.addView(activeLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        this.addContentView(faceGroup, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        initLayout(savedInstanceState, activeLayout);

        faceGroup.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
    }

    public int getActiveLayoutResource() {
        return -1;
    }

    public int getFakeLayoutResource() {
        return -1;
    }

    public abstract void initLayout(Bundle bundle, LinearLayout activeLayout);

    public void updateFakeLayout(Bundle bundle, LinearLayout fakeLayout) {
    }

    public boolean beforeFaceChange() {
        return true;
    }

    public void afterFaceChange() {
    }

    public void gotoFace(Class<? extends Face> c) {
        gotoFace(c, true);
    }

    public void gotoFace(Class<? extends Face> c, boolean updateLayout) {
        if (beforeFaceChange()) {
            if (updateLayout) {
                prepareFakeLayout();
            }
            nextFrameClass = c;
            new FaceRotator(this).play();
        }
    }

    public LinearLayout prepareFakeLayout() {
        if (fakeLayout != null) {
            faceGroup.removeView(fakeLayout);
        }
        if (getFakeLayoutResource() > 0) {
            fakeLayout = (LinearLayout) this.loadLayout(getFakeLayoutResource());
        } else {
            fakeLayout = new LinearLayout(this);
            fakeLayout.setOrientation(LinearLayout.VERTICAL);
        }
        faceGroup.addView(fakeLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        updateFakeLayout(this.getIntent().getExtras(), fakeLayout);
        return fakeLayout;
    }

    void changeFrame() {
        if (nextFrameClass != null) {
            new GoAction(this, "", nextFrameClass).execute();
            nextFrameClass = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (activeLayout != null) {
            activeLayout.setVisibility(View.VISIBLE);
        }
        if (fakeLayout != null) {
            fakeLayout.setVisibility(View.GONE);
            faceGroup.removeView(fakeLayout);
            fakeLayout = null;
        }
    }
}
