package com.sswatosh.nextrip;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;

public class RouteArray implements Iterable<Route>{
    private JSONArray array;

    public RouteArray(JSONArray array) {
        this.array = array;
    }

    public Route get(int i) throws JSONException {
        return new Route(array.getJSONObject(i));
    }

    public int length() {
        return array.length();
    }

    @Override
    public Iterator<Route> iterator() {
        return new NexTripRouteArrayIterator(array);
    }

    private class NexTripRouteArrayIterator implements Iterator<Route> {
        private JSONArray array;
        private int pointer = 0;

        public NexTripRouteArrayIterator(JSONArray array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return pointer < array.length();
        }

        @Override
        public Route next() {
            Route route = null;
            try {
                route = new Route(array.getJSONObject(pointer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pointer++;
            return route;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
