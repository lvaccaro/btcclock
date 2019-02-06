package com.eternitywall.btcclock.clocks;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SpaceXClock extends Clock {
    private static final String url = "https://api.spacexdata.com/v1/launches/latest";
    private static final String title = "SPACEX: latest flight number";

    public SpaceXClock() {
        super(7, title, R.drawable.earth);
    }

    public void run(final Context context, final int appWidgetId) {
        new Runnable() {
            @Override
            public void run() {

                final AsyncHttpClient client = new AsyncHttpClient();
                Log.d(getClass().getName(),"run");
                client.get(url, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers, final JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d(getClass().getName(),response.toString());
                        try {
                            final JSONObject json = response.getJSONObject(0);
                            final String count = String.valueOf(json.getLong("flight_number"));
                            SpaceXClock.this.updateListener.callback(context, appWidgetId, count, SpaceXClock.this.name, SpaceXClock.this.resource);
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(final int statusCode, final Header[] headers, final Throwable throwable, final JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        }.run();
    }
}
