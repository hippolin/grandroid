/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import java.util.List;

/**
 * 繼承自Face，提供陣列化Face的功能，達到動態內容呈現
 * 基本原理為，永遠有一個假頁面與真頁面(即真正可操作使用的頁面)
 * 建議改使用ArrayPart物件，不建議再使用本類別
 * @param <T> 
 * @author Rovers
 * @deprecated 
 */
@Deprecated
public abstract class ArrayFace<T> extends Face {

    /**
     * 
     */
    protected FaceTouchSensor2 touchSensor;
    /**
     * 
     */
    protected int index;
    /**
     * 
     */
    protected boolean cycle;
    /**
     * 
     */
    protected boolean touchable;
    /**
     * 
     */
    protected List<T> dataObjects;
    /**
     * 
     */
    protected View.OnTouchListener gestureListener;

    /**
     * 
     * @param savedInstanceState
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cycle = true;
        touchable = true;
        touchSensor = new FaceTouchSensor2(this, getAnimationArray());
        dataObjects = loadArray();

        gestureListener = new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent me) {
                if (touchable) {
                    return touchSensor.onTouchEvent(me);
                } else {
                    return false;
                }
            }
        };
//        if (getActiveLayoutResource() > 0) {
//            this.loadLayout(touchSensor.getActiveLayout(), getActiveLayoutResource());
//        }
//        if (getFakeLayoutResource() > 0) {
//            this.loadLayout(touchSensor.getFakeLayout(), getFakeLayoutResource());
//        }
        initLayout(savedInstanceState, touchSensor.getActiveLayout(), touchSensor.getFakeLayout());
    }

    /**
     * 
     * @param me
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        System.out.println(me.toString());
        touchSensor.onTouchEvent(me);
        return super.onTouchEvent(me);
    }

    /**
     * 
     */
    public final void next() {
        if (canNext()) {
            int newIndex = index >= dataObjects.size() - 1 ? 0 : index + 1;
            boolean confirm = beforeFaceChange(newIndex, index);
            if (confirm) {
                touchSensor.playMovingLeft(newIndex, index);
                index = newIndex;
            }
        }
    }

    /**
     * 
     * @param newIndex
     */
    public final void next(int newIndex) {
        boolean confirm = beforeFaceChange(newIndex, index);
        if (confirm) {
            touchSensor.playMovingLeft(newIndex, index);
            index = newIndex;
        }
    }

    /**
     * 
     */
    public final void previous() {
        if (canPrevious()) {
            int newIndex = index <= 0 ? dataObjects.size() - 1 : index - 1;
            boolean confirm = beforeFaceChange(newIndex, index);
            if (confirm) {
                touchSensor.playMovingRight(newIndex, index);
                index = newIndex;
            }
        }
    }

    /**
     * 
     * @param newIndex
     */
    public final void previous(int newIndex) {
        boolean confirm = beforeFaceChange(newIndex, index);
        if (confirm) {
            touchSensor.playMovingRight(newIndex, index);
            index = newIndex;
        }
    }

    /**
     * 
     */
    public final void nextFirst() {
        if (dataObjects.size() > 0) {
            next(0);
        }
    }

    /**
     * 
     */
    public final void nextLast() {
        if (dataObjects.size() > 0) {
            next(dataObjects.size() - 1);
        }
    }

    /**
     * 
     */
    public final void update() {
        dataObjects = loadArray();
    }

    /**
     * 
     * @return
     */
    public boolean canNext() {
        return cycle || index < dataObjects.size() - 1;
    }

    /**
     * 
     * @return
     */
    public boolean canPrevious() {
        return cycle || index > 0;
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
     * 
     * @return
     */
    protected abstract int[] getAnimationArray();

    /**
     * 
     * @param savedInstanceState
     * @param activeLayout
     * @param fakeLayout
     */
    public abstract void initLayout(Bundle savedInstanceState, LinearLayout activeLayout, LinearLayout fakeLayout);

    /**
     * 
     * @return
     */
    public abstract List<T> loadArray();

    /**
     * 
     * @param indexIn
     * @param indexOut
     * @return
     */
    public boolean beforeFaceChange(int indexIn, int indexOut) {
        return true;
    }

    /**
     * 
     * @param indexIn
     * @param indexOut
     */
    public void afterFaceChange(int indexIn, int indexOut) {
    }

    /**
     * 
     * @return
     */
    public boolean isCycle() {
        return cycle;
    }

    /**
     * 
     * @param cycle
     */
    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    /**
     * 
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 
     * @return
     */
    public T getCurrentData() {
        return dataObjects.get(index);
    }

    void setIndex(int index) {
        this.index = index;
    }

    /**
     * 
     * @return
     */
    public int getSize() {
        return dataObjects.size();
    }

    /**
     * 
     * @param me
     */
    public void onLongPress(MotionEvent me) {
    }
}
