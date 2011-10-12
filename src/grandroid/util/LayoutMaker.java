/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import grandroid.adapter.ItemClickable;

/**
 *
 * @author Rovers
 */
public class LayoutMaker {

    /**
     * 
     */
    protected ViewGroup lastLayout;
    /**
     * 
     */
    protected LinearLayout mainLayout;
    /**
     * 
     */
    protected int viewID = 1;
    protected Context context;
    protected static int colorBG = Color.WHITE;

    public LayoutMaker(Context context) {
        this.context = context;
        LinearLayout ll = new LinearLayout(context);

        ll.setBackgroundColor(colorBG);
        ll.setOrientation(LinearLayout.VERTICAL);
        mainLayout = ll;
        lastLayout = ll;
        ((Activity) context).setContentView(mainLayout);
    }

    public LayoutMaker(Context context, LinearLayout layout) {
        this.context = context;
        if (layout.getParent() == null) {
            LinearLayout fakeRoot = new LinearLayout(context);
            fakeRoot.setOrientation(LinearLayout.VERTICAL);
            fakeRoot.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            this.mainLayout = fakeRoot;
        } else {
            this.mainLayout = layout;
        }
        lastLayout = layout;
    }

    public LayoutMaker(Dialog dialog, Context context) {
        this.context = context;
        LinearLayout ll = new LinearLayout(context);
        ll.setBackgroundColor(colorBG);
        ll.setOrientation(LinearLayout.VERTICAL);
        mainLayout = ll;
        lastLayout = ll;
        dialog.setContentView(mainLayout);
    }

    public static void setBackgroundColor(int color) {
        colorBG = color;
    }

    public ViewGroup getLastLayout() {
        return lastLayout;
    }

    public LinearLayout getMainLayout() {
        return mainLayout;
    }

    public ViewGroup getRootLayout() {
        if (mainLayout.getParent() instanceof ScrollView || mainLayout.getParent() instanceof HorizontalScrollView) {
            return (ViewGroup) mainLayout.getParent().getParent();
        } else if (mainLayout.getParent() instanceof LinearLayout || mainLayout.getParent() instanceof RelativeLayout) {
            return (ViewGroup) lastLayout.getParent();
        } else {
            return mainLayout;
        }
    }

    /**
     * 新增一個view元件至"目前Layout"
     * 加入Layout時的參數為layWW(0)
     * @param view 任何view物件，如某種Layout、TextView、EditText、ListView或ImageView等等
     * @return view本身
     */
    public <T extends View> T add(T view) {
        lastLayout.addView(view);
        return view;
    }

    /**
     * 新增一個view元件至"目前Layout"，依傳入的LinearLayout.LayoutParams物件
     * @param view 任何view物件，如某種Layout、TextView、EditText、ListView或ImageView等等
     * @param params 一般不需自己生成，而是使用layFF()、layFW()、layWW(0)、layWW(1)代替
     * @return view本身
     */
    public <T extends View> T add(T view, LinearLayout.LayoutParams params) {
        lastLayout.addView(view, params);
        return view;
    }

    /**
     * 產生一個文字標籤
     * @param text 將顯示的文字
     * @return 生成的物件
     */
    public TextView createTextView(String text) {
        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv.setTextSize(20);
        tv.setText(text);
        return tv;
    }

    /**
     * 產生一個文字標籤，並加入到"目前Layout"
     * 加入Layout時的參數為layWW(0)
     * @param text 將顯示的文字
     * @return 生成的物件
     */
    public TextView addTextView(String text) {
        TextView tv = createTextView(text);
        lastLayout.addView(tv);
        return tv;
    }

    /**
     * 產生一個文字方塊
     * @param text 將顯示的文字
     * @return 生成的物件
     */
    public EditText createEditText(String text) {
        EditText et = new EditText(context);
        et.setTextColor(Color.BLACK);
        et.setTextSize(20);
        et.setText(text);
        return et;
    }

    /**
     * 產生一個文字方塊，並加入到"目前Layout"
     * 加入Layout時的參數為layWW(0)
     * @param text 將顯示的文字
     * @return 生成的物件
     */
    public EditText addEditText(String text) {
        return addEditText(text, false);
    }

