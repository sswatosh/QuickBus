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

public class NexTripObjectProvider {

    private static final String baseURL = "http://svc.metrotransit.org/NexTrip/";
    private static final String jsonFormat = "?format=json";
    private static final String PROVIDERS = "Providers";
    private static final String STOPS = "Stops";
    private static final String ROUTES = "Routes";
    private static final String DIRECTIONS = "Directions";

    public static TextValuePairArray getProviders() throws JSONException {
        String json = request(PROVIDERS);
        return new TextValuePairArray(new JSONArray(json));
    }

    public static RouteArray getRoutes() throws JSONException {
        String json = request(ROUTES);
        return new RouteArray(new JSONArray(json));
    }

    public static TextValuePairArray getDirections(String route) throws JSONException {
        String json = request(DIRECTIONS, route);
        return new TextValuePairArray(new JSONArray(json));
    }

    public static TextValuePairArray getStops(String route, String direction) throws JSONException {
        String json = request(STOPS, route, direction);
        return new TextValuePairArray(new JSONArray(json));
    }


    private static String request(String... segments) {
        String path = "";
        for (int i = 0; i < segments.length - 1; i++) {
            path += segments[i] + "/";
        }
        path += segments[segments.length-1];

        try {
            String response = new NexTripRequest().execute(path).get(10, TimeUnit.SECONDS);
            Log.d("NexTripObjectProvider", response);
            return response;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.w("NexTripObjectProvider", "NexTrip request failed.");
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
                Log.e("NexTripObjectProvider", "Bad NexTrip URL: " + baseURL + requestPath);
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                Log.e("NexTripObjectProvider", "NexTrip connection failed.");
                e.printStackTrace();
                return null;
            }
        }
    }
}
