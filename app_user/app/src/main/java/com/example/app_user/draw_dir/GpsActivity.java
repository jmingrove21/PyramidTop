package com.example.app_user.draw_dir;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.app_user.Item_dir.User;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.home_dir.MenuActivity;
import com.example.app_user.util_dir.LoginActivity;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapView;

import javax.crypto.spec.GCMParameterSpec;

public class GpsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        TMapView tmapView = new TMapView(this);
        tmapView.setSKTMapApiKey(UtilSet.key);

        tmapView.setCompassMode(true);
        tmapView.setIconVisibility(true);
        tmapView.setZoomLevel(15);
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapView.setLocationPoint(UtilSet.longitude,UtilSet.latitude);
        tmapView.setCenterPoint(UtilSet.longitude,UtilSet.latitude);
        tmapView.setTrackingMode(true);
        tmapView.setSightVisible(true);
        relativeLayout.addView(tmapView);
        setContentView(relativeLayout);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Tmap_async t_async = new Tmap_async();
                t_async.execute();

            }
        });
        thread.start();
    }

    private class Tmap_async extends AsyncTask<Integer, Integer, String>{

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                final String address = new TMapData().convertGpsToAddress(UtilSet.latitude,UtilSet.longitude);

                GpsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(GpsActivity.this, address, Toast.LENGTH_SHORT).show();
                        UtilSet.my_user.setUser_address(address);
                    }
                });

            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    } @Override
    public void onBackPressed() {
        Intent intent=new Intent(GpsActivity.this, FirstMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
