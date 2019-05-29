package com.example.app_delivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Delivery_to_Store_Activity extends AppCompatActivity {
    String store_address;
    String store_phone;
    double store_latitude;
    double store_longtite;
    int order_number;
    TMapView tMapView;
    ArrayList alTmapPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_to_store);
        Intent intent = getIntent();

        store_address = intent.getStringExtra("store_address");
        store_phone = intent.getStringExtra("store_phone");
        store_latitude = intent.getDoubleExtra("store_latitude",0.0);
        store_longtite = intent.getDoubleExtra("store_longitude",0.0);
        order_number=intent.getIntExtra("order_number",0);
        this.tMapView = new TMapView(this);
        this.alTmapPoint = new ArrayList();

        tMapView.setSKTMapApiKey(UtilSet.tmap_key);
        set_Tmap_Init();

        Tmap_async t_async=new Tmap_async();
        UtilSet.set_GPS_value(UtilSet.lm,this);
        t_async.execute();
        Button button1 = (Button) findViewById(R.id.delivery_start_btn) ;
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                delivery_start_event();
            }
        });

    }
    public void delivery_start_event() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "departure");
                    jsonParam.put("order_number", order_number);

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        Intent intent=new Intent(getApplicationContext(),MapActivity.class);
                        intent.putExtra("order_number",order_number);
                        intent.putExtra("json", jobj.toString());
                        startActivityForResult(intent,101);
                        finish();
                    }else{
                        Log.d("error","Connect fail");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void set_Tmap_Init(){
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.layout_go_to_store);
        linearLayoutTmap.addView(tMapView);
    }
    private class Tmap_async extends AsyncTask<Integer,Integer,String>{

        @Override
        protected String doInBackground(Integer... integers) {
            Bitmap bitmap_delivery = BitmapFactory.decodeResource(getResources(), R.drawable.marker3);
            Bitmap bitmap_store = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);


            while (true) {
                TMapPoint tmap_start = new TMapPoint(UtilSet.latitude, UtilSet.longitude); // delivery 위치
                TMapPoint tmap_end= new TMapPoint(store_latitude, store_longtite); // store 위치
                TMapMarkerItem markerItem1 = new TMapMarkerItem();

                markerItem1.setIcon(bitmap_delivery); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint( tmap_start ); // 마커의 좌표 지정
                markerItem1.setName("delivery"); // 마커의 타이틀 지정

                TMapMarkerItem markerItem2 = new TMapMarkerItem();
                markerItem2.setIcon(bitmap_store); // 마커 아이콘 지정
                markerItem2.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem2.setTMapPoint( tmap_end ); // 마커의 좌표 지정
                markerItem2.setName("store"); // 마커의 타이틀 지정

                tMapView.setCenterPoint( UtilSet.longitude, UtilSet.latitude );

                tMapView.removeAllMarkerItem();
                tMapView.addMarkerItem("source", markerItem1); // 지도에 마커 추가
                tMapView.addMarkerItem("destination", markerItem2); // 지도에 마커 추가
                try {
                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tmap_start, tmap_end);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(8);
                    tMapView.refreshDrawableState();
                    tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                    UtilSet.set_GPS_value(UtilSet.lm,Delivery_to_Store_Activity.this);
                    Thread.sleep(5000);
                }catch(Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            // 마커 아이콘
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
