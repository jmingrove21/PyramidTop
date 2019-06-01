package com.example.app_user.util_dir;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.home_dir.FirstMainActivity;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(3000);
            Intent intent = new Intent(getApplicationContext(), FirstMainActivity.class);
            UtilSet.load_user_data();
            startActivityForResult(intent, 101);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
