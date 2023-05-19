package com.example.wdigetdemo.job;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Configuration;

import com.example.wdigetdemo.R;
import com.example.wdigetdemo.TimeUtil;
import com.example.wdigetdemo.widget.TestWidgetProvider;
import com.example.wdigetdemo.widget.UploadUtils;

import java.util.Random;

public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";
    // Random number generator
    private final Random mGenerator = new Random();

    public MyJobService() {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(0, 1000);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        // The work that this service "does" is simply wait for a certain duration and finish
        // the job (on another thread).

        int max = params.getExtras().getInt("max", 6);  //something low so I know it didn't work.
        Log.wtf(TAG, "max is " + max);

        // Process work here...  we'll pretend by sleeping for 3 seconds.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }

        Toast.makeText(getApplicationContext(), "Job: number is " + mGenerator.nextInt(max), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Job: I'm working on something...");

        //since there seems to be threshold on recurring.  say 10 to 30 minutes, based on simple tests.
        //you could just reschedule the job here.  Then the time frame can be much shorter.
        //scheduleJob(getApplicationContext(),max, true);

        // Return true as there's more work to be done with this job.

        try {
            String data = TimeUtil.long2String(System.currentTimeMillis(), TimeUtil.HOUR_MM_SS);
            SharedPreferences sp = getApplicationContext().getSharedPreferences("com/example/wdigetdemo/geolo", Context.MODE_PRIVATE);
            sp.edit().putString(MyJobService.class.getSimpleName(), data).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 刷新widget
        UploadUtils.updateWidget(getApplicationContext(), R.layout.widget_layout, TestWidgetProvider.class);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Stop tracking these job parameters, as we've 'finished' executing.
        Log.i(TAG, "on stop job: " + params.getJobId());

        // Return false to drop the job.
        return false;
    }


    //This is a helper method to schedule the job.  It doesn't need to be declared here, but it
    //a good way to keep everything together.

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context, int max, boolean recurring) {

        ComponentName serviceComponent = new ComponentName(context, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        if (recurring) {
            builder.setPeriodic(15 * 10000); //only once every 15 seconds.
            //builder.setPersisted(true);  //will persist across reboots.
            //except this runs in about 10 to 30 minute intervals...  Likely a min threshold here.
            Log.wtf(TAG, "set recurring");
        } else {  //just set it for once, between 10 to 30 seconds from now.
            builder.setMinimumLatency(10 * 1000); // wait at least
            builder.setOverrideDeadline(30 * 1000); // maximum delay
            Log.wtf(TAG, "set once");
        }

        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        //builder.setRequiresBatteryNotLow(true);  //only when the batter is not low.  API 26+
        //set some data via a persistablebundle.
        PersistableBundle extras = new PersistableBundle();
        extras.putInt("max", max);
        builder.setExtras(extras);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }
    }

    // cancel all the jobs.
    public static void cancelJob(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.cancelAll();
        }
    }

}