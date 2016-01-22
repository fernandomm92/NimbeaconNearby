package com.example.ferna.nimbeaconnearbylib.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.messages.NearbyMessagesStatusCodes;

/**
 * Created by ferna on 20/01/2016.
 */
public class NimbeaconNearbyManager{
    private static final String TAG = "NimbeaconNearbyManager";
    private Activity mActivity;
    private Intent intentBeaconServiceInit = null;
    /**
     * Singleton.
     */
    private static NimbeaconNearbyManager sInstance = null;
    protected NimbeaconNearbyManager() {
        // Exists only to defeat instantiation.
    }
    public static NimbeaconNearbyManager getInstance() {
        if(sInstance == null) {
            sInstance = new NimbeaconNearbyManager();
        }
        return sInstance;
    }
    public void init(Context context,Activity activity,NimbeaconInternalEventListener mListener){
        Log.i("NearbyManager","init");
        mActivity = activity;
        NimBeaconNearbyHelper.getInstance().setmListener(mListener);
        intentBeaconServiceInit = new Intent(context, BeaconServiceInit.class);
        context.startService(intentBeaconServiceInit);
    }
    public void stop(Context context){
        context.stopService(new Intent(context, BeaconServiceInit.class));
    }
    private boolean mResolvingNearbyPermissionError = false;
    public void handleUnsuccessfulNearbyResult(Status status) {
        Log.i(TAG, "processing error, status = " + status);
        if (status.getStatusCode() == NearbyMessagesStatusCodes.APP_NOT_OPTED_IN) {
            if (!mResolvingNearbyPermissionError) {
                try {
                    mResolvingNearbyPermissionError = true;
                    status.startResolutionForResult(mActivity,
                            Constants.REQUEST_RESOLVE_ERROR);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (status.getStatusCode() == ConnectionResult.NETWORK_ERROR) {
                //Log.i("No connectivity, cannot proceed.","Fix in 'Settings' and try again.");
                Log.i("No connectivity","No connectivity");
            } else {
                // To keep things simple, pop a toast for all other error messages.
                Log.i("Unsuccessful: " + status.getStatusMessage(), "");
            }

        }
    }
}
