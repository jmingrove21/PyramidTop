package com.example.app_user.Item_dir;


import java.io.Serializable;

public class User implements Serializable {
    private String user_id;
    private String user_password;
    private String user_name;

    private String user_img;
    private int user_serial;
    private transient String user_time;
    private int user_mileage;

    private double user_latitude=0;
    private double user_longitude=0;

    private String user_address;

    transient int user_price;
    public User(String user_id, String user_password, int user_serial, String user_name, int user_mileage, String user_img){
        this.user_img = user_img;
        this.user_id=user_id;
        this.user_password=user_password;
        this.user_serial = user_serial;
        this.user_name = user_name;
        this.user_mileage = user_mileage;
    }
    public User(String user_id){
        this.user_id=user_id;
    }
    public void setUser_info(String user_time, String user_price){
        this.user_time=user_time;
        this.user_price=Integer.parseInt(user_price);
    }

    public int getUser_serial() {
        return user_serial;
    }


    public String getUser_name() {
        return user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }


    public String getUser_id() {
        return user_id;
    }
    public String getUser_time() {
        return user_time;
    }

    public int getUser_price() {
        return user_price;
    }

    public String getUser_img() {
        return user_img;
    }
    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public int getUser_mileage() {
        return user_mileage;
    }

    public void setUser_mileage(int user_mileage) {
        this.user_mileage = user_mileage;
    }

    public void set_user_gps(double user_latitude, double user_longitude){
        this.user_latitude=user_latitude;
        this.user_longitude=user_longitude;
    }
    public double get_user_latitude(){
        return user_latitude;
    }
    public double get_user_longitude(){
        return user_longitude;
    }
}
