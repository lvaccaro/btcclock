package com.eternitywall.btcclock;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.eternitywall.btcclock.adapters.ClockAdapter;
import com.eternitywall.btcclock.adapters.RadioAdapter;
import com.eternitywall.btcclock.clocks.StandardClock;

import java.util.Arrays;
import java.util.List;

/**
 * The configuration screen for the {@link ClockWidget ClockWidget} AppWidget.
 */
public class ClockWidgetConfigureActivity extends Activity implements RadioAdapter.OnItemClickListener{

    private static final String PREFS_NAME = "com.eternitywall.btcclock.ClockWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RecyclerView mRecyclerView;
    private ClockAdapter mClockAdapter;

    public ClockWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveIdPref(final Context context, int appWidgetId, Clock clock) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, clock.id);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Clock loadIdPref(final Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int id = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
        if (id == -1) {
            return new StandardClock();
        } else {
            return Clocks.get(id);
        }
    }

    static void deleteIdPref(final Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }


    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            setContentView(R.layout.activity_about);
            return;
        }
        setContentView(R.layout.clock_widget_configure);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Blockchain Clock");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        List<Clock> clocks = Arrays.asList(Clocks.get());
        mClockAdapter = new ClockAdapter(this, clocks);
        mClockAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mClockAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Clock clock = loadIdPref(this,mAppWidgetId);
        for(int i=0; i < clocks.size() ;i++){
            if(clocks.get(i).id == clock.id){
                mClockAdapter.mSelectedItem = i;
                mClockAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onItemClick(final View view, final int position) {
        Clock[] clocks = Clocks.get();
        for(int i=0; i < clocks.length ;i++){
            if(i == position){
                saveIdPref(this, mAppWidgetId, clocks[i]);
                invalidate();
            }
        }
    }

    public void invalidate(){
        Intent intent = new Intent(UpdateTimeService.UPDATE_TIME);
        intent.setPackage("com.eternitywall.btcclock");
        startService(intent);

        final Context context = ClockWidgetConfigureActivity.this;

        // It is the responsibility of the configuration activity to update the app widget
        //AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //ClockWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

}

