package com.eternitywall.btcclock;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public final class UpdateTimeService extends Service implements Clock.UpdateListener {
    static final String UPDATE_TIME = "com.eternitywall.btcclock.action.UPDATE_TIME";

    private final static IntentFilter mIntentFilter = new IntentFilter();

    static {
        mIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        mIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        mIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
    }

    public static void start(final Context context) {
        final Intent intent = new Intent(UpdateTimeService.UPDATE_TIME);
        intent.setPackage("com.eternitywall.btcclock");
        context.startService(intent);
    }
    public static void stop(final Context context) {
        final Intent service = new Intent(context.getApplicationContext(), UpdateTimeService.class);
        context.stopService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("widget", "onCreate");
        registerReceiver(mTimeChangedReceiver, mIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mTimeChangedReceiver);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("widget", "onStartCommand");

        if (intent != null) {
            if (UPDATE_TIME.equals(intent.getAction())) {
                final Context context = getApplicationContext();
                ClockWidget.tick(context, UpdateTimeService.this);
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(final Intent intent) {
        Log.d("widget", "onBind");
        return null;
    }

    private final BroadcastReceiver mTimeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d("widget", "mTimeChangedReceiver");
            ClockWidget.tick(context, UpdateTimeService.this);
        }
    };

    @Override
    public void callback(final Context context, final int appWidgetId, final String time, final String description, final int resource) {
        ClockWidget.callback(context, appWidgetId, time, description, resource);
    }
}