package com.example.app_user;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.home_dir.MenuActivity;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

public class StaticActivity extends AppCompatActivity {
    public  static Context mContext;
     static Activity appCompatActivity;

    public void onCreate(){
        mContext = this.getApplicationContext();
    }

    public static Context getAppContext(){
        return mContext;
    }
    public static void goToActivity(Intent intent) {
        mContext.startActivity(intent);
    }

    public static void setmContext(Context c){
        mContext=c;
    }
}
