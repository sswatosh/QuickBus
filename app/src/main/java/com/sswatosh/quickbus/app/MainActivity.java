package com.sswatosh.quickbus.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.sswatosh.nextrip.*;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalDateTime currentTime = LocalDateTime.now();

        // get morning buses
        try {
            DepartureArray departures = NexTripObjectProvider.getDepartures("17", "2", "LAFR");

            if (departures.length() > 0) {
                TextView morningBus1 = (TextView) findViewById(R.id.morning_bus_1);
                Departure firstDeparture = departures.get(0);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    morningBus1.setText(String.valueOf(timeDifference.getMinutes()) + " minutes");
                } else {
                    morningBus1.setText(firstDeparture.getDepartureText());
                    morningBus1.setTextColor(Color.RED);
                }
            }

            if (departures.length() > 1) {
                TextView morningBus2 = (TextView) findViewById(R.id.morning_bus_2);
                Departure firstDeparture = departures.get(1);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    morningBus2.setText(String.valueOf(timeDifference.getMinutes()) + " minutes");
                } else {
                    morningBus2.setText(firstDeparture.getDepartureText());
                    morningBus2.setTextColor(Color.RED);
                }
            }
        } catch (JSONException e) {
            Log.e("MainActivity.onCreate", "Error getting morning buses.");
            e.printStackTrace();
        }


        try {
            // get evening buses
            DepartureArray departures = NexTripObjectProvider.getDepartures("17", "3", "GRNI");
            List<Departure> actualDepartures = departures.getActualDepartures();

            if (departures.length() > 0) {
                TextView eveningBus1 = (TextView) findViewById(R.id.evening_bus_1);
                Departure firstDeparture = departures.get(0);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    eveningBus1.setText(String.valueOf(timeDifference.getMinutes()) + " minutes");
                } else {
                    eveningBus1.setText(firstDeparture.getDepartureText());
                    eveningBus1.setTextColor(Color.RED);
                }
            }

            if (departures.length() > 1) {
                TextView eveningBus2 = (TextView) findViewById(R.id.evening_bus_2);
                Departure firstDeparture = departures.get(1);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    eveningBus2.setText(String.valueOf(timeDifference.getMinutes()) + " minutes");
                } else {
                    eveningBus2.setText(firstDeparture.getDepartureText());
                    eveningBus2.setTextColor(Color.RED);
                }
            }
        } catch (JSONException e) {
            Log.e("MainActivity.onCreate", "Error getting evening buses.");
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
