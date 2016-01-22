package com.example.ferna.nimbeaconnearby;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.ferna.nimbeaconnearbylib.library.NimbeaconInternalEventListener;
import com.example.ferna.nimbeaconnearbylib.library.NimbeaconNearbyManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.messages.Message;

public class MainActivity extends Activity {
Button play,stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        Log.i("Main Activity", "ON CREATE");
        final NimbeaconNearbyManager nimbeaconNearbyManager = NimbeaconNearbyManager.getInstance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nimbeaconNearbyManager.stop(getApplicationContext());
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        play = (Button) findViewById(R.id.buttonPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NimbeaconInternalEventListener mListener = new NimbeaconInternalEventListener() {
                    @Override
                    public void onBeaconFound(Message message) {
                        Log.i("ON BEACON FOUND", "FOUND");
                    }

                    @Override
                    public void onBeaconLost(Message message) {
                        Log.i("ON BEACON LOST", "LOST");
                    }
                };
                nimbeaconNearbyManager.init(getApplicationContext(), MainActivity.this, mListener);
            }
        });
        stop = (Button) findViewById(R.id.buttonStop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nimbeaconNearbyManager.stop(getApplicationContext());
            }
        });
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
