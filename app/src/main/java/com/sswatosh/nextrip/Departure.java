package com.sswatosh.nextrip;


import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

public class Departure {
    JSONObject departure;

    public Departure(JSONObject departure) {
        this.departure = departure;
    }

    public boolean getActual() throws JSONException {
        return departure.getBoolean("Actual");
    }

    public int getBlockNumber() throws JSONException {
        return departure.getInt("BlockNumber");
    }

    public String getDepartureText() throws JSONException {
        return departure.getString("DepartureText");
    }

    public DateTime getDepartureTime() throws JSONException {
        return Utils.NexTripJSONDateToDateTime(departure.getString("DepartureTime"));
    }

    public String getDescription() throws JSONException {
        return departure.getString("Description");
    }

    public String getGate() throws JSONException {
        return departure.getString("Gate");
    }

    public String getRoute() throws JSONException {
        return departure.getString("Route");
    }

    public String getRouteDirection() throws JSONException {
        return departure.getString("RouteDirection");
    }

    public String getTerminal() throws JSONException {
        return departure.getString("Terminal");
    }

    public int getVehicleHeading() throws JSONException {
        return departure.getInt("VehicleHeading");
    }

    public double getVehicleLatitude() throws JSONException {
        return departure.getDouble("VehicleLatitude");
    }

    public double getVehicleLongitude() throws JSONException {
        return departure.getDouble("VehicleLongitude");
    }
}
