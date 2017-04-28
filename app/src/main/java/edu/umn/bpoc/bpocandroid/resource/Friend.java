package edu.umn.bpoc.bpocandroid.resource;

import java.io.Serializable;

public class Friend implements Serializable {
    public int Id;
    public String Name;
    public String Status;
    public double Distance;

    public Friend (int Id, String Name,String Status, double Distance) {
        this.Id = Id;
        this.Name = Name;
        this.Status = Status;
        this.Distance = Distance;
    }

    public Integer getId() {return this.Id;}
    public String getName() {return this.Name;}
    public double getDistance() {return this.Distance;}
}
