package com.example.ferna.nimbeaconnearbylib.library;

/**
 * Created by ferna on 20/01/2016.
 */
public class NimBeaconNearbyHelper {
    /**
     * Singleton.
     */
    private static NimBeaconNearbyHelper sInstance = null;
    protected NimBeaconNearbyHelper() {
        // Exists only to defeat instantiation.
    }
    public static NimBeaconNearbyHelper getInstance() {
        if(sInstance == null) {
            sInstance = new NimBeaconNearbyHelper();
        }
        return sInstance;
    }

    private static NimbeaconInternalEventListener mListener;

    public void setmListener(NimbeaconInternalEventListener mListener){
        this.mListener = mListener;
    }
    public NimbeaconInternalEventListener getmListener(){
        return mListener;
    }
}
