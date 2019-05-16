package com.example.app_user;

import android.graphics.Bitmap;
import android.widget.Button;

public class MenuProductItem {

    private Button btn_choice;
    private String menu_inform;
    private String price_inform;
    private String menu_code;
    private Bitmap menu_image;
    private int order_number;

    public String getMenu_inform() {
        return menu_inform;
    }

    public void setMenu_inform(String menu_inform) {
        this.menu_inform = menu_inform;
    }

    public String getPrice_inform() {
        return price_inform;
    }

    public void setPrice_inform(String price_inform) {
        this.price_inform = price_inform;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public Button getChoice() {
        return btn_choice;
    }

    public void setChoice(Button choice) {
        this.btn_choice = choice;
    }

    public Bitmap getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(Bitmap menu_image) {
        this.menu_image = menu_image;
    }
    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }
}
