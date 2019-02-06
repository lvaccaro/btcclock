package com.eternitywall.btcclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ClockWidgetConfigureActivity ClockWidgetConfigureActivity}
 */
public class ClockWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        Log.d("widget", "onUpdate");
        start(context);
    }

    @Override
    public void onDeleted(final Context context, final int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (final int id : appWidgetIds)
            ClockWidgetConfigureActivity.deleteIdPref(context, id);
    }

    @Override
    public void onEnabled(final Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("widget", "onEnabled");
        start(context);
    }

    @Override
    public void onDisabled(final Context context) {
        // Enter relevant functionality for when the last widget is disabled
        stop(context);
    }


    public static void start(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            SyncJobService.start(context);
        else
            UpdateTimeService.start(context);
    }

    public static void stop(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            SyncJobService.stop(context);
        else
            UpdateTimeService.stop(context);
    }

    public static void tick(final Context context, final Clock.UpdateListener listener) {
        final int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, ClockWidget.class));
        for (final int id : ids) {
            final Clock clock = ClockWidgetConfigureActivity.loadIdPref(context, id);
            Log.d("widget", String.valueOf(clock.id));
            clock.updateListener = listener;
            clock.run(context, id);
        }
    }

    public static void callback(final Context context, final int appWidgetId, final String time, String description, final int resource) {
        Log.d("widget", "callback");
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        views.setTextViewText(R.id.tvDescription, description);
        views.setTextViewText(R.id.tvTime, time);
        views.setImageViewResource(R.id.imageView, resource);

        final AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(context);
        mAppWidgetManager.updateAppWidget(appWidgetId,views);
    }
}

