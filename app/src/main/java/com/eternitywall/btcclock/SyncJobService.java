package com.eternitywall.btcclock;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SyncJobService  extends JobService implements Clock.UpdateListener  {

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d(getPackageName(),"onStartJob");
        final Context context = getApplicationContext();
        ClockWidget.tick(context,this);
        start(context);
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters jobParameters) {
        Log.d(getPackageName(),"onStopJob");
        return true;
    }

    public static void stop(final Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
    }

    public static void start(final Context context) {
        final ComponentName serviceComponent = new ComponentName(context, SyncJobService.class);
        final JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(20 * 1000); // wait at least
        builder.setOverrideDeadline(30 *1000); // maximum delay
        //builder.setPeriodic(10*0000);
        //builder.setPersisted(true);
        builder.setRequiresDeviceIdle(false); // device should not be only idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        final JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        final int result = jobScheduler.schedule(builder.build());
        Log.d(SyncJobService.class.getName(),result == JobScheduler.RESULT_SUCCESS ? "success" : "failure");
    }

    @Override
    public void callback(final Context context, final int appWidgetId, final String time, final String description, final int resource) {
        ClockWidget.callback(context, appWidgetId, time, description, resource);
    }
}
