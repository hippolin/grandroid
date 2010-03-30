/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import grandroid.action.Action;
import grandroid.action.CMD;
import java.util.ArrayList;

/**
 *
 * @author Rovers
 */
public class Face extends Activity {

    protected ArrayList<Action> menulist;
    //protected HashMap<Integer, Action> menuActions;
    protected int menuID;
    protected Menu menu;

    public Face() {
        super();
        CMD.CURR_FRAME = this;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (menulist != null) {
            for (int i = 0; i < menulist.size(); i++) {
                menu.add(menulist.get(i).getActionName());
            }
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            for (int i = 0; i < menulist.size(); i++) {
                if (item.getTitle().equals(menulist.get(i).getActionName())) {
                    menulist.get(i).execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void print(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void alert(String title, String msg) {
        System.out.println(msg);
        alert(title, msg, new Action().setActionName("OK"));
    }

    public void alert(String title, String msg, final Action actPositive) {
        alert(title, msg, actPositive, null);
    }

    public void alert(String title, String msg, final Action actPositive, final Action actNegative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(title).setMessage(msg);
        if (actPositive != null) {
            builder.setPositiveButton(actPositive.getActionName(), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    actPositive.execute();
                }
            });
        }
        if (actNegative != null) {
            builder.setNegativeButton(actNegative.getActionName(), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    actNegative.execute();
                }
            });
        }
        builder.show();
    }

    protected void setButtonEvent(int btnID, final Action act) {
        final Button btn = (Button) findViewById(btnID);
        setButtonEvent(btn, act);
    }

    protected void setButtonEvent(Button btn, final Action act) {
        act.setSrc(btn);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                act.execute();
            }
        });
    }

    protected void addMenu(final Action act) {
        if (menulist == null) {
            menulist = new ArrayList<Action>();
        }
        menulist.add(act);
        //menuActions.put(menuItemID, act);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CMD.CURR_FRAME = this;
    }

    @Override
    protected void onRestart() {
        if (CMD.FIHISHED) {
            finish();
        } else {
            super.onRestart();
            CMD.CURR_FRAME = this;
        }
    }

    @Override
    protected void onResume() {
        if (CMD.FIHISHED) {
            finish();
        } else {
            super.onResume();
            CMD.CURR_FRAME = this;
        }
    }
}
