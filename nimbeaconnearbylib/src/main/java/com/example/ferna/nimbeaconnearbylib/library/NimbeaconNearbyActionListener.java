package com.example.ferna.nimbeaconnearbylib.library;

import com.example.ferna.nimbeaconnearbylib.actions.NimbeaconNearbyActionNotification;

public interface NimbeaconNearbyActionListener {
    /**
     * Event triggered when an action of type Notification should be launched.
     *
     * @param action Action to be performed.
     */
    void onNotificationAction(NimbeaconNearbyActionNotification action);
}