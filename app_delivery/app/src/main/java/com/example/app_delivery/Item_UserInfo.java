package com.example.app_delivery;

import java.util.ArrayList;

public class Item_UserInfo {
    public int order_number;
    public int user_serial;
    public String user_name;
    public String user_phone;
    public String destination;
    public double destination_lat;
    public double destination_long;
    public int delivery_status=1;
    public int total_price;

    public ArrayList<Menu> al_menu=new ArrayList<>();
    public void set_ItemUserInfo(int order_number, String user_serial, String user_name, String user_phone, String destination,String total_price){
        this.order_number=order_number;
        this.user_serial=Integer.parseInt(user_serial);
        this.user_name=user_name;
        this.user_phone=user_phone;
        this.destination=destination;
        this.total_price=Integer.parseInt(total_price);
    }

    public void set_destination(String destination_lat, String destination_long){
        this.destination_lat=Double.parseDouble(destination_lat);
        this.destination_long=Double.parseDouble(destination_long);
    }
}
