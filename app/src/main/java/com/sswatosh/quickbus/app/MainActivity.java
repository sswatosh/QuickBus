package com.sswatosh.quickbus.app;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.common.collect.Lists;
import com.sswatosh.nextrip.Departure;
import com.sswatosh.nextrip.NexTripObjectProvider;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String MORNING_BUS_1_TEXT = "morningBus1Text";
    private static final String MORNING_BUS_2_TEXT = "morningBus2Text";
    private static final String EVENING_BUS_1_TEXT = "eveningBus1Text";
    private static final String EVENING_BUS_2_TEXT = "eveningBus2Text";
    private static final String MORNING_BUS_1_COLOR = "morningBus1Color";
    private static final String MORNING_BUS_2_COLOR = "morningBus2Color";
    private static final String EVENING_BUS_1_COLOR = "eveningBus1Color";
    private static final String EVENING_BUS_2_COLOR = "eveningBus2Color";

    private static final String LAST_REFRESH_TIME = "lastRefreshTime";

    private static final int MIN_REFRESH_SECONDS = 31;
    private static final long MIN_REFRESH_MILLIS = MIN_REFRESH_SECONDS * 1000;

    private static final int REFRESH_MESSAGE = 0;
    
    private TextView morningBus1;
    private TextView morningBus2;
    private TextView eveningBus1;
    private TextView eveningBus2;

    private DateTime lastRefreshTime;

    private Handler refreshHandler;

    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        morningBus1 = (TextView) findViewById(R.id.morning_bus_1);
        morningBus2 = (TextView) findViewById(R.id.morning_bus_2);
        eveningBus1 = (TextView) findViewById(R.id.evening_bus_1);
        eveningBus2 = (TextView) findViewById(R.id.evening_bus_2);

        // Restore state if saved
        if (savedInstanceState != null) {
            morningBus1.setText(savedInstanceState.getString(MORNING_BUS_1_TEXT));
            morningBus1.setTextColor(savedInstanceState.getInt(MORNING_BUS_1_COLOR));
            morningBus2.setText(savedInstanceState.getString(MORNING_BUS_2_TEXT));
            morningBus2.setTextColor(savedInstanceState.getInt(MORNING_BUS_2_COLOR));
            eveningBus1.setText(savedInstanceState.getString(EVENING_BUS_1_TEXT));
            eveningBus1.setTextColor(savedInstanceState.getInt(EVENING_BUS_1_COLOR));
            eveningBus2.setText(savedInstanceState.getString(EVENING_BUS_2_TEXT));
            eveningBus2.setTextColor(savedInstanceState.getInt(EVENING_BUS_2_COLOR));

            lastRefreshTime = DateTime.parse(savedInstanceState.getString(LAST_REFRESH_TIME));
        }

        refreshHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (isActive) {
                    refreshUI();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        refreshUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        refreshHandler.removeMessages(REFRESH_MESSAGE);
    }

    private void refreshUI() {
        if (lastRefreshTime != null) {
            DateTime currentTime = DateTime.now();
            Duration sinceRefresh = new Duration(lastRefreshTime.toInstant(), currentTime.toInstant());
            if (sinceRefresh.getMillis() > MIN_REFRESH_MILLIS) {
                refreshBusTimes();
            } else {
                refreshHandler.sendEmptyMessageDelayed(REFRESH_MESSAGE, MIN_REFRESH_MILLIS - sinceRefresh.getMillis() + 500);
            }
        } else {
            refreshBusTimes();
        }
    }

    private void refreshBusTimes() {
        DateTime currentTime = DateTime.now();
        lastRefreshTime = currentTime;

        // get morning buses
        try {
            List<Departure> departures = NexTripObjectProvider.getDepartures("17", "2", "LAFR");

            if (departures.size() > 0) {
                Departure firstDeparture = departures.get(0);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    morningBus1.setText(String.valueOf(timeDifference.getMinutes()) + " minutes");
                    morningBus1.setTextColor(Color.GRAY);
                } else {
                    morningBus1.setText(firstDeparture.getDepartureText());
                    morningBus1.setTextColor(Color.RED);
                }
            }

            if (departures.size() > 1) {
                Departure firstDeparture = departures.get(1);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    morningBus2.setText(String.valueOf(timeDifference.getMinutes()) + " minutes");
                    morningBus2.setTextColor(Color.GRAY);
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
            List<String> disallowedTerminals = Lists.newArrayList("A");
            List<Departure> departures = NexTripObjectProvider.getDepartures("17", "3", "GRNI", null, disallowedTerminals);

            if (departures.size() > 0) {
                Departure firstDeparture = departures.get(0);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    eveningBus1.setText(String.valueOf(timeDifference.getMinutes()) + " min");
                    eveningBus1.setTextColor(Color.GRAY);
                } else {
                    eveningBus1.setText(firstDeparture.getDepartureText());
                    eveningBus1.setTextColor(Color.RED);
                }
            }

            if (departures.size() > 1) {
                Departure firstDeparture = departures.get(1);
                if (firstDeparture.getActual()) {
                    Period timeDifference = new Period(currentTime, firstDeparture.getDepartureTime());
                    eveningBus2.setText(String.valueOf(timeDifference.getMinutes()) + " min");
                    eveningBus2.setTextColor(Color.GRAY);
                } else {
                    eveningBus2.setText(firstDeparture.getDepartureText());
                    eveningBus2.setTextColor(Color.RED);
                }
            }
        } catch (JSONException e) {
            Log.e("MainActivity.onCreate", "Error getting evening buses.");
            e.printStackTrace();
        }

        refreshHandler.sendEmptyMessageDelayed(REFRESH_MESSAGE, MIN_REFRESH_MILLIS);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MORNING_BUS_1_TEXT, String.valueOf(morningBus1.getText()));
        outState.putInt(MORNING_BUS_1_COLOR, morningBus1.getCurrentTextColor());
        outState.putString(MORNING_BUS_2_TEXT, String.valueOf(morningBus2.getText()));
        outState.putInt(MORNING_BUS_2_COLOR, morningBus2.getCurrentTextColor());
        outState.putString(EVENING_BUS_1_TEXT, String.valueOf(eveningBus1.getText()));
        outState.putInt(EVENING_BUS_1_COLOR, eveningBus1.getCurrentTextColor());
        outState.putString(EVENING_BUS_2_TEXT, String.valueOf(eveningBus2.getText()));
        outState.putInt(EVENING_BUS_2_COLOR, eveningBus2.getCurrentTextColor());

        outState.putString(LAST_REFRESH_TIME, lastRefreshTime.toString());
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
