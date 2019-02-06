package com.eternitywall.btcclock.clocks;

import android.content.Context;

import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

public class TimestampClock extends Clock {
    private static final String title = "UNIX Timestamp";

    public TimestampClock() {
        super(1, title, R.drawable.time);
    }

    public void run(final Context context, final int appWidgetId) {
        new Runnable() {
            @Override
            public void run() {
                final String time = String.valueOf(System.currentTimeMillis());
                final String description = TimestampClock.this.name;
                TimestampClock.this.updateListener.callback(context, appWidgetId, time, description, TimestampClock.this.resource);
            }
        }.run();
    }
}
