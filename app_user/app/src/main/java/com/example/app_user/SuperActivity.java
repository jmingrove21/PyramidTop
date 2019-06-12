package com.example.app_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

public class SuperActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawer;

    public void setDrawer(){
        drawer= findViewById(R.id.drawer_layout);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(LoginLogoutInform.getLogin_flag()==1){
            switch (menuItem.getItemId()) {
                case R.id.old_olderlist:
                    getSupportActionBar().setTitle("지난 주문 내역");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Old_Orderlist()).commit();
                    break;
//                case R.id.menu_idoption:
//                    getSupportActionBar().setTitle("계정 설정");
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            new Profile()).commit();
//                    break;
                case R.id.menu_logout:
                    UtilSet.loginLogoutInform.setLogin_flag(0);
                    UtilSet.my_user=null;
                    UtilSet.delete_user_data();
                    Intent intent=new Intent(getApplicationContext(), FirstMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
            }
        }else{
            switch (menuItem.getItemId()) {
                case R.id.menu_register:
                    Intent register_intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivityForResult(register_intent, 101);
                    break;

                case R.id.menu_login:
                    Intent login_intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(login_intent, 101);
                    break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
