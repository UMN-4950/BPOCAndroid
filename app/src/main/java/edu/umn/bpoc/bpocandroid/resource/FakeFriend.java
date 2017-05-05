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
    public List<Double> TravelPathLat = new ArrayList<Double>();
    public List<Double> TravelPathLong = new ArrayList<Double>();

    public FakeFriend(int id, String name, String status, double distance, List<Double>travelpathlat, List<Double>travelpathlong) {
        this.Id = id;
        this.Name = name;
        this.Status = status;
        this.Distance = distance;
        this.TravelPathLat = travelpathlat;
        this.TravelPathLong = travelpathlong;
    }

    public String getName() {return this.Name;}
    public double getDistance() {return this.Distance;}


}
