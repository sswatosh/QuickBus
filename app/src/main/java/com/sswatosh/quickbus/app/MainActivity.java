package com.sswatosh.quickbus.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.sswatosh.nextrip.NexTripObjectProvider;
import com.sswatosh.nextrip.TextValuePair;
import com.sswatosh.nextrip.TextValuePairArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mainText = (TextView) findViewById(R.id.main_text);

        String text = "";

        try {
            TextValuePairArray providers = NexTripObjectProvider.getProviders();
            for (TextValuePair pair : providers) {
                text = text + pair.getText() + " : " + pair.getValue() + "\n";
            }
        } catch (JSONException e) {
            Log.e("MainActivity.onCreate", "Had a bad time");
            e.printStackTrace();
        }

        if (text.equals("")) {
            text = "It didn't work.";
        }

        mainText.setText(text);
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
