package com.example.ferna.nimbeaconnearbylib.actions;

/**
 * Created by ferna on 25/01/2016.
 */
public class NimbeaconNearbyActionNotification extends NimbeaconNearbyAction {

    /**
     * Custom Data.
     */
    private String id;
    private String messageContent;

    public NimbeaconNearbyActionNotification(NimbeaconActionType type, String id, String messageContent) {
        super(type);
        this.id = id;
        this.messageContent = messageContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }


}
