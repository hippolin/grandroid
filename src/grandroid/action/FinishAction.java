/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import grandroid.AppStatus;

/**
 *
 * @author Rovers
 */
public class FinishAction extends Action {

    /**
     * 
     * @return
     */
    @Override
    public boolean execute() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        return true;
    }
}
