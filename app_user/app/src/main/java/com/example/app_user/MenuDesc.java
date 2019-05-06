package com.example.app_user;

public class MenuDesc {
    private String menu_code;
    private String menu_name;

    public MenuDesc(String menu_code, String menu_name){
        this.menu_code=menu_code;
        this.menu_name=menu_name;
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
}
