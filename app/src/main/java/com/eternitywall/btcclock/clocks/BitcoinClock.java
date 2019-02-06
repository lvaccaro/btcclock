package com.eternitywall.btcclock.clocks;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class BitcoinClock extends Clock {
    private static final String url = "https://blockstream.info/api/blocks";
    private static final String title = "BITCOIN: blockchain height";

    public BitcoinClock() {
        super(2, title, R.drawable.bitcoin);
    }

    public void run(final Context context, final int appWidgetId) {
        new Runnable() {
            @Override
            public void run() {

                final AsyncHttpClient client = new AsyncHttpClient();
                client.get(url, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers, final JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            final JSONObject json = (JSONObject) response.get(0);
                            final String height = String.valueOf(json.getLong("height"));
                            final String hash = json.getString("id");
                            final String subHash = hash.substring(0,24) + "…" + hash.substring(56);
                            BitcoinClock.this.updateListener.callback(context, appWidgetId, height, subHash, BitcoinClock.this.resource);
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
