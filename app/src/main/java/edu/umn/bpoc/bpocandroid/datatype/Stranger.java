package edu.umn.bpoc.bpocandroid.datatype;

/**
 * Created by wznic on 5/9/2017.
 */

public class Stranger {
    private int strangerId;
    private String strangerName;
    private String strangerStatus;

    public Stranger (int strangerId, String strangerName, String strangerStatus){
        this.strangerId = strangerId;
        this.strangerName = strangerName;
        this.strangerStatus = strangerStatus;
    }
    public void setStrangerId (int strangerId){this.strangerId = strangerId;}
    public void setStrangerName (String strangerName) {this.strangerName = strangerName;}
    public void setStrangerStatus (String strangerStatus) {this.strangerStatus = strangerStatus;}
    public Integer getStrangerId (){return this.strangerId;}
    public String getStrangerName () {return this.strangerName;}
    public String getStrangerStatus () {return this.strangerStatus;}
}
