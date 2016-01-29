package com.sswatosh.nextrip;

import org.json.JSONException;
import org.json.JSONObject;


public class Route {
    JSONObject route;

    public Route(JSONObject route) {
        this.route = route;
    }

    public String getDescription() throws JSONException {
        return route.getString("Description");
    }

    public String getProviderID() throws JSONException {
        return route.getString("ProviderID");
    }

    public String getRoute() throws JSONException {
        return route.getString("Route");
    }
}