    /**
     * 產生一個文字方塊，並加入到"目前Layout"
     * 加入Layout時的參數為layWW(0)
     * @param text 將顯示的文字
     * @param fillParent layout參數width是否為fillparent
     * @return 生成的物件
     */
    public EditText addEditText(String text, boolean fillParent) {
        EditText et = createEditText(text);
        if (fillParent) {
            lastLayout.addView(et, layFW());
        } else {
            lastLayout.addView(et);
        }
        return et;
    }

    /**
     * 產生一個按鈕
     * @param text 將顯示的文字
     * @return 生成的物件
     */
    public Button createButton(String text) {
        Button btn = new Button(context);
        btn.setTextColor(Color.BLACK);
        btn.setTextSize(20);
        btn.setText(text);
        return btn;
    }

    /**
     * 產生一個按鈕，並加入到"目前Layout"
     * 加入Layout時的參數：當"目前Layout"的Orientation橫向時為layWW(1)；其他狀況為layWW(0)
     * @param text 將顯示的文字
     * @return 生成的物件
     */
    public Button addButton(String text) {
        Button btn = createButton(text);
        lastLayout.addView(btn, layWW(lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL ? 1 : 0));
        return btn;
    }

    /**
     * 產生一個影像按鈕
     * @param <T> 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param viewClass 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param resourceID Resource ID
     * @return 生成的物件
     */
    public <T extends ImageView> T createImage(Class<T> viewClass, int resourceID) {
        try {
            T iv = viewClass.getConstructor(Context.class).newInstance(context);
            iv.setBackgroundColor(Color.argb(0, 0, 0, 0));
            iv.setImageResource(resourceID);
            return iv;
        } catch (Exception ex) {
            Log.e("grandroid-layout", null, ex);
            return null;
        }
    }

    /**
     * 使用網路上的圖片產生一個影像按鈕
     * @param <T> 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param viewClass 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param uri 圖片網址或檔案完整路徑
     * @return 生成的物件
     */
    public <T extends ImageView> T createImage(Class<T> viewClass, String uri) {
        try {
            T iv = viewClass.getConstructor(Context.class).newInstance(context);
            iv.setBackgroundColor(Color.argb(0, 0, 0, 0));
            iv.setImageBitmap(ImageUtil.loadBitmap(uri));
            return iv;
        } catch (Exception ex) {
            Log.e("grandroid-layout", null, ex);
            return null;
        }
    }

    /**
     * 使用網路上的圖片產生一個影像按鈕
     * @param <T> 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param viewClass 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param bitmap 圖片的Bitmap，來源為網路或SD卡檔案時，可使用ImageUtil.loadBitmap()取得
     * @return 生成的物件
     */
    public <T extends ImageView> T createImage(Class<T> viewClass, Bitmap bitmap) {
        try {
            T iv = viewClass.getConstructor(Context.class).newInstance(context);
            iv.setBackgroundColor(Color.argb(0, 0, 0, 0));
            iv.setImageBitmap(bitmap);
            return iv;
        } catch (Exception ex) {
            Log.e("grandroid-layout", null, ex);
            return null;
        }
    }

    /**
     * 產生一個圖片按鈕，並加入到"目前Layout"
     * 加入Layout時的參數：當"目前Layout"的Orientation橫向時為layWW(1)；其他狀況為layWW(0)
     * @param <T> 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param viewClass 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param resourceID Resource ID
     * @return 生成的物件
     */
    public <T extends ImageView> T addImage(Class<T> viewClass, int resourceID) {
        T iv = createImage(viewClass, resourceID);
        lastLayout.addView(iv, layWW(lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL ? 1 : 0));
        return iv;
    }

    /**
     * 產生一個圖片按鈕，並加入到"目前Layout"
     * 加入Layout時的參數：當"目前Layout"的Orientation橫向時為layWW(1)；其他狀況為layWW(0)
     * @param <T> 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param viewClass 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param bitmap 影像bitmap
     * @return 生成的物件
     */
    public <T extends ImageView> T addImage(Class<T> viewClass, Bitmap bitmap) {
        T iv = createImage(viewClass, bitmap);
        lastLayout.addView(iv, layWW(lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL ? 1 : 0));
        return iv;
    }

