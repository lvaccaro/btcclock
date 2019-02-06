package com.eternitywall.btcclock.clocks;

import android.content.Context;

import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class BitcoinPriceEurClock extends Clock {
    private static final DateFormat formatter = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT);
    private static final int EVERY = 20;
    private static final String url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=EUR";
    private static final String title = "BTC/EUR last price from coinmarketcap.com";
    private static int current = -1;

    public BitcoinPriceEurClock() {
        super(9, title, R.drawable.bitcoin);
    }

    public void run(final Context context, final int appWidgetId) {
        current++;
        if(current!=0 && current < EVERY)
            return;
        current=0;

        new Runnable() {
            @Override
            public void run() {

                final AsyncHttpClient client = new AsyncHttpClient();
                client.get(url,  new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers, final JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            final JSONObject jsonObject = response.getJSONObject(0);
                            final DecimalFormat df = new DecimalFormat("#,###.##");
                            final String price = df.format( Double.valueOf(jsonObject.getString("price_eur")));
                            final String height = "€ " + price;
                            final Long lastUpdated = Long.parseLong( jsonObject.getString("last_updated") );
                            final Date date = new Date(lastUpdated*1000L);
                            final String desc = "Coinmarketcap @ " + formatter.format(date);
                            BitcoinPriceEurClock.this.updateListener.callback(context, appWidgetId, height, desc, BitcoinPriceEurClock.this.resource);
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
