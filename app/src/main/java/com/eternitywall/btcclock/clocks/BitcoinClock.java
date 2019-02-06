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

public class BitcoinClock extends Clock {
    private static final String url = "https://blockstream.info/api/blocks";
    private static final String title = "BITCOIN: blockchain height";
    private RequestQueue mRequestQueue;

    public BitcoinClock() {
        super(2, title, R.drawable.bitcoin);
    }

    public void run(final Context context, final int appWidgetId) {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(final JSONArray response) {
                        try {
                            final JSONObject json = (JSONObject) response.get(0);
                            final String height = String.valueOf(json.getLong("height"));
                            final String hash = json.getString("id");
                            final String subHash = hash.substring(0, 24) + "…" + hash.substring(56);
                            BitcoinClock.this.updateListener.callback(context, appWidgetId, height, subHash, BitcoinClock.this.resource);
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
