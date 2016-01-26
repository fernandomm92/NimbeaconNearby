package com.example.ferna.nimbeaconnearbylib.library;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ferna.nimbeaconnearbylib.R;
import com.example.ferna.nimbeaconnearbylib.actions.NimbeaconNearbyAction;
import com.example.ferna.nimbeaconnearbylib.actions.NimbeaconNearbyActionNotification;
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
    private NimbeaconNearbyManager nnM = NimbeaconNearbyManager.getInstance();
    private PendingIntent pendingIntent;
    private NimBeaconNearbyHelper mHelper = NimBeaconNearbyHelper.getInstance();
    private String actionType;

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

        if(intent==null) {
            Log.i("intent nulo", "Nulo");
            SharedPreferences prefs =
                   getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
            actionType = prefs.getString("actionType",null);

            mHelper = NimBeaconNearbyHelper.getInstance();
            mHelper.setNimbeaconActionType(NimbeaconNearbyAction.toMyEnum(actionType));
            mHelper.setaListener(aListener);
        }

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

    // Action to track notification dismissal
    public static final String ACTION_DISMISS =
            "BeaconService.ACTION_DISMISS";

    private NimbeaconNearbyActionListener aListener = new NimbeaconNearbyActionListener() {
        @Override
        public void onNotificationAction(NimbeaconNearbyActionNotification action) {
            Log.i("onNotificationAction","NOTIFICATION");
            Log.i("Action content ", action.getMessageContent());
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            String packageName = context.getPackageName();
            Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);
            Notification notification = new Notification.Builder(BeaconServiceInit.this).setContentTitle("Beacons Detected")
                    .setContentText(String.format(action.getId()))
                    .setSmallIcon(R.drawable.ic_stat_scan)
                    .setContentIntent(intent)
                            //.setDeleteIntent(delete)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(action.getId(),0, notification);
        }
    };
    /*final static String STOP_SERVICE_BROADCAST_KEY="StopServiceBroadcastKey";
    final static int RQS_STOP_SERVICE = 1;

    NotifyServiceReceiver notifyServiceReceiver;
    public class NotifyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            int rqs = arg1.getIntExtra(STOP_SERVICE_BROADCAST_KEY, 0);

            if (rqs == RQS_STOP_SERVICE){
                stopSelf();
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                        .cancelAll();
            }
        }
    }*/
}
