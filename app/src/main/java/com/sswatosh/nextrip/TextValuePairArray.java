package com.sswatosh.nextrip;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;

public class TextValuePairArray implements Iterable<TextValuePair>{
    private JSONArray array;

    public TextValuePairArray(JSONArray array) {
        this.array = array;
    }

    public TextValuePair get(int i) throws JSONException {
        return new TextValuePair(array.getJSONObject(i));
    }

    public int length() {
        return array.length();
    }

    @Override
    public Iterator<TextValuePair> iterator() {
        return new TextValuePairArrayIterator(array);
    }

    private class TextValuePairArrayIterator implements Iterator<TextValuePair> {
        private JSONArray array;
        private int pointer = 0;

        public TextValuePairArrayIterator(JSONArray array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return pointer < array.length();
        }

        @Override
        public TextValuePair next() {
            TextValuePair pair = null;
            try {
                pair = new TextValuePair(array.getJSONObject(pointer));
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
