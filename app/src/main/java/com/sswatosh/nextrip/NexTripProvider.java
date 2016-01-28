package com.sswatosh.nextrip;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NexTripProvider {

    private static final String baseURL = "http://svc.metrotransit.org/NexTrip/";
    private static final String jsonFormat = "?format=json";
    private static final String PROVIDERS = "Providers";

    public static TextValuePairArray getProviders() throws JSONException {
        String json = request(PROVIDERS);
        return new TextValuePairArray(new JSONArray(json));
    }

    private static String request(String requestPath) {
        try {
            String response = new NexTripRequest().execute(requestPath).get(10, TimeUnit.SECONDS);
            Log.d("NexTripProvider", response);
            return response;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.w("NexTripProvider", "NexTrip request failed.");
            e.printStackTrace();
            return null;
        }
    }

    private static class NexTripRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length > 1) {
                Log.w("NexTripRequest", "More than one param.");
            }

            String requestPath = params[0];
            try {
                URL url = new URL(baseURL + requestPath + jsonFormat);
                InputStream in = url.openStream();

                try {
                    return IOUtils.toString(in);
                } finally {
                    IOUtils.closeQuietly(in);
                }

            } catch (MalformedURLException e) {
                Log.e("NexTripProvider", "Bad NexTrip URL: " + baseURL + requestPath);
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                Log.e("NexTripProvider", "NexTrip connection failed.");
                e.printStackTrace();
                return null;
            }
        }
    }
}
