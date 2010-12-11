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
 *
 * @author Rovers
 */
public abstract class ArrayFace<T> extends Face {

    protected FaceTouchSensor2 touchSensor;
    protected int index;
    protected boolean cycle;
    protected boolean touchable;
    protected List<T> dataObjects;
    protected View.OnTouchListener gestureListener;

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

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        System.out.println(me.toString());
        touchSensor.onTouchEvent(me);
        return super.onTouchEvent(me);
    }

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

    public final void next(int newIndex) {
        boolean confirm = beforeFaceChange(newIndex, index);
        if (confirm) {
            touchSensor.playMovingLeft(newIndex, index);
            index = newIndex;
        }
    }

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

    public final void previous(int newIndex) {
        boolean confirm = beforeFaceChange(newIndex, index);
        if (confirm) {
            touchSensor.playMovingRight(newIndex, index);
            index = newIndex;
        }
    }

    public final void nextFirst() {
        if (dataObjects.size() > 0) {
            next(0);
        }
    }

    public final void nextLast() {
        if (dataObjects.size() > 0) {
            next(dataObjects.size() - 1);
        }
    }

    public final void update() {
        dataObjects = loadArray();
    }

    public boolean canNext() {
        return cycle || index < dataObjects.size() - 1;
    }

    public boolean canPrevious() {
        return cycle || index > 0;
    }

    public int getActiveLayoutResource() {
        return -1;
    }

    public int getFakeLayoutResource() {
        return -1;
    }

    protected abstract int[] getAnimationArray();

    public abstract void initLayout(Bundle savedInstanceState, LinearLayout activeLayout, LinearLayout fakeLayout);

    public abstract List<T> loadArray();

    public boolean beforeFaceChange(int indexIn, int indexOut) {
        return true;
    }

    public void afterFaceChange(int indexIn, int indexOut) {
    }

    public boolean isCycle() {
        return cycle;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public int getIndex() {
        return index;
    }

    public T getCurrentData() {
        return dataObjects.get(index);
    }

    void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return dataObjects.size();
    }

    public void onLongPress(MotionEvent me) {
    }
}
