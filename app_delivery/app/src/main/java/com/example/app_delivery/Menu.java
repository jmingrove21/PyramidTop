package com.example.app_delivery;

import java.io.Serializable;

public class Menu implements Serializable {
    public String menu_name;
    public int menu_count;
    public int menu_price;

    public Menu(String menu_name, String menu_count, String menu_price){
        this.menu_name=menu_name;
        this.menu_count=Integer.parseInt(menu_count);
        this.menu_price=Integer.parseInt(menu_price);
    }
}
