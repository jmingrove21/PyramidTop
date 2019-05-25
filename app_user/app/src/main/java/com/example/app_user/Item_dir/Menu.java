package com.example.app_user.Item_dir;

import java.util.ArrayList;

public class Menu {
    private ArrayList<MenuDesc> menu_desc_al=new ArrayList<>();


    private String menu_type_code;
    private String menu_type_name;

    public Menu(String menu_type_code, String menu_type_name){
        this.menu_type_code=menu_type_code;
        this.menu_type_name=menu_type_name;
    }

    public String getMenu_type_code() {
        return menu_type_code;
    }

    public void setMenu_type_code(String menu_type_code) {
        this.menu_type_code = menu_type_code;
    }

    public String getMenu_type_name() {
        return menu_type_name;
    }

    public void setMenu_type_name(String menu_type_name) {
        this.menu_type_name = menu_type_name;
    }

    public ArrayList<MenuDesc> getMenu_desc_al() {
        return menu_desc_al;
    }

    public void setMenu_desc_al(ArrayList<MenuDesc> menu_desc_al) {
        this.menu_desc_al = menu_desc_al;
    }


}
