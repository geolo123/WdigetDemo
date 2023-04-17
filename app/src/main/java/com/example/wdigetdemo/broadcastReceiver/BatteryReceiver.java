package com.example.wdigetdemo.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.wdigetdemo.widget.UploadUtils;
import com.example.wdigetdemo.works.TestWorker;

/**
 * 需要动态注册的广播
 * <ul>
 *     <li> {@link Intent.ACTION_TIME_TICK}
 *     <li> {@link #ACTION_TIME_CHANGED}
 *     <li> {@link #ACTION_TIMEZONE_CHANGED}
 *     <li> {@link #ACTION_BOOT_COMPLETED}
 *     <li> {@link #ACTION_PACKAGE_ADDED}
 *     <li> {@link #ACTION_PACKAGE_CHANGED}
 *     <li> {@link #ACTION_PACKAGE_REMOVED}
 *     <li> {@link #ACTION_PACKAGE_RESTARTED}
 *     <li> {@link #ACTION_PACKAGE_DATA_CLEARED}
 *     <li> {@link #ACTION_PACKAGES_SUSPENDED}
 *     <li> {@link #ACTION_PACKAGES_UNSUSPENDED}
 *     <li> {@link #ACTION_UID_REMOVED}
 *     <li> {@link #ACTION_BATTERY_CHANGED}
 *     <li> {@link #ACTION_POWER_CONNECTED}
 *     <li> {@link #ACTION_POWER_DISCONNECTED}
 *     <li> {@link #ACTION_SHUTDOWN}
 * </ul>
 */
public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("geolo", "BatteryReceiver - onReceive() -- 接收广播， intent.getAction ->" + intent.getAction());
        WorkManager.getInstance(context).enqueue(OneTimeWorkRequest.from(TestWorker.class));
        UploadUtils.saveActionTime(context,intent);
    }

}
