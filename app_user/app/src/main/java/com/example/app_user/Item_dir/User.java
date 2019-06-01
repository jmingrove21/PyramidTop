package com.example.app_user.Item_dir;


import java.io.Serializable;

public class User implements Serializable {
    private String user_id;
    private transient String user_time;
    transient int user_price;
    private String user_password;
    public User(String user_id, String user_password){
        this.user_id=user_id;
        this.user_password=user_password;
    }
    public User(String user_id){
        this.user_id=user_id;
    }
    public void setUser_info(String user_time, String user_price){
        this.user_time=user_time;
        this.user_price=Integer.parseInt(user_price);
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


}
