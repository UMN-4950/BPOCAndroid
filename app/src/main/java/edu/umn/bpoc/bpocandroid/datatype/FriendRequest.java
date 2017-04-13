package edu.umn.bpoc.bpocandroid.datatype;

import android.content.Intent;

/**
 * Created by wznic on 4/7/2017.
 */

public class FriendRequest {
    private int strangerId;
    private String strangerName;

    public FriendRequest (int strangerId, String strangerName){
        this.strangerId = strangerId;
        this.strangerName = strangerName;
    }

    public String getStrangerName() {return this.strangerName;}
    public int getStrangerId() {return this.strangerId;}
}
