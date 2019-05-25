package com.example.app_user.Item_dir;

import android.graphics.Bitmap;

import com.example.app_user.Item_dir.Menu;
import com.example.app_user.Item_dir.MenuDesc;

import java.util.ArrayList;

public class Store {
    private ArrayList<Menu> menu_al=new ArrayList<>();


    private ArrayList<MenuDesc> menu_desc_al=new ArrayList<>();

    private Bitmap store_image;
    private int store_serial;
    private String store_name;
    private String store_branch_name;
    private String store_building_name;

    private String start_time;
    private String end_time;
    private String store_notice;
    private String store_restday;
    private String store_profile_img;
    private String store_main_type_name;
    private String store_sub_type_name;

    private String minimum_order_price;
    private String store_phone;
    private String store_address;
    private float distance;
    private int order_number;

    public Store(String serial, String name, String branch_name, String address, String phone, String distance){
        this.store_address = address;
        this.store_name  = name;
        this.store_branch_name = branch_name;
        this.store_serial = Integer.parseInt(serial);
        this.distance = Float.parseFloat(distance);
        this.store_phone = phone;
    }
    public Store(String serial, String name, String branch_name, String address, String store_phone, String minimum_price, String distance, String store_profile_img){
        this.store_serial = Integer.parseInt(serial);
        this.store_name=name;
        this.store_branch_name=branch_name;
        this.store_address=address;
        this.store_phone=store_phone;
        this.minimum_order_price=minimum_price;
        this.distance=Float.parseFloat(distance);
        this.store_profile_img=store_profile_img;
    }
    public Store(String serial, String name, String branch_name, String minimum_price,String store_profile_img){
        this.store_serial = Integer.parseInt(serial);
        this.store_name=name;
        this.store_branch_name=branch_name;
        this.minimum_order_price=minimum_price;
        this.store_profile_img=store_profile_img;
    }
    public String getStore_branch_name() {
        return store_branch_name;
    }

    public int getStore_serial() {
        return store_serial;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getStore_phone() {
        return store_phone;
    }

    public String getStore_address() {
        return store_address;
    }

    public String getStore_building_name() {
        return store_building_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getStore_notice() {
        return store_notice;
    }

    public String getStore_restday() {
        return store_restday;
    }

    public String getStore_profile_img() {
        return store_profile_img;
    }

    public ArrayList<Menu> getMenu_al() {
        return menu_al;
    }

    public Bitmap getStore_image() {
        return store_image;
    }

    public void setStore_image(Bitmap store_image) {
        this.store_image = store_image;
    }
    public String getMinimum_order_price() {
        return minimum_order_price;
    }


    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }


    public void set_store_spec( String store_building_name, String start_time, String end_time, String store_restday, String store_notice, String store_main_type_name, String store_sub_type_name){
        this.store_building_name=store_building_name;
        this.start_time=start_time;
        this.end_time=end_time;
        this.store_restday=store_restday;
        this.store_notice=store_notice;
        this.store_main_type_name=store_main_type_name;
        this.store_sub_type_name=store_sub_type_name;
    }
    public void set_store_spec(String store_serial, String store_building_name, String start_time, String end_time, String store_phone, String store_address,String store_restday, String store_notice){
        this.store_building_name=store_building_name;
        this.start_time=start_time;
        this.end_time=end_time;
        this.store_phone=store_phone;
        this.store_address=store_address;
        this.store_restday=store_restday;
        this.store_notice=store_notice;

    }
    public void setMenu_str(){
        menu_desc_al.clear();
        for(int i=0;i<menu_al.size();i++){
            for(int j=0;j<menu_al.get(i).getMenu_desc_al().size();j++){
               menu_desc_al.add(menu_al.get(i).getMenu_desc_al().get(j));
            }
        }
    }

    public ArrayList<String> getMenu_str(){
        ArrayList<String> menu_name=new ArrayList<>();
        for(int i=0;i<menu_al.size();i++){
            for(int j=0;j<menu_al.get(i).getMenu_desc_al().size();j++){
                menu_name.add(menu_al.get(i).getMenu_desc_al().get(j).getMenu_name());
            }
        }
        return menu_name;
    }
    public ArrayList<MenuDesc> getMenu_desc_al() {
        return menu_desc_al;
    }

}