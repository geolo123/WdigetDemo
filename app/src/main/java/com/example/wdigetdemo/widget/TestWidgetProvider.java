package com.example.wdigetdemo.widget;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.wdigetdemo.R;
import com.example.wdigetdemo.TimeUtil;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Author: clement
 * Create: 2022/7/22
 * Desc:
 */
public class TestWidgetProvider extends AppWidgetProvider {

    //系统更新广播
    public static final String APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    //自定义的刷新广播


    //定期任务的name
    private static final String WORKER_NAME = "TestWorker";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("geolo", "onReceive() -- 接收主动点击刷新广播/系统刷新广播， intent.getAction ->" + intent.getAction());
        //接收主动点击刷新广播/系统刷新广播
        if (TextUtils.equals(intent.getAction(), UploadUtils.REFRESH_ACTION)
                || TextUtils.equals(intent.getAction(), UploadUtils.REFRESH_ACTION2)
                || TextUtils.equals(intent.getAction(), UploadUtils.REFRESH_ACTION3)
                || TextUtils.equals(intent.getAction(), APPWIDGET_UPDATE)
                || TextUtils.equals(intent.getAction(), Intent.ACTION_TIME_TICK)) {
            //执行一次任务
            WorkManager.getInstance(context).enqueue(OneTimeWorkRequest.from(TestWorker.class));

            String data = TimeUtil.long2String(System.currentTimeMillis(), TimeUtil.HOUR_MM_SS);
            SharedPreferences sp = context.getSharedPreferences("geolo", Context.MODE_PRIVATE);
            HashSet<String> setList = (HashSet<String>) sp.getStringSet("uploadTime", new HashSet<>());
            HashSet<String> newSetList = new HashSet<>(setList);
            newSetList.add(data + " --- " + intent.getAction());
            sp.edit().putStringSet("uploadTime", newSetList).apply();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.e("geolo", "onUpdate() -- 到达指定的更新时间或者当用户向桌面添加AppWidget时被调用,或更新widget时");
        //到达指定的更新时间或者当用户向桌面添加AppWidget时被调用,或更新widget时

        //点击事件
        Intent intent = new Intent();
        intent.setClass(context, TestWidgetProvider.class);
        intent.setAction(UploadUtils.REFRESH_ACTION);

        //设置pendingIntent
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        //Retrieve a PendingIntent that will perform a broadcast
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);


        //为刷新按钮绑定一个事件便于发送广播
        remoteViews.setOnClickPendingIntent(R.id.iv_refresh, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //删除一个AppWidget时调用
        Log.e("geolo", "onDeleted() -- 删除一个AppWidget时调用");
    }

    @Override
    public void onEnabled(Context context) {
        Log.e("geolo", "onEnabled() -- AppWidget的实例第一次被创建时调用, 开始定时工作,间隔15分钟刷新一次");
        //AppWidget的实例第一次被创建时调用
        super.onEnabled(context);
        startAlarmManager(context);

        //开始定时工作,间隔15分钟刷新一次
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(TestWorker.class,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .setConstraints(new Constraints.Builder()
                        .setRequiresCharging(true)
                        .build())
                .build();
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORKER_NAME, ExistingPeriodicWorkPolicy.REPLACE, workRequest);
    }

    @Override
    public void onDisabled(Context context) {
        Log.e("geolo", "onDisabled() -- 删除一个AppWidget时调用");
        //删除一个AppWidget时调用
        super.onDisabled(context);
        //停止任务
        WorkManager.getInstance(context).cancelUniqueWork(WORKER_NAME);
    }


    private void startAlarmManager(Context context) {
        {
            Intent intent = new Intent();
            intent.setClass(context, TestWidgetProvider.class);
            intent.setAction(UploadUtils.REFRESH_ACTION3);
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("'geolo'", 0);
            //设置pendingIntent
            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            }
            ((AlarmManager) context.getSystemService(ALARM_SERVICE)).setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.currentThreadTimeMillis(), 60000L, pendingIntent);
        }
    }


}
