/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.dialog;

import android.content.DialogInterface;

/**
 *
 * @author Rovers
 */
public abstract class CommandPickModel implements DialogInterface.OnClickListener {

    protected String title;
    protected String[] extraCmds;

    public CommandPickModel(String title, String... extraCmds) {
        this.title = title;
        this.extraCmds = extraCmds;
    }

    public String[] getStringArray() {
        return extraCmds;
    }

    public String getTitle() {
        return title;
    }

    public void onClick(DialogInterface arg0, int index) {
        onCommand(index);
    }

    public abstract void onCommand(int cmdIndex);
}
