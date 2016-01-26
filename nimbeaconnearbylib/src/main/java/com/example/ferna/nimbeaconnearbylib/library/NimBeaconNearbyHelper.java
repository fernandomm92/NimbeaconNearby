package com.example.ferna.nimbeaconnearbylib.library;

import android.util.Log;

import com.example.ferna.nimbeaconnearbylib.actions.NimbeaconNearbyAction;
import com.example.ferna.nimbeaconnearbylib.actions.NimbeaconNearbyActionNotification;
import com.google.android.gms.nearby.messages.Message;

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

    private static NimbeaconNearbyActionListener aListener = null;

    public void setaListener(NimbeaconNearbyActionListener aListener){
        this.aListener = aListener;
    }
    public NimbeaconNearbyActionListener getaListener(){
        return aListener;
    }

    private static NimbeaconNearbyAction.NimbeaconActionType nimbeaconActionType = null;

    public NimbeaconNearbyAction.NimbeaconActionType getNimbeaconActionType() {

        return nimbeaconActionType;
    }

    public void setNimbeaconActionType(NimbeaconNearbyAction.NimbeaconActionType nimbeaconActionType) {
        //Log.i("set type",nimbeaconActionType.toString());
        this.nimbeaconActionType = nimbeaconActionType;
    }

    public void dispatchAction(Message message){//String beaconID,String messageContent){
        String beaconID = "000";
        String messageContent = message.getContent().toString();
        try {
            beaconID = message.toString().split("\\{id=")[1].split("\\}")[0];
        }catch(Exception e1){

        }
        Log.i("Dispatch Action", "Dispatch");
        if(nimbeaconActionType!=null) {
            Log.i(String.valueOf(nimbeaconActionType), "nimbeaconactiontype");
        }else{
            Log.i("nimbeaconActionType","null");
        }
        if(nimbeaconActionType==NimbeaconNearbyAction.NimbeaconActionType.NOTIFICATION){
            aListener.onNotificationAction(new NimbeaconNearbyActionNotification(NimbeaconNearbyAction.NimbeaconActionType.NOTIFICATION, beaconID, messageContent));
        }
    }
}
