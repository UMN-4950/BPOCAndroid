package edu.umn.bpoc.bpocandroid.resource;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public ArrayList<Friend> Friends;
    public ArrayList<Location> Locations;
    public ArrayList<Notification> Notifications;

    public int Id;
    public String GoogleId;
    public String Email;

    public String GivenName;
    public String FamilyName;
}
