package com.example.app_delivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {
    TMapView tMapView;
    ArrayList alTmapPoint;
    private String id = "delivery_1";
    TMapData tmapdata=new TMapData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.tMapView = new TMapView(this);
        this.alTmapPoint = new ArrayList();
        tMapView.setSKTMapApiKey(UtilSet.tmap_key);
        set_MapView();

        if(getIntent().hasExtra("json")) {
            try{
                JSONObject mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
                get_destination(mJsonObject);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void set_MapView() {
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);
    }

    private void set_Marker_in_Map() {

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker3);
        final Bitmap bitmap_1 = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);

        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        // 마커 아이콘 지정
        markerItem1.setIcon(bitmap_1);// 마커 아이콘 지정
        // 마커의 좌표 지정
        markerItem1.setTMapPoint(new TMapPoint(37.279548, 127.043791));
        //지도에 마커 추가
        tMapView.addMarkerItem("markerItem0", markerItem1);

        for (int i = 0; i < alTmapPoint.size(); i++) {
            markerItem1 = new TMapMarkerItem();
            // 마커 아이콘 지정
            markerItem1.setIcon(bitmap);// 마커 아이콘 지정
            markerItem1.setName(String.valueOf(i));
            // 마커의 좌표 지정
            markerItem1.setTMapPoint((TMapPoint) alTmapPoint.get(i));
            markerItem1.setCanShowCallout(true);
            markerItem1.setCalloutTitle(String.valueOf(i));

            //지도에 마커 추가
            tMapView.addMarkerItem("markerItem" + (i + 1), markerItem1);

        }
        tMapView.setCenterPoint(127.043840, 37.278307);
        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(37.279548, 127.043791), (TMapPoint)alTmapPoint.get(0),  alTmapPoint, 0,
                new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine polyLine) {
                        polyLine.setLineColor(Color.BLUE);
                        polyLine.setLineWidth(8.0f);
                        tMapView.addTMapPath(polyLine);
                    }
                });

    }

    private void get_destination(JSONObject jobj) {
        try {
            JSONArray json_result_al = (JSONArray) jobj.get("user_order");
            for (int i = 0; i < json_result_al.length(); i++) {
                double lat_dest = Double.parseDouble(((JSONObject) json_result_al.get(i)).get("destination_lat").toString());
                double long_dest = Double.parseDouble(((JSONObject) json_result_al.get(i)).get("destination_long").toString());
                alTmapPoint.add(new TMapPoint(lat_dest, long_dest));
            }
            set_Marker_in_Map();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
