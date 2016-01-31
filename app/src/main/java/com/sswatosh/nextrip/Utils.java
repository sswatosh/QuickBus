package com.sswatosh.nextrip;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Utils {

    public static DateTime NexTripJSONDateToDateTime(String jsonDate) {
        long millis = Long.parseLong(jsonDate.substring(6,19));
        int offsetDirection = (jsonDate.charAt(20) == '-') ? -1 : 1;
        int offsetHours = Integer.parseInt(jsonDate.substring(21,22));
        int offsetMinutes = Integer.parseInt(jsonDate.substring(23,24));
        int offsetMillis = (offsetHours * 60 + offsetMinutes) * 60 * 1000 * offsetDirection;
        return new DateTime(millis, DateTimeZone.forOffsetMillis(offsetMillis));
    }
}
