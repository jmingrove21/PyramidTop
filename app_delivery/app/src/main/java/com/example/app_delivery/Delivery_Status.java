package com.example.app_delivery;

import java.io.Serializable;
import java.util.ArrayList;

public class Delivery_Status implements Serializable {
    public String totalDistance;
    public String totalTime;
    public  ArrayList<String> destination_point=new ArrayList<>();
    public ArrayList<String> destination_time=new ArrayList<>();
    public ArrayList<String> destination_distance=new ArrayList<>();

}
