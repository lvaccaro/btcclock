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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;


public class BitcoinPriceClock extends Clock {
    private static final DateFormat formatter = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT);
    private static final int EVERY = 20;
    private static final String url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=USD";
    private static final String title = "BTC/USD last price from coinmarketcap.com";
    private RequestQueue mRequestQueue;
    private int current = -1;

    public BitcoinPriceClock() {
        super(8, title, R.drawable.bitcoin);
    }

    public void run(final Context context, final int appWidgetId) {
        current++;
        if(current != 0 && current < EVERY)
            return;
        current = 0;
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(final JSONArray response) {
                        try {
                            final JSONObject jsonObject = response.getJSONObject(0);
                            final DecimalFormat df = new DecimalFormat("#,###.##");
                            final String price = df.format( Double.valueOf(jsonObject.getString("price_usd")));
                            final String height = "$ " + price;
                            final Long lastUpdated = Long.parseLong( jsonObject.getString("last_updated") );
                            final Date date = new Date(lastUpdated*1000L);
                            final String desc = "Coinmarketcap @ " + formatter.format(date);
                            BitcoinPriceClock.this.updateListener.callback(context, appWidgetId, height, desc, BitcoinPriceClock.this.resource);
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
