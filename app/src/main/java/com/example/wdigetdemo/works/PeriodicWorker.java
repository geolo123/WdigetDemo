package com.example.wdigetdemo.works;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.wdigetdemo.R;
import com.example.wdigetdemo.TimeUtil;
import com.example.wdigetdemo.widget.TestWidgetProvider;
import com.example.wdigetdemo.widget.UploadUtils;

/**
 * Author: clement
 * Create: 2022/7/22
 * Desc:
 */
public class PeriodicWorker extends Worker {

    public PeriodicWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.e("com/example/wdigetdemo/geolo", "PeriodicWorker -- 初始化 PeriodicWorker");
//        SharedPreferences sp = context.getSharedPreferences("geolo", Context.MODE_PRIVATE);
//        if (!sp.getBoolean("geolo", false)){
//            UploadUtils.myRegisterReceiver(context);
//            UploadUtils.myRegisterReceiverTimeTick(context);
//            sp.edit().putBoolean("geolo", true).apply();
//        }
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("com/example/wdigetdemo/geolo", "PeriodicWorker--doWork() -- 模拟耗时/网络请求操作");
        //模拟耗时/网络请求操作
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            String data = TimeUtil.long2String(System.currentTimeMillis(), TimeUtil.HOUR_MM_SS);
            SharedPreferences sp = getApplicationContext().getSharedPreferences("com/example/wdigetdemo/geolo", Context.MODE_PRIVATE);
            sp.edit().putString(PeriodicWorker.class.getSimpleName(), data).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 刷新widget
        UploadUtils.updateWidget(getApplicationContext(), R.layout.widget_layout, TestWidgetProvider.class);
        return Result.success();
    }
}
