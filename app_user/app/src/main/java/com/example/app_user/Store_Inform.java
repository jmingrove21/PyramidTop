package com.example.app_user;

public class Store_Inform {
    private String store_name;
    private String store_phone;
    private String store_branch_name;
    private String store_address_jibun;
    private String store_start_time;
    private String store_end_time;
    private String store_restday;
    private String store_notice;
    private String store_profile_img;
    private String store_main_type_name;
    private String store_sub_type_name;

    Store_Inform(String store_name, String store_phone, String store_branch_name, String store_address_jibun,
                 String store_start_time, String store_end_time, String store_restday,
                 String store_notice, String store_profile_img, String store_main_type_name, String store_sub_type_name){
        this.store_name = store_name;
        this.store_phone = store_phone;
        this.store_branch_name = store_branch_name;
        this.store_address_jibun = store_address_jibun;
        this.store_start_time = store_start_time;
        this.store_end_time = store_end_time;
        this.store_restday = store_restday;
        this.store_notice = store_notice;
        this.store_profile_img = store_profile_img;
        this.store_main_type_name = store_main_type_name;
        this.store_sub_type_name = store_sub_type_name;
    }


    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_phone() {
        return store_phone;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public String getStore_branch_name() {
        return store_branch_name;
    }

    public void setStore_branch_name(String store_branch_name) {
        this.store_branch_name = store_branch_name;
    }

    public String getStore_address_jibun() {
        return store_address_jibun;
    }

    public void setStore_address_jibun(String store_address_jibun) {
        this.store_address_jibun = store_address_jibun;
    }

    public String getStore_start_time() {
        return store_start_time;
    }

    public void setStore_start_time(String store_start_time) {
        this.store_start_time = store_start_time;
    }

    public String getStore_end_time() {
        return store_end_time;
    }

    public void setStore_end_time(String store_end_time) {
        this.store_end_time = store_end_time;
    }

    public String getStore_restday() {
        return store_restday;
    }

    public void setStore_restday(String store_restday) {
        this.store_restday = store_restday;
    }

    public String getStore_notice() {
        return store_notice;
    }

    public void setStore_notice(String store_notice) {
        this.store_notice = store_notice;
    }

    public String getStore_profile_img() {
        return store_profile_img;
    }

    public void setStore_profile_img(String store_profile_img) {
        this.store_profile_img = store_profile_img;
    }

    public String getStore_main_type_name() {
        return store_main_type_name;
    }

    public void setStore_main_type_name(String store_main_type_name) {
        this.store_main_type_name = store_main_type_name;
    }

    public String getStore_sub_type_name() {
        return store_sub_type_name;
    }

    public void setStore_sub_type_name(String store_sub_type_name) {
        this.store_sub_type_name = store_sub_type_name;
    }

}