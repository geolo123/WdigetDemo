package com.example.wdigetdemo.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.wdigetdemo.R;
import com.example.wdigetdemo.TimeUtil;

public class UploadUtils {

    public static final String REFRESH_ACTION = "android.appwidget.action.REFRESH";
    public static final String REFRESH_ACTION2 = "android.appwidget.action.REFRESH2";
    public static final String REFRESH_ACTION3 = "android.appwidget.action.REFRESH3";

    public static void updateWidget(Context context, int layoutId, Class<?> cls) {
        Log.e("geolo", "UploadUtils--updateWidget() -- 通过远程对象修改textview");
        String data = TimeUtil.long2String(System.currentTimeMillis(), TimeUtil.HOUR_MM_SS);
        //只能通过远程对象来设置appwidget中的控件状态
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layoutId);
        //通过远程对象修改textview
        remoteViews.setTextViewText(R.id.tv_text, data);

        //获得appwidget管理实例，用于管理appwidget以便进行更新操作
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        //获得所有本程序创建的appwidget
        ComponentName componentName = new ComponentName(context.getApplicationContext(), cls);
        //更新appwidget
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    public static void myRegisterReceiver(Context context){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addAction("android.appwidget.action.APPWIDGET_UPDATE");
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        intentFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        intentFilter.addAction(REFRESH_ACTION);
        intentFilter.addAction(REFRESH_ACTION2);
        intentFilter.addAction(REFRESH_ACTION3);
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.ALARM_CHANGED");
        intentFilter.addAction("android.intent.action.ON_DAY_CHANGE");
        intentFilter.addAction("android.intent.action.WORLD_CITIES_CHANGED");
        if (Build.VERSION.SDK_INT >= 24) {
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        }
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");

        // context.registerReceiver(TestWidgetProvider.mBroadcast,intentFilter);
        context.registerReceiver(new TestWidgetProvider(), intentFilter);
    }

    public static void myRegisterReceiverTimeTick(Context context){
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        // context.registerReceiver(TestWidgetProvider.mBroadcast,intentFilter);
        context.registerReceiver(new TestWidgetProvider(), intentFilter);
    }
}
