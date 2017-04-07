package edu.umn.bpoc.bpocandroid.resource;

import java.io.Serializable;

public class Friend implements Serializable {
    public int Id;
    public String Name;
    public String Status;
    public double Distance;

    public String getName() {return this.Name;}
    public double getDistance() {return this.Distance;}
}
