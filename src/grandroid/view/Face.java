/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.app.Activity;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import grandroid.AppStatus;
import grandroid.MessageReceiver;
import grandroid.DataAgent;
import grandroid.action.Action;
import grandroid.action.AlertAction;
import grandroid.action.ContextAction;
import grandroid.action.NotifyAction;
import grandroid.action.ToastAction;
import grandroid.adapter.ItemClickable;
import grandroid.util.ImageUtil;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rovers
 */
public class Face extends Activity {

    protected ArrayList<Action> menuList;
    protected MessageReceiver bundledReceiver = null;
    protected int menuID;
    protected Menu menu;
    protected boolean disableLock = true;
    protected DataAgent dataAgent;
    protected ViewGroup lastLayout;
    protected ViewGroup mainLayout;
    protected int viewID = 1;

    public Face() {
        super();

    }

    public DataAgent getData() {
        if (dataAgent == null) {
            dataAgent = new DataAgent(this);
        }
        return dataAgent;
    }

    public View loadLayout(int resourceID) {
        LayoutInflater vi = this.getLayoutInflater();
        View vv = vi.inflate(resourceID, null, false);
        return vv;
        //layout.setBaselineAligned(disableLock)
        //layout.addView(vv, new LinearLayout.LayoutParams(layout.getLayoutParams().width, layout.getLayoutParams().height));
    }

    protected void keepViewData(View view) {
        getData().keep(view);
    }

    protected void keepViewData(View view, boolean autofill) {
        getData().keep(view, autofill);
    }

    protected void keepViewData(int viewID) {
        getData().keep(this, viewID);
    }

    protected void keepViewData(int viewID, boolean autofill) {
        getData().keep(this, viewID, autofill);
    }

    protected void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (menuList != null) {
            for (int i = 0; i < menuList.size(); i++) {
                menu.add(i, i, i, menuList.get(i).getActionName());
                //System.out.println("menuList.get(i).getActionName()=" + menuList.get(i).getActionName());
            }
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() < menuList.size()) {
                menuList.get(item.getItemId()).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void log(String msg) {
        Logger.getLogger(Face.class.getName()).log(Level.INFO, msg);
    }

    public void toast(String msg) {
        new ToastAction(this).setMessage(msg).execute();
    }

    public void alert(String title, String msg) {
        //System.out.println(msg);
        alert(title, msg, new Action().setActionName("OK"));
    }

    public void alert(String title, String msg, final Action actPositive) {
        alert(title, msg, actPositive, null);
    }

    public void alert(String title, String msg, final Action actPositive, final Action actNegative) {
        new AlertAction(this).setData(title, msg, actPositive, actNegative).execute();
    }

    public void notify(String title, String msg) {
        new NotifyAction(this).setData(title, msg).execute();
    }

    protected void setButtonEvent(int btnID, final Action act) {
        final View btn = findViewById(btnID);
        setButtonEvent(btn, act);
    }

    protected void setButtonEvent(View btn, final Action act) {

        btn.setOnClickListener(new View.OnClickListener()   {

            public void onClick(View view) {
                act.setSrc(view);
                act.execute();
            }
        });
    }

    protected void addMenu(final Action act) {
        if (menuList == null) {
            menuList = new ArrayList<Action>();
        }
        menuList.add(act);
        //menuActions.put(menuItemID, act);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        if (AppStatus.FIHISHED) {
            finish();
        } else {
            super.onRestart();
            if (bundledReceiver != null) {
                bundledReceiver.registerAllEvent(this);
            }
        }
    }

