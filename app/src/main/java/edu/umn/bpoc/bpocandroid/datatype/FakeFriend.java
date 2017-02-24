package edu.umn.bpoc.bpocandroid.datatype;

import java.io.Serializable;

/**
 * Created by wznic on 2/17/2017.
 * fake friend data with name and distance, just for test.
 */

public class FakeFriend implements Serializable{
    private int id;
    private String name;
    private double distance;

    public FakeFriend (int id, String name, double distance){
        this.id = id;
        this.name = name;
        this.distance = distance;
    }

    public String getName (){return this.name;}
    public double getDistance () {return this.distance;}



}
