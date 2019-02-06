package com.eternitywall.btcclock.clocks;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

import org.json.JSONException;
import org.json.JSONObject;

public class NasaClock extends Clock {
    private static final String url = "https://api.nasa.gov/neo/rest/v1/stats?api_key=DEMO_KEY";
    private static final String title = "NASA NEO: near earth object count";
    private RequestQueue mRequestQueue;

    public NasaClock() {
        super(4, title, R.drawable.earth);
    }

    public void run(final Context context, final int appWidgetId) {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            final String count = String.valueOf(response.getLong("near_earth_object_count"));
                            NasaClock.this.updateListener.callback(context, appWidgetId, count, NasaClock.this.name, NasaClock.this.resource);
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.d(getClass().getName(), error.getLocalizedMessage());
                    }
                });
        getRequestQueue(context).add(jsonObjectRequest);
    }

    private RequestQueue getRequestQueue(final Context context) {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);
        return mRequestQueue;
    }
}
