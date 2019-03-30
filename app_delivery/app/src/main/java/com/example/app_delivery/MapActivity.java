package com.example.app_delivery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {
    TMapView tMapView;
    ArrayList alTmapPoint;
    private String id="delivery_1";
    private String key="31a0c8ab-6880-42ba-b6f2-18080fbe6070";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.tMapView = new TMapView(this);
        this.alTmapPoint=new ArrayList();
        tMapView.setSKTMapApiKey(key);
        set_MapView();
        request_delivery_list();

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
    public void request_delivery_list() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://54.180.102.7:80/get/delivery_manage.php");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "order");
                    jsonParam.put("delivery_id",id);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        JSONArray json_result_al= (JSONArray) jobj.get("data");
                        for(int i=0;i<json_result_al.length();i++){
                            alTmapPoint.add(new TMapPoint(Double.parseDouble(((JSONObject)json_result_al.get(i)).get("destination_x").toString()),Double.parseDouble(((JSONObject)json_result_al.get(i)).get("destination_y").toString())));
                        }
                        set_Marker_in_Map();
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
    }
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
