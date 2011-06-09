/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.dialog;

import android.content.DialogInterface;
import java.util.List;

/**
 *
 * @author Rovers
 */
public abstract class ObjectPickModel<T> extends CommandPickModel {

    protected List<T> list;

    public ObjectPickModel(String title, List<T> list, String... cmds) {
        super(title,cmds);
        this.list = list;
    }

    @Override
    public String[] getStringArray() {
        String[] typearr = new String[list.size() + extraCmds.length];
        for (int i = 0; i < list.size(); i++) {
            typearr[i] = getDisplayString(list.get(i));
        }
        System.arraycopy(extraCmds, 0, typearr, list.size(), extraCmds.length);
        return typearr;
    }

    @Override
    public void onClick(DialogInterface arg0, int index) {
        if (index < list.size()) {
            onPicked(index, list.get(index));
        } else {
            onCommand(index - list.size());
        }
    }

    protected abstract String getDisplayString(T obj);

    public abstract void onPicked(int index, T obj);

    public void onCommand(int cmdIndex) {
    }
}
