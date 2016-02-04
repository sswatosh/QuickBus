package com.sswatosh.nextrip;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public static List<TextValuePair> getProviders() throws JSONException {
        String json = request(PROVIDERS);
        return jsonToTextValuePairs(json);
    }

    public static List<Route> getRoutes() throws JSONException {
        String json = request(ROUTES);
        JSONArray jsonArray = new JSONArray(json);
        List<Route> routes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            routes.add(new Route(jsonArray.getJSONObject(i)));
        }
        return routes;
    }

    public static List<TextValuePair> getDirections(String route) throws JSONException {
        String json = request(DIRECTIONS, route);
        return jsonToTextValuePairs(json);
    }

    public static List<TextValuePair> getStops(String route, String direction) throws JSONException {
        String json = request(STOPS, route, direction);
        return jsonToTextValuePairs(json);
    }

    public static List<Departure> getDepartures(String route, String direction, String stop) throws JSONException {
        return getDepartures(route, direction, stop, null, null);
    }

    public static List<Departure> getDepartures(String route, String direction, String stop,
                                                @Nullable List<String> allowedTerminals, @Nullable List<String> disallowedTerminals) throws JSONException {
        String json = request(route, direction, stop);
        JSONArray jsonArray = new JSONArray(json);
        List<Departure> departures = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            departures.add(new Departure(jsonArray.getJSONObject(i)));
        }

        if (allowedTerminals != null) {
            Iterator<Departure> iterator = departures.iterator();
            while (iterator.hasNext()) {
                Departure departure = iterator.next();
                if (!allowedTerminals.contains(departure.getTerminal())) {
                    iterator.remove();
                    Log.d("getDepartures", "Ignoring bus with terminal: " + departure.getTerminal());
                }
            }
        }

        if (disallowedTerminals != null) {
            Iterator<Departure> iterator = departures.iterator();
            while (iterator.hasNext()) {
                Departure departure = iterator.next();
                if (disallowedTerminals.contains(departure.getTerminal())) {
                    iterator.remove();
                    Log.d("getDepartures", "Ignoring bus with terminal: " + departure.getTerminal());
                }
            }
        }

        return departures;
    }


    private static List<TextValuePair> jsonToTextValuePairs(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<TextValuePair> pairs = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            pairs.add(new TextValuePair(jsonArray.getJSONObject(i)));
        }
        return pairs;
    }


    private static String request(String... segments) {
        String path = "";
        for (int i = 0; i < segments.length - 1; i++) {
            path += segments[i] + "/";
        }
        path += segments[segments.length-1];

        try {
            String response = new NexTripRequest().execute(path).get(10, TimeUnit.SECONDS);
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
