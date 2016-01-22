package com.example.ferna.nimbeaconnearbylib.library;


import com.google.android.gms.nearby.messages.Message;

/**
 * Created by ferna on 20/01/2016.
 */
public interface NimbeaconInternalEventListener {
    void onBeaconFound(Message message);
    void onBeaconLost(Message message);
}
