package com.example.app_user.draw_dir;

public class OldOrderPrduct {

    private String user_store_name_input;
    private int user_order_price_sum_input;
    private String user_order_time_input;
    private String user_store_branch_name_input;

    public String getUser_store_name_input() {
        return user_store_name_input;
    }

    public void setUser_store_name_input(String user_store_name_input) {
        this.user_store_name_input = user_store_name_input;
    }
    public String getUser_store_branch_name_input() {
        return user_store_branch_name_input;
    }

    public void setUser_store_branch_name_input(String user_store_branch_name_input) {
        if(user_store_branch_name_input.equals(null))
            this.user_store_branch_name_input="";
        else
            this.user_store_branch_name_input = user_store_branch_name_input;
    }

    public int getUser_order_price_sum_input() {
        return user_order_price_sum_input;
    }

    public void setUser_order_price_sum_input(int user_order_price_sum_input) {
        this.user_order_price_sum_input = user_order_price_sum_input;
    }

    public String getUser_order_time_input() {
        return user_order_time_input;
    }

    public void setUser_order_time_input(String user_order_time_input) {
        this.user_order_time_input = user_order_time_input;
    }
}
