package com.example.ferna.nimbeaconnearbylib.library;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

/**
 * Created by ferna on 20/01/2016.
 */
public class BeaconServiceInit extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    Context context;
    private static final String TAG = BeaconServiceInit.class.getSimpleName();
    //private boolean mResolvingNearbyPermissionError = false;
    private NotificationManager mNotificationManager;
    private String key;
    private NimbeaconNearbyManager nnM = NimbeaconNearbyManager.getInstance();
    private PendingIntent pendingIntent;

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("On connected","On connected");
        pendingIntent = getPendingIntent();
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended: " + connectionSuspendedCauseToString(cause));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "connection to GoogleApiClient failed");
    }
    private static String connectionSuspendedCauseToString(int cause) {
        switch (cause) {
            case CAUSE_NETWORK_LOST:
                return "CAUSE_NETWORK_LOST";
            case CAUSE_SERVICE_DISCONNECTED:
                return "CAUSE_SERVICE_DISCONNECTED";
            default:
                return "CAUSE_UNKNOWN: " + cause;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        Log.i("BeaconServiceInit", "onStartCommand");
        context = getApplicationContext();
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
        //stopSelf();
        Log.i("stop self", "stop self");
        Log.d(TAG, "Background Scanning Service Destroyed…");
    }

    private void subscribe() {
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY).build();
        Nearby.Messages.subscribe(mGoogleApiClient, pendingIntent, options)
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "subscribed successfully");
                        } else {
                            Log.i(TAG, "could not subscribe");
                            nnM.handleUnsuccessfulNearbyResult(status);
                        }
                    }
                });
    }
    private void unsubscribe() {
        Log.i(TAG, "trying to unsubscribe");
        // Cannot proceed without a connected GoogleApiClient. Reconnect and execute the pending
        // task in onConnected().
        if (!mGoogleApiClient.isConnected()) {
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            Nearby.Messages.unsubscribe(mGoogleApiClient, pendingIntent)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Log.i(TAG, "unsubscribed successfully");
                            } else {
                                Log.i(TAG, "could not unsubscribe");
                                nnM.handleUnsuccessfulNearbyResult(status);
                            }
                        }
                    });
        }
    }
    private PendingIntent getPendingIntent() {
        return PendingIntent.getService(context, 0, getBackgroundSubscribeServiceIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent getBackgroundSubscribeServiceIntent() {
        return new Intent(context, BeaconIntentService.class);
    }
}
