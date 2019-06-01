package com.example.app_user.draw_dir;


import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.app_user.R;
import com.skt.Tmap.TMapView;

public class GpsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        TMapView tmapView = new TMapView(this);

        tmapView.setSKTMapApiKey("31a0c8ab-6880-42ba-b6f2-18080fbe6070");

        tmapView.setCompassMode(true);
        tmapView.setIconVisibility(true);
        tmapView.setZoomLevel(15);
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapView.setTrackingMode(true);
        tmapView.setSightVisible(true);
        relativeLayout.addView(tmapView);
        setContentView(relativeLayout);
    }

}
