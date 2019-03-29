package com.example.app_delivery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {
    TMapView tMapView;
    ArrayList alTmapPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.tMapView = new TMapView(this);
        this.alTmapPoint=new ArrayList();
        tMapView.setSKTMapApiKey("31a0c8ab-6880-42ba-b6f2-18080fbe6070");
        set_MapView();
        set_Marker_in_Map();
        //37.278307 127.043840
//        TMapMarkerItem markerItem1 = new TMapMarkerItem();
//
//        TMapPoint tMapPoint1 = new TMapPoint(37.278307, 127.043840); // SKT타워
//
//// 마커 아이콘
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);
//
//        markerItem1.setIcon(bitmap); // 마커 아이콘 지정
//        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
//        markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
//        markerItem1.setName("SKT타워"); // 마커의 타이틀 지정
//        tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
//
//        tMapView.setCenterPoint( 127.043840, 37.278307 );

    }
    private void set_MapView(){
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView( tMapView );
    }
    private void set_Marker_in_Map(){
        alTmapPoint.add(new TMapPoint(37.576016, 126.976867));//광화문
        alTmapPoint.add(new TMapPoint(37.570432, 126.992169));//종로3가
        alTmapPoint.add(new TMapPoint(37.570194, 126.983045));//종로5가

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker2);

        for(int i=0; i<alTmapPoint.size(); i++){
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            // 마커 아이콘 지정
            markerItem1.setIcon(bitmap);
            // 마커의 좌표 지정
            markerItem1.setTMapPoint((TMapPoint) alTmapPoint.get(i));
            //지도에 마커 추가
            tMapView.addMarkerItem("markerItem"+i, markerItem1);

        }
    }
}
