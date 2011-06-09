/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import grandroid.AppStatus;
import grandroid.MessageReceiver;
import grandroid.DataAgent;
import grandroid.action.Action;
import grandroid.action.AlertAction;
import grandroid.action.ContextAction;
import grandroid.action.NotifyAction;
import grandroid.action.ToastAction;
import grandroid.dialog.CommandPickModel;
import grandroid.dialog.DateTimePickModel;
import java.util.ArrayList;

/**
 *
 * @author Rovers
 */
public class Face extends Activity {

    /**
     * 
     */
    protected ArrayList<Action> menuList;
    /**
     * 
     */
    protected MessageReceiver bundledReceiver = null;
    /**
     * 
     */
    protected int menuID;
    /**
     * 
     */
    protected Menu menu;

    /**
     * 
     */
    protected DataAgent dataAgent;
    protected DateTimePickModel model;

    /**
     * 
     */
    public Face() {
        super();
    }

    /**
     * 取得資料代理人，一般用途為保存view的資料，以及存取SharedPreference
     * @return 資料代理人
     */
    public DataAgent getData() {
        if (dataAgent == null) {
            dataAgent = new DataAgent(this);
        }
        return dataAgent;
    }

    /**
     * 載入以view為單位的Layout XML (一般是以Activity為單位)
     * @param resourceID
     * @return 具體化後的View物件
     */
    public View loadLayout(int resourceID) {
        LayoutInflater vi = this.getLayoutInflater();
        View vv = vi.inflate(resourceID, null, false);
        return vv;
        //layout.setBaselineAligned(disableLock)
        //layout.addView(vv, new LinearLayout.LayoutParams(layout.getLayoutParams().width, layout.getLayoutParams().height));
    }

    /**
     * 將view物件註冊為「需保存值資料」，同時載入前次的值
     * 該view物件應設定過tag
     * @param view
     */
    protected void keepViewData(View view) {
        getData().keep(view);
    }

    /**
     * 將view物件註冊為「需保存值資料」，同時載入前次的值
     * 該view物件應設定過tag
     * @param view
     * @param autofill 是否載入前次的值
     */
    protected void keepViewData(View view, boolean autofill) {
        getData().keep(view, autofill);
    }

    /**
     * 將view物件註冊為「需保存值資料」，同時載入前次的值
     * 該view物件應設定過tag
     * @param viewID
     */
    protected void keepViewData(int viewID) {
        getData().keep(this, viewID);
    }

    /**
     * 將view物件註冊為「需保存值資料」，同時載入前次的值
     * 該view物件應設定過tag
     * @param viewID
     * @param autofill 是否載入前次的值
     */
    protected void keepViewData(int viewID, boolean autofill) {
        getData().keep(this, viewID, autofill);
    }

    /**
     * 
     * @param menuID
     */
    protected void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    /**
     * 
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (menuList != null) {
            for (int i = 0; i < menuList.size(); i++) {
                if (menuList.get(i).getActionName().contains(",")) {
                    String[] menuNames = menuList.get(i).getActionName().split(",");
                    menu.add(0, i, i, menuNames[0]);
                    menu.getItem(i).setIcon(Integer.valueOf(menuNames[1]));
                } else {
                    menu.add(0, i, i, menuList.get(i).getActionName());
                }
                //System.out.println("menuList.get(i).getActionName()=" + menuList.get(i).getActionName());
            }
        }
        this.menu = menu;
        return true;
    }

    /**
     * 
     * @param item
     * @return
     */
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

    /**
     * Log message with info leverl
     * @param msg
     */
    public void logi(String msg) {
        Log.i("grandroid", msg);
    }

    /**
     * Log message with error leverl
     * @param msg
     */
    public void loge(String msg) {
        Log.e("grandroid", msg);
    }

    /**
     * 顯示Toast訊息
     * @param msg
     */
    public void toast(String msg) {
        new ToastAction(this).setMessage(msg).execute();
    }

    /**
     * 顯示帶有一個OK按鈕的Dialog
     * @param title
     * @param msg
     */
    public void alert(String title, String msg) {
        //System.out.println(msg);
        alert(title, msg, new Action().setActionName("OK"));
    }

