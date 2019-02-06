package com.eternitywall.btcclock.clocks;

import android.content.Context;
import android.text.format.DateFormat;

import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

import java.util.Calendar;

public class StandardClock extends Clock {
    private static final String title = "Standard time";

    public StandardClock() {
        super(0, title, R.drawable.clock);
    }

    public void run(final Context context, final int appWidgetId) {
        new Runnable() {
            @Override
            public void run() {
                final Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                final CharSequence time = DateFormat.format("HH:mm", mCalendar);
                final CharSequence description = DateFormat.format("d MMM yyyy", mCalendar);
                StandardClock.this.updateListener.callback(context, appWidgetId, time.toString(), description.toString(), StandardClock.this.resource);
            }
        }.run();
    }
}
