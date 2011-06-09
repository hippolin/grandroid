/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.service;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;

/**
 *
 * @author Rovers
 */
public abstract class HandlerService extends BasicService {

    protected Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("HandlerService");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(run);
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.postDelayed(run, 0);
    }
    private Runnable run = new Runnable() {

        public void run() {
            if (execute()) {
                //Log.d("dishpage", "stop 600 sec to redo");
                handler.postDelayed(run, getServiceInterval());
            } else {
                HandlerService.this.stopSelf();
            }
        }
    };

    /**
     * 
     * @return
     */
    protected abstract long getServiceInterval();

    /**
     * 
     * @return
     */
    protected abstract boolean execute();
}
