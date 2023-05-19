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
 * android.intent.action.TIME_SET
 * android.intent.action.TIMEZONE_CHANGED
 * android.intent.action.BOOT_COMPLETED
 */
public class RescheduleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("com/example/wdigetdemo/geolo", "ResheduleReceiver - onReceive() -- 接收广播， intent.getAction ->" + intent.getAction());
        WorkManager.getInstance(context).enqueue(OneTimeWorkRequest.from(TestWorker.class));
        UploadUtils.saveActionTime(context,intent);
    }

}
