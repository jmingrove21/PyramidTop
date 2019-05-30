package com.example.app_user.Item_dir;

public class LoginLogoutInform {

    private static int login_flag=0;

    public LoginLogoutInform(int login_flag){
        this.login_flag = login_flag;
    }

    public static int getLogin_flag() {
        return login_flag;
    }

    public void setLogin_flag(int login_flag) {
        this.login_flag = login_flag;
    }
}
