package com.eternitywall.btcclock;

import android.content.Context;

public class Clock {

    public int id;
    public String name;
    public int resource;
    public UpdateListener updateListener;

    public Clock(final int id, String name, final int resource, final UpdateListener updateListener) {
        this.id = id;
        this.name = name;
        this.resource = resource;
        this.updateListener = updateListener;
    }

    public Clock(final int id, final String name, final int resource) {
        this.id = id;
        this.name = name;
        this.resource = resource;
        this.updateListener = null;
    }

    public interface UpdateListener {
        // you can define any parameter as per your requirement
        void callback(final Context context, final int appWidgetId, final String time, final String description, final int resource);
    }

    public void run(final Context context, final int appWidgetId) {
    }
}