    /**
     * 使用網路上的圖片產生一個影像按鈕，並加入到"目前Layout"
     * 加入Layout時的參數：當"目前Layout"的Orientation橫向時為layWW(1)；其他狀況為layWW(0)
     * @param <T> 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param viewClass 需要產生影像物件的類別，一般為ImageView.class或ImageButton.class
     * @param uri 圖片網址或檔案完整路徑
     * @return 生成的物件
     */
    public <T extends ImageView> T addImage(Class<T> viewClass, String uri) {
        T iv = createImage(viewClass, uri);
        lastLayout.addView(iv, layWW(lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL ? 1 : 0));
        return iv;
    }

    public RadioGroup createRadioGroup(String[] btnTexts) {
        RadioGroup rg = new RadioGroup(context);
        RadioButton[] rbs = new RadioButton[btnTexts.length];
        for (int i = 0; i < rbs.length; i++) {
            rbs[i] = new RadioButton(context);
            rbs[i].setText(btnTexts[i]);
            rbs[i].setTextSize(16);
            rbs[i].setTextColor(Color.BLACK);
            rg.addView(rbs[i]);
        }
        return rg;
    }

    public CheckBox createCheckBox(boolean checked) {
        CheckBox cb = new CheckBox(context);
        cb.setChecked(checked);
        cb.setTextColor(Color.BLACK);
        cb.setTextSize(20);
        return cb;

    }

    public CheckBox addCheckBox(boolean checked) {
        CheckBox cb = createCheckBox(checked);
        lastLayout.addView(cb);
        return cb;
    }

    public CheckBox addCheckBox(boolean checked, String text) {
        CheckBox cb = createCheckBox(checked);
        cb.setText(text);
        lastLayout.addView(cb);
        //addTextView(text);
        return cb;
    }

