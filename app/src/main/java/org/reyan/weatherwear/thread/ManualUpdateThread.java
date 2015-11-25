package org.reyan.weatherwear.thread;

import android.util.Log;

import org.reyan.weatherwear.activity.MainActivity;
import org.reyan.weatherwear.service.UpdateService;

/**
 * Created by reyan on 11/4/15.
 */
public class ManualUpdateThread extends Thread {

    private MainActivity mainActivity;

    public ManualUpdateThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Log.d("ManualThread", "running");
        UpdateService.update(mainActivity);
    }

}