    /**
     * 顯示帶有一個按鈕的Dialog，按鈕名稱為actPositive的name屬性
     * @param title
     * @param msg
     * @param actPositive
     */
    public void alert(String title, String msg, final Action actPositive) {
        alert(title, msg, actPositive, null);
    }

    /**
     * 顯示帶有兩個按鈕的Dialog，按鈕名稱為actPositive、actNegative的name屬性
     * @param title
     * @param msg
     * @param actPositive
     * @param actNegative
     */
    public void alert(String title, String msg, final Action actPositive, final Action actNegative) {
        new AlertAction(this).setData(title, msg, actPositive, actNegative).execute();
    }

    /**
     * 顯示訊息及標題於Notification Bar
     * @param title
     * @param msg
     */
    public void notify(String title, String msg) {
        new NotifyAction(this).setContent(title, msg).execute();
    }

    /**
     * 將按鈕設定為按下時觸發Action的execute方法
     * 只支援Button與ImageButton
     * @param btnID Resource ID
     * @param act
     */
    protected void setButtonEvent(int btnID, final Action act) {
        final View btn = findViewById(btnID);
        setButtonEvent(btn, act);
    }

    /**
     * 將按鈕設定為按下時觸發Action的execute方法
     * 只支援Button與ImageButton
     * @param btn
     * @param act
     */
    protected void setButtonEvent(View btn, final Action act) {

        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                act.setSrc(view);
                act.execute();
            }
        });
    }

    /**
     * 新增一個menu選項，menu的名字是act的name屬性 (按下手機menu鍵時跳出來的選單，即ContextMenu)
     * 只適合製作靜態的menu
     * @param act
     */
    protected void addMenu(final Action act) {
        if (menuList == null) {
            menuList = new ArrayList<Action>();
        }
        menuList.add(act);
        //menuActions.put(menuItemID, act);
    }

    protected void addMenu(final Action act, int icon) {
        if (menuList == null) {
            menuList = new ArrayList<Action>();
        }
        act.setActionName(act.getActionName() + "," + icon);
        menuList.add(act);
        //menuActions.put(menuItemID, act);
    }

    /**
     * 
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 
     */
    @Override
    protected void onResume() {
        super.onResume();
        AppStatus.ON_TOP = true;
        if (bundledReceiver != null) {
            bundledReceiver.registerAllEvent(this);
        }
//            if (disableLock) {
//                log("redisable Lock");
//                KeyguardManager km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//                km.newKeyguardLock("Grandroid").disableKeyguard();
//            }
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AppStatus.ON_TOP = true;
    }


    /**
     * 
     */
    @Override
    protected void onPause() {
        super.onPause();
        AppStatus.ON_TOP = false;
        if (dataAgent != null) {
            dataAgent.digest();
        }
        if (bundledReceiver != null) {
            this.unregisterReceiver(bundledReceiver);
        }
//        if (receiver != null) {
//            if (disableLock) {
//                log("reenable Lock");
//                KeyguardManager km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//                km.newKeyguardLock("Grandroid").reenableKeyguard();
//            }
//        }
    }

    /**
     * 向Android註冊欲處理的事件
     * 使用本機制需注意，只在頁面為目前使用者觀看的頁面時有效
     * 當本頁面發生onPouse事件後，即不再有效，直到onResume發生，又會重新有效
     * @param event
     * @param action
     */
    protected void registerBundledAction(String event, ContextAction action) {
        if (bundledReceiver == null) {
            bundledReceiver = new MessageReceiver();
        }
        bundledReceiver.addEvent(event, action);
        this.registerReceiver(bundledReceiver, new IntentFilter(event));
    }

    /**
     * 移除所有向Android註冊的事件
     */
    protected void unregisterAllBundledAction() {
        if (bundledReceiver != null) {
            this.unregisterReceiver(bundledReceiver);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return model.createDialog(this);
    }

    protected void pickDateTime(DateTimePickModel model) {
        this.model = model;
        showDialog(model.getMode());
    }

    public <T> void pickObject(CommandPickModel cpm) {
        new AlertDialog.Builder(this).setItems(cpm.getStringArray(), cpm).setTitle(cpm.getTitle()).show();
    }
}
