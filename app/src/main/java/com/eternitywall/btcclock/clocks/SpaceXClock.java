package com.eternitywall.btcclock.clocks;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpaceXClock extends Clock {
    private static final String url = "https://api.spacexdata.com/v1/launches/latest";
    private static final String title = "SPACEX: latest flight number";
    private RequestQueue mRequestQueue;

    public SpaceXClock() {
        super(7, title, R.drawable.earth);
    }

    public void run(final Context context, final int appWidgetId) {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(final JSONArray response) {
                        try {
                            final JSONObject json = response.getJSONObject(0);
                            final String count = String.valueOf(json.getLong("flight_number"));
                            SpaceXClock.this.updateListener.callback(context, appWidgetId, count, SpaceXClock.this.name, SpaceXClock.this.resource);
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
        getRequestQueue(context).add(jsonArrayRequest);
    }

    private RequestQueue getRequestQueue(final Context context) {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);
        return mRequestQueue;
    }
}
