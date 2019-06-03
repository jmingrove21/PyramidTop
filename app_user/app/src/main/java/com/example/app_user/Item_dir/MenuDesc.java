package com.example.app_user.Item_dir;

import android.graphics.Bitmap;

public class MenuDesc {
    private Bitmap menu_image;
    private String menu_code;
    private String menu_name;
    private int menu_price;
    private String menu_img;
    private int menu_count;
    public MenuDesc(String menu_code, String menu_name, int menu_price, String menu_img){
        this.menu_code=menu_code;
        this.menu_name=menu_name;
        this.menu_price=menu_price;
        this.menu_img=menu_img;
    }
    public MenuDesc(String menu_name, int menu_count, int menu_price){
        this.menu_name=menu_name;
        this.menu_count=menu_count;
        this.menu_price=menu_price;
    }

    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }
    public int getMenu_price() {
        return menu_price;
    }

    public String getMenu_img() {
        return menu_img;
    }
    public Bitmap getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(Bitmap menu_image) {
        this.menu_image = menu_image;
    }

    public int getMenu_count() {
        return menu_count;
    }

}
