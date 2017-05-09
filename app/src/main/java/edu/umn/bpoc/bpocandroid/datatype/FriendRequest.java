package edu.umn.bpoc.bpocandroid.datatype;

import android.content.Intent;

/**
 * Created by wznic on 4/7/2017.
 */

public class FriendRequest {
    private int strangerPendingId;
    private String strangerPendingName;

    public FriendRequest (int strangerPendingId, String strangerPendingName){
        this.strangerPendingId = strangerPendingId;
        this.strangerPendingName = strangerPendingName;
    }

    public String getStrangerPendingName() {return this.strangerPendingName;}
    public int getStrangerPendingId() {return this.strangerPendingId;}
}
