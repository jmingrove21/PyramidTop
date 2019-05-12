package com.example.app_delivery;

public class Item_UserInfo {
    public int user_serial;
    public String user_name;
    public String user_phone;
    public String destination;
    public double destination_lat;
    public double destination_long;

    public void set_ItemUserInfo(String user_serial, String user_name, String user_phone, String destination){
        this.user_serial=Integer.parseInt(user_serial);
        this.user_name=user_name;
        this.user_phone=user_phone;
        this.destination=destination;
    }

}
