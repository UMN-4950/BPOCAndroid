package edu.umn.bpoc.bpocandroid.datatype;

/**
 * Created by wznic on 4/7/2017.
 */

public class FriendRequest {
    private int id;
    private String strangerName;

    public FriendRequest (int id, String strangerName){
        this.id = id;
        this.strangerName = strangerName;
    }

    public String getStrangerName() {return this.strangerName;}
}