    @Override
    protected void onResume() {
        if (AppStatus.FIHISHED) {
            finish();
        } else {
            super.onResume();
//            if (disableLock) {
//                log("redisable Lock");
//                KeyguardManager km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//                km.newKeyguardLock("Grandroid").disableKeyguard();
//            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("Arcface onStop is called..");
        if (bundledReceiver != null) {
            this.unregisterReceiver(bundledReceiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataAgent != null) {
            dataAgent.digest();
        }
//        if (receiver != null) {
//            if (disableLock) {
//                log("reenable Lock");
//                KeyguardManager km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//                km.newKeyguardLock("Grandroid").reenableKeyguard();
//            }
//        }
    }

    protected void registerBundledAction(String event, ContextAction action) {
        if (bundledReceiver == null) {
            bundledReceiver = new MessageReceiver();
        }
        bundledReceiver.addEvent(event, action);
        this.registerReceiver(bundledReceiver, new IntentFilter(event));
    }

    protected void unregisterAllBundledAction() {
        if (bundledReceiver != null) {
            this.unregisterReceiver(bundledReceiver);
        }
    }

    public View add(View view) {
        lastLayout.addView(view);
        return view;
    }

    public View add(View view, LinearLayout.LayoutParams params) {
        lastLayout.addView(view, params);
        return view;
    }

    public TextView addTextView() {
        return addTextView("");
    }

    public TextView addTextView(String text) {
        TextView tv = new TextView(this);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv.setTextSize(20);
        tv.setText(text);
        lastLayout.addView(tv);
        return tv;
    }

    public EditText addEditText(String text) {
        EditText et = new EditText(this);
        et.setTextColor(Color.BLACK);
        //et.setBackgroundColor(Color.argb(0, 0, 0, 0));
        et.setTextSize(20);
        et.setText(text);
        lastLayout.addView(et);
        return et;
    }

    public Button addButton(String text) {
        Button btn = new Button(this);
        btn.setTextColor(Color.BLACK);
        btn.setTextSize(20);
        btn.setText(text);

        lastLayout.addView(btn, layWW(lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL ? 1 : 0));
        return btn;
    }

    public ImageButton addImageButton(int resourceID) {
        ImageButton btn = new ImageButton(this);
        btn.setBackgroundColor(Color.argb(0, 255, 255, 0));
        btn.setImageResource(resourceID);
        lastLayout.addView(btn, layWW(lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL ? 1 : 0));
        return btn;
    }

    public ImageButton addImageButton(String url) {
        ImageButton btn = new ImageButton(this);
        btn.setBackgroundColor(Color.argb(0, 255, 255, 0));
        btn.setImageBitmap(ImageUtil.loadBitmap(url));
        lastLayout.addView(btn, layWW(lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL ? 1 : 0));
        return btn;
    }

    public ListView addListView(final BaseAdapter adapter) {
        ListView lv = new ListView(this);
        lv.setBackgroundColor(Color.WHITE);
        lv.setCacheColorHint(Color.WHITE);
        lv.setAdapter(adapter);
        lastLayout.addView(lv, layFF());
        
        if(ItemClickable.class.isInstance(adapter)){
            lv.setOnItemClickListener(new OnItemClickListener(){

                public void onItemClick(AdapterView<?> parent, View view, int index, long arg3) {
                    ((ItemClickable)adapter).onClickItem(index, view, adapter.getItem(index));
                }
                
            });
        }
        
        return lv;
    }

    public LinearLayout.LayoutParams layFF() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    }

    public LinearLayout.LayoutParams layFW() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public LinearLayout.LayoutParams layWW(float weight) {
        if (weight > 0) {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        } else {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public LinearLayout createMainLayout() {
        LinearLayout ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.WHITE);
        ll.setOrientation(LinearLayout.VERTICAL);
        mainLayout = ll;
        lastLayout = ll;
        this.setContentView(ll);
        return ll;
    }

    public LinearLayout createRowLayout() {
        return createRowLayout(false);
    }

    public LinearLayout createRowLayout(boolean withScroll) {
        LinearLayout ll = new LinearLayout(this);
        //ll.setBackgroundColor(Color.WHITE);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        if (withScroll) {
            ScrollView sv = new ScrollView(this);
            sv.setScrollContainer(true);
            sv.setFocusable(true);
            sv.addView(ll);
            lastLayout.addView(sv, layFF());
        } else {
            if (lastLayout instanceof LinearLayout && ((LinearLayout) lastLayout).getOrientation() == LinearLayout.HORIZONTAL) {
                lastLayout.addView(ll, layWW(0));
            } else {
                lastLayout.addView(ll, layFW());
            }
        }
        lastLayout = ll;
        return ll;
    }

    public LinearLayout createColLayout() {
        return createColLayout(false);
    }

    public LinearLayout createColLayout(boolean withScroll) {
        LinearLayout ll = new LinearLayout(this);
        //ll.setBackgroundColor(Color.WHITE);
        ll.setOrientation(LinearLayout.VERTICAL);
        if (withScroll) {
            ScrollView sv = new ScrollView(this);
            sv.setScrollContainer(true);
            sv.setFocusable(true);
            sv.addView(ll);
            lastLayout.addView(sv, layFW());
        } else {
            lastLayout.addView(ll, layWW(1));
        }
        lastLayout = ll;
        return ll;
    }

    public void insertTopBanner(View view) {
        boolean isParentRelative = getParentOfLastLayout() instanceof RelativeLayout;
        ViewGroup parent = getParentOfLastLayout();
        parent.removeView(lastLayout);
        RelativeLayout rl;
        if (isParentRelative) {
            rl = (RelativeLayout) parent;
        } else {
            rl = new RelativeLayout(this);
            rl.setBackgroundColor(Color.WHITE);
        }

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
        if (!isParentRelative) {
            parent.addView(rl, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
    }

    public void insertBottomBanner(View view) {
        boolean isParentRelative = getParentOfLastLayout() instanceof RelativeLayout;
        ViewGroup parent = getParentOfLastLayout();
        parent.removeView(lastLayout);
        RelativeLayout rl;
        if (isParentRelative) {
            rl = (RelativeLayout) parent;
        } else {
            rl = new RelativeLayout(this);
            rl.setBackgroundColor(Color.WHITE);
        }

        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        view.setId(viewID);
        rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rl.addView(view, rllp);

        RelativeLayout.LayoutParams rllpMain = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        rllpMain.addRule(RelativeLayout.ABOVE, viewID++);
        if (rl.getChildCount() == 2) {
            rllpMain.addRule(RelativeLayout.BELOW, rl.getChildAt(0).getId());
        }
        rl.addView(lastLayout, rllpMain);
        if (!isParentRelative) {
            parent.addView(rl, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
    }

    public void escape() {
        if (lastLayout != mainLayout) {
            lastLayout = getParentOfLastLayout();
        }
    }

    public ViewGroup styliseCenter() {
        if (lastLayout instanceof LinearLayout) {
            ((LinearLayout) lastLayout).setGravity(Gravity.CENTER);
        }
        return lastLayout;
    }

    public ViewGroup styliseBackground(int resourceID) {
        lastLayout.setBackgroundResource(resourceID);
        return lastLayout;
    }

    protected ViewGroup getParentOfLastLayout() {
        if (lastLayout.getParent() instanceof ScrollView) {
            return (ViewGroup) lastLayout.getParent().getParent();
        } else {
            return (ViewGroup) lastLayout.getParent();
        }
    }
}
