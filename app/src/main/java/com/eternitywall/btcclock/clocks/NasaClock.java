package com.eternitywall.btcclock.clocks;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NasaClock extends Clock {
    private static final String url = "https://api.nasa.gov/neo/rest/v1/stats?api_key=DEMO_KEY";
    private static final String title = "NASA NEO: near earth object count";

    public NasaClock() {
        super(4, title, R.drawable.earth);
    }


    public void run(final Context context, final int appWidgetId) {
        new Runnable() {
            @Override
            public void run() {

                final AsyncHttpClient client = new AsyncHttpClient();
                Log.d(getClass().getName(),"run");

                client.get(url,new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers, final JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d(getClass().getName(),response.toString());
                        try {
                            final String count = String.valueOf(response.getLong("near_earth_object_count"));
                            NasaClock.this.updateListener.callback(context, appWidgetId, count, NasaClock.this.name, NasaClock.this.resource);
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
