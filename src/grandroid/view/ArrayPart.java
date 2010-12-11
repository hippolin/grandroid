/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import grandroid.anim.AnimationEventHandler;
import grandroid.anim.FlipperController;
import java.util.List;

/**
 *
 * @author Rovers
 */
public class ArrayPart<T> implements AnimationEventHandler {

    protected Context context;
    protected ArrayPartListener<T> partListener;
    protected int[] animArray;
    protected FaceTouchSensor touchSensor;
    protected FlipperController flipperController;
    protected int index;
    protected int nextIndex;
    protected boolean cycle;
    protected boolean touchable;
    protected List<T> dataObjects;
    protected View.OnTouchListener gestureListener;

    public ArrayPart(Context context, int[] animArray, View activeView, View fakeView, ArrayPartListener partListener) {
        this.context = context;
        this.animArray = animArray;
        this.partListener = partListener;
        cycle = true;
        touchable = true;
        touchSensor = new FaceTouchSensor(context, this);
        dataObjects = partListener.loadArray();

        gestureListener = new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent me) {
                if (touchable) {
                    return touchSensor.onTouchEvent(me);
                } else {
                    return false;
                }
            }
        };
        activeView.setOnTouchListener(gestureListener);
        flipperController = new FlipperController(animArray, context);
        flipperController.setupViews(activeView, fakeView);
        flipperController.setHandler(this);
    }

    public void attachOnTouchListener(View view) {
        view.setOnTouchListener(gestureListener);
    }

//    public boolean onTouchEvent(MotionEvent me) {
//        touchSensor.onTouchEvent(me);
//        return true;
//    }
    public View getView() {
        return flipperController.getViewFlipper();
    }

    public final void next() {
        if (canNext()) {
            nextIndex = index >= dataObjects.size() - 1 ? 0 : index + 1;
            if (partListener.beforeFaceChange(nextIndex, index)) {
                flipperController.playMovingLeft();
            }
        }
    }

    public final void next(int newIndex) {
        nextIndex = newIndex;
        if (partListener.beforeFaceChange(nextIndex, index)) {
            flipperController.playMovingLeft();
        }
    }

    public final void previous() {
        if (canPrevious()) {
            nextIndex = index <= 0 ? dataObjects.size() - 1 : index - 1;
            if (partListener.beforeFaceChange(nextIndex, index)) {
                flipperController.playMovingRight();
            }
        }
    }

    public final void previous(int newIndex) {
        nextIndex = newIndex;
        if (partListener.beforeFaceChange(nextIndex, index)) {
            flipperController.playMovingRight();
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
        dataObjects = partListener.loadArray();
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

    public T getData(int dataIndex) {
        return dataObjects.get(dataIndex);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return dataObjects.size();
    }

    public void onLongPress(MotionEvent me) {
    }

    public void beforeAnimation() {
    }

    public void afterAnimation() {
        flipperController.showNext();
        int oldIndex = index;
        index = nextIndex;
        partListener.afterFaceChange(nextIndex, oldIndex);
    }

    public boolean onTouchEvent(MotionEvent me) {
        return touchSensor.onTouchEvent(me);
    }

    public List<T> getDataObjects(){
        return this.dataObjects;
    }
}
