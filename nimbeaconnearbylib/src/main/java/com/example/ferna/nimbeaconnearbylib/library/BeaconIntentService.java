package com.example.ferna.nimbeaconnearbylib.library;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.example.ferna.nimbeaconnearbylib.actions.NimbeaconNearbyAction;
import com.example.ferna.nimbeaconnearbylib.actions.NimbeaconNearbyActionNotification;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

/**
 * Created by ferna on 20/01/2016.
 */
public class BeaconIntentService extends IntentService{
    private NimbeaconInternalEventListener mListener;
    private NimbeaconNearbyActionListener aListener;


    public BeaconIntentService() {
        super("My intent service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final NimBeaconNearbyHelper mHelper = NimBeaconNearbyHelper.getInstance();
        mListener = mHelper.getmListener();
        aListener = mHelper.getaListener();

        Nearby.Messages.handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.w("BeaconIntentServiceList", "FOUND Background Beacon: "
                        + new String(message.getContent()));
                //Call onBeaconFound.
                try {
                    mListener.onBeaconFound(message);
                }catch(Exception e){
                    Log.i("Message","Message "+message);
                    mHelper.dispatchAction(message);
                    Log.i("Exception ",e.toString());
                }
            }

            @Override
            public void onLost(Message message){
                Log.w("BeaconIntentServiceList", "LOST Background Beacon: "
                        + new String(message.getContent()));
                //Call onBeaconLost.
                try {
                    mListener.onBeaconLost(message);
                }catch(Exception e){
                    Log.i("Exception",e.toString());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.i("IntentService","On destroy");
        super.onDestroy();
    }
}
