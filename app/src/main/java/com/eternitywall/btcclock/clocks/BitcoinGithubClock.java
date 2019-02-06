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


public class BitcoinGithubClock extends Clock {
    private static final String url = "https://api.github.com/repos/bitcoin/bitcoin/commits/master";
    private static final String title = "BITCOIN: sha of the last commit on github";
    private RequestQueue mRequestQueue;

    public BitcoinGithubClock() {
        super(5, title, R.drawable.bitcoin);
    }

    public void run(final Context context, final int appWidgetId) {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            final String height = response.getString("sha");
                            BitcoinGithubClock.this.updateListener.callback(context, appWidgetId, height, BitcoinGithubClock.this.name, BitcoinGithubClock.this.resource);
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
