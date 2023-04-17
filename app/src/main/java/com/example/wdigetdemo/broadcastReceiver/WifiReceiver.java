package com.example.wdigetdemo.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.wdigetdemo.widget.UploadUtils;
import com.example.wdigetdemo.works.TestWorker;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("geolo", "WifiReceiver - onReceive() -- 接收广播， intent.getAction ->" + intent.getAction());
        WorkManager.getInstance(context).enqueue(OneTimeWorkRequest.from(TestWorker.class));
        UploadUtils.saveActionTime(context,intent);
    }

}
