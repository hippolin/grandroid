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
 * 提供旋轉特效的Face
 * @author Rovers
 */
public abstract class RotationFace extends Face {

    /**
     * 
     */
    protected ViewGroup faceGroup;
    /**
     * 
     */
    protected LinearLayout activeLayout;
    /**
     * 
     */
    protected LinearLayout fakeLayout;
    /**
     * 
     */
    protected GoAction changeFaceAction;

    /**
     * 禁止覆寫onCreate，使用RotationFace應覆寫initLayout()及updateFakeLayout()
     * @param savedInstanceState
     */
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

    /**
     * 
     * @return
     */
    public int getActiveLayoutResource() {
        return -1;
    }

    /**
     * 
     * @return
     */
    public int getFakeLayoutResource() {
        return -1;
    }

    /**
     * 初始化真正互動的Layout
     * @param bundle
     * @param activeLayout
     */
    public abstract void initLayout(Bundle bundle, LinearLayout activeLayout);

    /**
     * 一般應覆寫此函數，通常在gotoFace()執行後被叫用
     * @param bundle
     * @param fakeLayout 由prepareFakeLayout()所產生的假畫面Layout
     */
    public void updateFakeLayout(Bundle bundle, LinearLayout fakeLayout) {
    }

    /**
     * gotoFace執行後，在跳去別的Class之前被呼叫，一般不需覆寫此方法
     * @return 回傳false則會中斷操作，預設總是回傳true
     */
    public boolean beforeFaceChange() {
        return true;
    }

    /**
     * gotoFace執行後，在動畫播放完後被呼叫，一般不需覆寫此方法
     */
    public void afterFaceChange() {
    }

    /**
     * 讓app跳到Class c所指定的頁面
     * @param c
     */
    public void gotoFace(GoAction changeFaceAction) {
        gotoFace(changeFaceAction, true);
    }

    /**
     * 讓app跳到Class c所指定的頁面
     * @param c
     * @param updateLayout
     */
    public void gotoFace(GoAction changeFaceAction, boolean updateLayout) {
        if (beforeFaceChange()) {
            if (updateLayout) {
                prepareFakeLayout();
            }
            this.changeFaceAction = changeFaceAction;
            new FaceRotator(this).play();
        }
    }

    /**
     * 產生假畫面Layout，一般不需覆寫此方法
     * @return 空的LinearLayout或來自xml定義的LinearLayout
     */
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

    /**
     * 不提供開發者叫用
     */
    public void changeFrame() {
        if (changeFaceAction != null) {
            changeFaceAction.execute();
            changeFaceAction = null;
        }
    }

    /**
     * 
     */
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
