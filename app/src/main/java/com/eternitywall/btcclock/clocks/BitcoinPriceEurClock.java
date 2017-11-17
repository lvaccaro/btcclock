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

/**
 * Created by luca on 26/09/2017.
 */

public class BitcoinPriceEurClock extends Clock {
    private final static DateFormat formatter = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT);

    private final static int EVERY = 20;
    private static int current=-1;

    public BitcoinPriceEurClock() {
        super(9, "BTC/EUR last price from coinmarketcap.com", R.drawable.bitcoin);
    }

    String url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=EUR";

    public void run(final Context context, final int appWidgetId){
        current++;
        if(current!=0 && current < EVERY)
            return;
        current=0;

        new Runnable() {
            @Override
            public void run() {

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url,  new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            final JSONObject jsonObject = response.getJSONObject(0);

                            DecimalFormat df = new DecimalFormat("#,###.##");
                            String price = df.format( Double.valueOf(jsonObject.getString("price_eur")));

                            String height = "€ " + price;
                            Long lastUpdated = Long.parseLong( jsonObject.getString("last_updated") );

                            final Date date = new Date(lastUpdated*1000L);
                            String desc = "Coinmarketcap @ " + formatter.format(date);

                            BitcoinPriceEurClock.this.updateListener.callback(context, appWidgetId, height, desc, BitcoinPriceEurClock.this.resource);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });


            }
        }.run();
    }

}
