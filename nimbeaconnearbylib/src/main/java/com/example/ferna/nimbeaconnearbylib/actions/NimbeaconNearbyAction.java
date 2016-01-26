package com.example.ferna.nimbeaconnearbylib.actions;

/**
 * Created by ferna on 25/01/2016.
 */
public abstract class NimbeaconNearbyAction {
    /** Enumerated with the possible action types. */
    public enum NimbeaconActionType {
        NOTIFICATION
    }

    /** Type of action. */
    protected NimbeaconActionType type;

    public NimbeaconActionType getType() {
        return type;
    }

    public void setType(NimbeaconActionType type) {
        this.type = type;
    }

    public NimbeaconNearbyAction(NimbeaconActionType type) {
        this.type = type;
    }

    public static NimbeaconActionType toMyEnum (String myEnumString) {
        try {
            return NimbeaconActionType.valueOf(myEnumString);
        } catch (Exception ex) {
            // For error cases
            return null;
        }
    }
}
