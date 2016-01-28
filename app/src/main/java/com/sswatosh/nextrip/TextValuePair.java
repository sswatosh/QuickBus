package com.sswatosh.nextrip;


import org.json.JSONException;
import org.json.JSONObject;

public class TextValuePair {
    JSONObject pair;

    public TextValuePair(JSONObject pair) {
        this.pair = pair;
    }

    public String getText() throws JSONException {
        return pair.getString("Text");
    }

    public String getValue() throws JSONException {
        return pair.getString("Value");
    }

}
