package edu.umn.bpoc.bpocandroid.resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christopherkoshiol on 4/28/17.
 */

public class FakeFriend {
    public int Id;
    public String Name;
    public String Status;
    public double Distance;
    public List<Double> TravelPath = new ArrayList<Double>();

    public FakeFriend(int id, String name, String status, double distance, List<Double>travelpath) {
        this.Id = id;
        this.Name = name;
        this.Status = status;
        this.Distance = distance;
        this.TravelPath = travelpath;
    }

    public String getName() {return this.Name;}
    public double getDistance() {return this.Distance;}


}