    /**
     * 以傳入的Adapter產生一個清單
     * @param adapter 清單所使用的adapter，若該adapter實作了ItemClickable介面，則按下item會執行adapter的onItemClick事件
     * @return 生成的物件
     */
    public ListView createListView(BaseAdapter adapter) {
        final ListView lv = new ListView(context);
        lv.setBackgroundColor(colorBG);
        lv.setCacheColorHint(colorBG);
        if (adapter != null) {
            lv.setAdapter(adapter);
            if (ItemClickable.class.isInstance(adapter)) {
                lv.setOnItemClickListener(new OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int index, long arg3) {
                        ((ItemClickable) lv.getAdapter()).onClickItem(index, view, lv.getAdapter().getItem(index));
                    }
                });
                lv.setOnItemLongClickListener(new OnItemLongClickListener() {

                    public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long arg3) {
                        ((ItemClickable) lv.getAdapter()).onLongPressItem(index, view, lv.getAdapter().getItem(index));
                        return true;
                    }
                });
            }
        }
        return lv;
    }

    /**
     * 以傳入的Adapter產生一個清單，並加入到"目前Layout"
     * 預設加入Layout時的參數為layFF()
     * @param adapter 清單所使用的adapter，若該adapter實作了ItemClickable介面，則按下item會執行adapter的onItemClick事件
     * @return 生成的物件
     */
    public ListView addListView(BaseAdapter adapter) {
        ListView lv = createListView(adapter);
        lastLayout.addView(lv, layFF());
        return lv;
    }

    /**
     * 以傳入的Adapter產生一個清單，並加入到"目前Layout"
     * 預設加入Layout時的參數為layFF()
     * @param adapter 清單所使用的adapter，若該adapter實作了ItemClickable介面，則按下item會執行adapter的onItemClick事件
     * @param layoutParams 加至LastLayout的參數
     * @return 生成的物件
     */
    public ListView addListView(BaseAdapter adapter, LinearLayout.LayoutParams layoutParams) {
        ListView lv = createListView(adapter);
        lastLayout.addView(lv, layoutParams);
        return lv;
    }

    public Spinner createSpinner(BaseAdapter adapter) {
        final Spinner sp = new Spinner(context);
        //sp.setBackgroundColor(colorBG);
        if (adapter != null) {
            sp.setAdapter(adapter);
        }
        return sp;
    }

    public Spinner addSpinner(BaseAdapter adapter, boolean fillParent) {
        Spinner sp = createSpinner(adapter);
        if (fillParent) {
            lastLayout.addView(sp, layFW());
        } else {
            lastLayout.addView(sp);
        }
        return sp;
    }

    public Spinner addSpinner(BaseAdapter adapter) {
        return addSpinner(adapter, false);
    }

    /**
     * 傳回一個LayoutParams物件(W=FILL_PARENT, H=FILL_PARENT)
     * @return 生成的LinearLayout.LayoutParams物件
     */
    public LinearLayout.LayoutParams lay(int width, int height, int weight) {
        return new LinearLayout.LayoutParams(width, height, weight);
    }

    /**
     * 傳回一個LayoutParams物件(W=FILL_PARENT, H=FILL_PARENT)
     * @return 生成的LinearLayout.LayoutParams物件
     */
    public LinearLayout.LayoutParams layFF() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    }

    /**
     * 傳回一個LayoutParams物件(W=FILL_PARENT, H=WRAP_CONTENT)
     * @return 生成的LinearLayout.LayoutParams物件
     */
    public LinearLayout.LayoutParams layFW() {
        return layFW(0);
    }

    /**
     * 傳回一個LayoutParams物件(W=FILL_PARENT, H=WRAP_CONTENT)
     * @param weight 垂直方向上的權重(水平方向上已被設為填滿，故無權重)
     * @return 生成的LinearLayout.LayoutParams物件
     */
    public LinearLayout.LayoutParams layFW(float weight) {
        if (weight > 0) {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        } else {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 傳回一個LayoutParams物件(W=FILL_PARENT, H=WRAP_CONTENT)
     * @param weight 垂直方向上的權重(水平方向上已被設為填滿，故無權重)
     * @return 生成的LinearLayout.LayoutParams物件
     */
    public LinearLayout.LayoutParams layWF(float weight) {
        if (weight > 0) {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, weight);
        } else {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        }
    }

    /**
     * 傳回一個LayoutParams物件(W=WRAP_CONTENT, H=WRAP_CONTENT, weight)
     * @param weight 剩餘空間分配權重，若為0則不會被分配到剩餘空間(分配方向是看上層Layout的Orientation)
     * @return 生成的LinearLayout.LayoutParams物件
     */
    public LinearLayout.LayoutParams layWW(float weight) {
        if (weight > 0) {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        } else {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 產生一個橫向的LinearLayout，並加入到"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 不會帶有捲軸
     * 加入Layout時的參數："目前Layout"已是橫向時為layWW(0)；其他則為layFW
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addRowLayout() {
        return addRowLayout(false);
    }

    /**
     * 產生一個橫向的LinearLayout，並加入到"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 加入Layout時的參數：withScroll=true時為layFF()；"目前Layout"已是橫向時為layWW(0)；其他則為layFW
     * @param withScroll 是否要有捲軸
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addRowLayout(boolean withScroll) {
        return addRowLayout(withScroll, false);
    }

    public LinearLayout addRowLayout(boolean withScroll, boolean fullHeight) {
        if (fullHeight) {
            if (withScroll) {
                return addRowLayout(withScroll, layFF());
            } else {
                return addRowLayout(withScroll, layWF(0));
            }
        } else {
            if (lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL) {
                return addRowLayout(withScroll, layWW(0));
            } else {
                return addRowLayout(withScroll, layFW());
            }
        }
    }

    public LinearLayout addRowLayout(boolean withScroll, LinearLayout.LayoutParams params) {
        LinearLayout ll = new LinearLayout(context);
        //ll.setBackgroundColor(colorBG);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        if (withScroll) {
            HorizontalScrollView sv = new HorizontalScrollView(context);
            sv.setScrollContainer(true);
            sv.setFocusable(true);
            sv.addView(ll, layFF());
            lastLayout.addView(sv, params);
        } else {
            lastLayout.addView(ll, params);
        }
        lastLayout = ll;
        return ll;
    }

    /**
     * 產生一個直向的LinearLayout，並加入到"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 不會帶有捲軸
     * 加入Layout時的參數為layWW(1)
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addColLayout() {
        return addColLayout(false);
    }

    /**
     * 產生一個直向的LinearLayout，並加入到"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 加入Layout時的參數：withScroll=true時為layFW()；否則為layWW(1)
     * @param withScroll 是否要有捲軸
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addColLayout(boolean withScroll) {
        return addColLayout(withScroll, false);
    }

    public LinearLayout addColLayout(boolean withScroll, boolean fullHeight) {
        if (fullHeight) {
            return addColLayout(withScroll, layWF(1));
        } else {
            if (lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL) {
                return addColLayout(withScroll, layWW(1));
            } else {
                return addColLayout(withScroll, layFW(1));
            }

        }
    }

    public LinearLayout addColLayout(boolean withScroll, LinearLayout.LayoutParams params) {
        LinearLayout ll = new LinearLayout(context);
        //ll.setBackgroundColor(colorBG);
        ll.setOrientation(LinearLayout.VERTICAL);
        if (withScroll) {
            ScrollView sv = new ScrollView(context);
            sv.setScrollContainer(true);
            sv.setFocusable(true);
            sv.addView(ll, new ScrollView.LayoutParams(ScrollView.LayoutParams.FILL_PARENT, ScrollView.LayoutParams.FILL_PARENT));
            lastLayout.addView(sv, params);
        } else {
            lastLayout.addView(ll, params);
//            if (lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL) {
//                lastLayout.addView(ll, fullHeight ? layWF(1) : layWW(1));
//            } else {
//                lastLayout.addView(ll, fullHeight ? layFF() : layFW(1));
//            }
        }
        lastLayout = ll;
        return ll;
    }

    /**
     * 產生一個橫向的LinearLayout作為置頂的Banner，並加入進"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 注意：不可連續呼叫，且應遵守任何一個Layout最多只能有一個TopBanner及一個BottomBanner的原則
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addTopBanner() {
        return addTopBanner(false);
    }

    /**
     * 產生一個橫向的LinearLayout作為置頂的Banner，並加入進"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 注意：不可連續呼叫，且應遵守任何一個Layout最多只能有一個TopBanner及一個BottomBanner的原則
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addTopBanner(boolean isFloat) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        insertTopBanner(ll, isFloat);
        lastLayout = ll;
        return ll;
    }

    /**
     * 將傳入的view插入作為置頂的元件
     * 建議以addTopBanner()取代本方法
     * @param view
     */
    public void insertTopBanner(View view, boolean isFloat) {
        boolean isParentRelative = getParentOfLastLayout() instanceof RelativeLayout;
        ViewGroup parent = getParentOfLastLayout();
        parent.removeView(lastLayout);
        RelativeLayout rl;
        if (isParentRelative) {
            rl = (RelativeLayout) parent;
        } else {
            rl = new RelativeLayout(context);
            //rl.setBackgroundColor(colorBG);
        }

        if (isFloat) {
            if (lastLayout.getChildCount() == 0) {
                Log.e("grandroid", "must exist at least one full_parent child in current layout while call addTopBanner(true)");
            }
            RelativeLayout.LayoutParams rllpMain = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            rl.addView(lastLayout, 0, rllpMain);
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rl.addView(view, rllp);
        } else {
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            view.setId(viewID);
            rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rl.addView(view, rllp);

            RelativeLayout.LayoutParams rllpMain = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            rllpMain.addRule(RelativeLayout.BELOW, viewID++);
            if (rl.getChildCount() == 2) {
                rllpMain.addRule(RelativeLayout.ABOVE, rl.getChildAt(0).getId());
            }
            rl.addView(lastLayout, rllpMain);
        }
        if (!isParentRelative) {
            parent.addView(rl, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
    }

    /**
     * 產生一個橫向的LinearLayout作為置底的Banner，並加入進"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 注意：不可連續呼叫，且應遵守任何一個Layout最多只能有一個TopBanner及一個BottomBanner的原則
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addBottomBanner() {
        return addBottomBanner(false);
    }

    /**
     * 產生一個橫向的LinearLayout作為置底的Banner，並加入進"目前Layout"，之後"目前Layout"將指向此新生成的LinearLayout
     * 注意：不可連續呼叫，且應遵守任何一個Layout最多只能有一個TopBanner及一個BottomBanner的原則
     * @return 生成的LinearLayout物件
     */
    public LinearLayout addBottomBanner(boolean isFloat) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        insertBottomBanner(ll, isFloat);
        lastLayout = ll;
        return ll;
    }

    /**
     * 將傳入的view插入作為置底的元件
     * 建議以addBottomBanner()取代本方法
     * @param view
     */
    public void insertBottomBanner(View view, boolean isFloat) {
        boolean isParentRelative = getParentOfLastLayout() instanceof RelativeLayout;
        ViewGroup parent = getParentOfLastLayout();
        ViewGroup current = getDecoratedLastLayout();
        parent.removeView(current);
        RelativeLayout rl;
        if (isParentRelative) {
            rl = (RelativeLayout) parent;
        } else {
            rl = new RelativeLayout(context);
            //rl.setBackgroundColor(colorBG);
        }

        if (isFloat) {
            RelativeLayout.LayoutParams rllpMain = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            rl.addView(current, 0, rllpMain);
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rl.addView(view, rllp);
        } else {
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            view.setId(viewID);
            rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rl.addView(view, rllp);

            RelativeLayout.LayoutParams rllpMain = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            rllpMain.addRule(RelativeLayout.ABOVE, viewID++);
            if (rl.getChildCount() == 2) {
                rllpMain.addRule(RelativeLayout.BELOW, rl.getChildAt(0).getId());
            }
            rl.addView(current, rllpMain);
        }
        if (!isParentRelative) {
            parent.addView(rl, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
    }

    /**
     * 跳出"目前Layout"，使"目前Layout"指向其Parent(上一層)
     * 若是在呼叫了addTopBanner()或是addBottomBanner()的狀況，則會跳回先前的Layout (仍然像是上一層的概念)
     * 可連續呼叫，跳離多次後，再繼續增加其他ColLayout或RowLayout
     */
    public void escape() {
        escape(false);
    }

    /**
     * 跳出"目前Layout"，使"目前Layout"指向其Parent(上一層)
     * 若是在呼叫了addTopBanner()或是addBottomBanner()的狀況，則會跳回先前的Layout (仍然像是上一層的概念)
     * 可連續呼叫，跳離多次後，再繼續增加其他ColLayout或RowLayout
     */
    public void escape(boolean force) {
        if (lastLayout != mainLayout) {
            ViewGroup vg = lastLayout;
            lastLayout = getParentOfLastLayout();
            if (lastLayout instanceof RelativeLayout) {
                if (force) {
                    lastLayout = getParentOfLastLayout();
                } else {
                    if (lastLayout.getChildAt(lastLayout.getChildCount() - 1) instanceof LinearLayout && lastLayout.getChildAt(lastLayout.getChildCount() - 1) != vg) {
                        lastLayout = (ViewGroup) lastLayout.getChildAt(lastLayout.getChildCount() - 1);
                    } else {
                        lastLayout = (ViewGroup) lastLayout.getChildAt(0);
                    }
                    if (!(lastLayout instanceof LinearLayout)) {
                        for (int i = 0; i < lastLayout.getChildCount(); i++) {
                            if (lastLayout.getChildAt(i) instanceof LinearLayout) {
                                lastLayout = (LinearLayout) lastLayout.getChildAt(i);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 對"目前Layout"設定置中對齊
     * @return 目前Layout
     */
    public ViewGroup styliseCenter() {
        if (lastLayout instanceof LinearLayout) {
            ((LinearLayout) lastLayout).setGravity(Gravity.CENTER);
        }
        return lastLayout;
    }

    /**
     * 對"目前Layout"設定背景圖片
     * @param resourceID 背景圖片Resource ID
     * @return 目前Layout
     */
    public ViewGroup styliseBackground(int resourceID) {
        lastLayout.setBackgroundResource(resourceID);
        return lastLayout;
    }

    public ViewGroup styliseParams(LinearLayout.LayoutParams params) {
        ViewGroup hostParent = getParentOfLastLayout();
        ViewGroup parent = (ViewGroup) lastLayout.getParent();
        if (hostParent == parent) {
            parent.removeView(lastLayout);
            parent.addView(lastLayout, params);
        } else {
            for (int i = 0; i < hostParent.getChildCount(); i++) {
                if (hostParent.getChildAt(i) == parent) {
                    hostParent.removeViewAt(i);
                    hostParent.addView(parent, i, params);
                    break;
                }
            }
        }
        return lastLayout;
    }

    /**
     * 
     * @return
     */
    protected ViewGroup getParentOfLastLayout() {
        if (lastLayout.getParent() instanceof ScrollView || lastLayout.getParent() instanceof HorizontalScrollView) {
            return (ViewGroup) lastLayout.getParent().getParent();
        } else {
            return (ViewGroup) lastLayout.getParent();
        }
    }

    protected ViewGroup getDecoratedLastLayout() {
        if (lastLayout.getParent() instanceof ScrollView || lastLayout.getParent() instanceof HorizontalScrollView) {
            return (ViewGroup) lastLayout.getParent();
        } else {
            return (ViewGroup) lastLayout;
        }
    }

    public int getResourceID(String uri) {
        return context.getResources().getIdentifier(uri, null, context.getPackageName());
    }
}
