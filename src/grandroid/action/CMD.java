/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.content.Intent;
import grandroid.DataAgent;
import grandroid.PhoneAgent;
import grandroid.view.Face;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rovers
 */
public class CMD {

    public static Face CURR_FRAME;
    public static boolean FIHISHED = false;
    public static Action DIAL = new Action("撥號") {

        @Override
        public boolean execute() {
            PhoneAgent.getInstance().dial();
            return true;
        }
    };    public static Action FINISH = new Action("結束") {

        @Override
        public boolean execute() {
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            FIHISHED = true;
            return true;
        }
    };
    public static Action PRINT = new Action("測試") {

        @Override
        public boolean execute() {
            if (CURR_FRAME != null) {
                CURR_FRAME.print(args[0]);
                return true;
            }
            return false;
        }
    };
    public static Action ALERT = new Action("警告") {

        @Override
        public boolean execute() {
            if (CURR_FRAME != null) {
                CURR_FRAME.alert(args[0], args[1]);
                return true;
            }
            return false;
        }
    };
    /**
     *
     */
    public static Action JUMP = new Action("換頁") {

        @Override
        public boolean execute() {
            try {
                Class c = Class.forName(args[0]);

                if (CURR_FRAME != null) {
                    DataAgent.getInstance().digest();
                    Intent intent = new Intent();
                    intent.setClass(CURR_FRAME, c);
                    CURR_FRAME.startActivity(intent);
                    CURR_FRAME.finish();
                    return true;
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CMD.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    };
}
