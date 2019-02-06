package com.eternitywall.btcclock.clocks;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.eternitywall.btcclock.Clock;
import com.eternitywall.btcclock.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class BitcoinGithubClock extends Clock {
    private static final String url = "https://api.github.com/repos/bitcoin/bitcoin/commits/master";
    private static final String title = "BITCOIN: sha of the last commit on github";

    public BitcoinGithubClock() {
        super(5, title, R.drawable.bitcoin);
    }

    public void run(final Context context, final int appWidgetId) {
        new Runnable() {
            @Override
            public void run() {

                final AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("User-Agent","Awesome-Octocat-App");
                client.get(url,  new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers, final JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            final String height = response.getString("sha");
                            BitcoinGithubClock.this.updateListener.callback(context, appWidgetId, height, BitcoinGithubClock.this.name, BitcoinGithubClock.this.resource);
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
