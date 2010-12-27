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
 * 可動態製作顯示陣列式資料的View元件
 * @param <T> 
 * @author Rovers
 */
public class ArrayPart<T> implements AnimationEventHandler {

    /**
     * 
     */
    protected Context context;
    /**
     * 
     */
    protected ArrayPartListener<T> partListener;
    /**
     * 
     */
    protected int[] animArray;
    /**
     * 
     */
    protected FaceTouchSensor touchSensor;
    /**
     * 
     */
    protected FlipperController flipperController;
    /**
     * 
     */
    protected int index;
    /**
     * 
     */
    protected int nextIndex;
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
     * 傳入一個真實的view/layout，以及一個假的view/layout，建立ArrayPart實體
     * @param context
     * @param animArray ArrayPart具有播放過場動畫功能，故需使用到動畫定義xml，一般此參數為 new int[]{R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out}
     * @param activeView 真正可操作的介面元件(一般為LinearLayout)
     * @param fakeView 只在過場動畫裡才會用到的介面元件(一般會是視覺上與activeView相仿的元件)
     * @param partListener callback物件，讓ArrayPart觸發事件時呼叫
     */
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

    /**
     * 讓某個view元件的觸碰感應可以連動ArrayPart(例如手指向左滑可以跳下一頁)
     * @param view
     */
    public void attachOnTouchListener(View view) {
        view.setOnTouchListener(gestureListener);
    }

//    public boolean onTouchEvent(MotionEvent me) {
//        touchSensor.onTouchEvent(me);
//        return true;
//    }
    /**
     * 取得ArrayPart所組合出來的ViewFlipper元件
     * @return
     */
    public View getView() {
        return flipperController.getViewFlipper();
    }

    /**
     * 播放過場動畫，顯示下一頁
     */
    public final void next() {
        if (canNext()) {
            nextIndex = index >= dataObjects.size() - 1 ? 0 : index + 1;
            if (partListener.beforeFaceChange(nextIndex, index)) {
                flipperController.playMovingLeft();
            }
        }
    }

    /**
     * 播放過場動畫，顯示參數指定頁
     * @param newIndex
     */
    public final void next(int newIndex) {
        nextIndex = newIndex;
        if (partListener.beforeFaceChange(nextIndex, index)) {
            flipperController.playMovingLeft();
        }
    }

    /**
     * 播放過場動畫，顯示前一頁
     */
    public final void previous() {
        if (canPrevious()) {
            nextIndex = index <= 0 ? dataObjects.size() - 1 : index - 1;
            if (partListener.beforeFaceChange(nextIndex, index)) {
                flipperController.playMovingRight();
            }
        }
    }

    /**
     * 播放過場動畫，顯示前一頁
     * @param newIndex
     */
    public final void previous(int newIndex) {
        nextIndex = newIndex;
        if (partListener.beforeFaceChange(nextIndex, index)) {
            flipperController.playMovingRight();
        }
    }

    /**
     * 播放過場動畫，顯示第一頁
     */
    public final void nextFirst() {
        if (dataObjects.size() > 0) {
            next(0);
        }
    }

    /**
     * 播放過場動畫，顯示最後一頁
     */
    public final void nextLast() {
        if (dataObjects.size() > 0) {
            next(dataObjects.size() - 1);
        }
    }

    /**
     * 重新擷取資料陣列
     */
    public final void update() {
        dataObjects = partListener.loadArray();
    }

    /**
     * 是否可跳至下一頁
     * @return 是否有下一頁 (循環模式下永遠為true)
     */
    public boolean canNext() {
        return cycle || index < dataObjects.size() - 1;
    }

    /**
     * 是否可跳至前一頁
     * @return 是否有前一頁 (循環模式下永遠為true)
     */
    public boolean canPrevious() {
        return cycle || index > 0;
    }

    /**
     * 回傳ActiveView所使用的Resource ID，當ActiveView的來源為XML定義的View時，才需覆寫此方法
     * @return
     */
    public int getActiveLayoutResource() {
        return -1;
    }

    /**
     * 回傳FakeView所使用的Resource ID，當FakeView的來源為XML定義的View時，才需覆寫此方法
     * @return
     */
    public int getFakeLayoutResource() {
        return -1;
    }

    /**
     * 是否為循環模式
     * @return
     */
    public boolean isCycle() {
        return cycle;
    }

    /**
     * 設定是否為循環模式
     * @param cycle
     */
    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    /**
     * 取得目前顯示的位置
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 取得目前顯示的資料
     * @return
     */
    public T getCurrentData() {
        return dataObjects.get(index);
    }

    /**
     * 依索引取得對應的資料
     * @param dataIndex
     * @return
     */
    public T getData(int dataIndex) {
        return dataObjects.get(dataIndex);
    }

    /**
     * 重設目前的索引值，呼叫後需手動更新介面
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 取得資料陣列的大小
     * @return
     */
    public int getSize() {
        return dataObjects.size();
    }

    /**
     * 長按後執行
     * @param me
     */
    public void onLongPress(MotionEvent me) {
    }

    /**
     * 在動畫之前執行
     */
    public void beforeAnimation() {
    }

    /**
     * 在動畫之後執行
     */
    public void afterAnimation() {
        flipperController.showNext();
        int oldIndex = index;
        index = nextIndex;
        partListener.afterFaceChange(nextIndex, oldIndex);
    }

    /**
     * 不應覆寫
     * @param me
     * @return
     */
    public boolean onTouchEvent(MotionEvent me) {
        return touchSensor.onTouchEvent(me);
    }

    /**
     * 不應覆寫
     * @return
     */
    public List<T> getDataObjects(){
        return this.dataObjects;
    }
}
