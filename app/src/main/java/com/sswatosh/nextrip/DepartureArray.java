package com.sswatosh.nextrip;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DepartureArray implements Iterable<Departure> {
    private JSONArray array;

    public DepartureArray(JSONArray array) {
        this.array = array;
    }

    public Departure get(int i) throws JSONException {
        return new Departure(array.getJSONObject(i));
    }

    public List<Departure> getActualDepartures() throws JSONException {
        List<Departure> departures = new ArrayList<>();
        for (Departure departure : this) {
            if (departure.getActual()) {
                departures.add(departure);
            }
        }
        return departures;
    }

    public int length() {
        return array.length();
    }

    @Override
    public Iterator<Departure> iterator() {
        return new DepartureArrayIterator(array);
    }

    private class DepartureArrayIterator implements Iterator<Departure> {
        private JSONArray array;
        private int pointer = 0;

        public DepartureArrayIterator(JSONArray array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return pointer < array.length();
        }

        @Override
        public Departure next() {
            Departure pair = null;
            try {
                pair = new Departure(array.getJSONObject(pointer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pointer++;
            return pair;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
