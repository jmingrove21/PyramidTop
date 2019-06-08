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
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Delivery_to_Store_Activity extends AppCompatActivity {
    String store_name;
    String store_address;
    String store_phone;
    double store_latitude;
    double store_longtite;
    int order_number;
    static TMapView tMapView;
    static boolean refresh_status=true;
    ArrayList<Item_UserInfo> best_destination=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_to_store);
        Intent intent = getIntent();

        store_name = intent.getStringExtra("store_name");
        store_address = intent.getStringExtra("store_address");
        store_phone = intent.getStringExtra("store_phone");
        store_latitude = intent.getDoubleExtra("store_latitude", 0.0);
        store_longtite = intent.getDoubleExtra("store_longitude", 0.0);
        order_number = intent.getIntExtra("order_number", 0);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(UtilSet.tmap_key);
        set_Tmap_Init();

        Log.d("좌표",String.valueOf(UtilSet.latitude));
        Log.d("좌표",String.valueOf(UtilSet.longitude));
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Tmap_async t_async = new Tmap_async();
                t_async.execute();

            }
        });
        thread.start();

        CircleImageView tmap_button = (CircleImageView) findViewById(R.id.go_to_tmap);
        TextView store_name_tx=(TextView) findViewById(R.id.store_name);
        TextView store_address_tx=(TextView) findViewById(R.id.store_address);
        Button start_button = (Button) findViewById(R.id.delivery_start_btn);
        store_name_tx.setText(store_name);
        store_address_tx.setText(store_address);
        final TMapTapi tMapTapi = new TMapTapi(this);

        tmap_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isTmapApp = tMapTapi.isTmapApplicationInstalled();
                if (isTmapApp == true) {
                    Delivery_to_Store_Activity.refresh_status=false;
                    tMapTapi.invokeNavigate("destination", new Float(store_longtite), new Float(store_latitude), 0, true);
                } else {
                    Toast.makeText(Delivery_to_Store_Activity.this, "TMap이 설치되어 있지 않습니다.", Toast.LENGTH_LONG);
                }
            }
        });

        start_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                delivery_start_event();
                Delivery_to_Store_Activity.refresh_status=false;
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
                    jsonParam.put("delivery_id", UtilSet.delivery_id);
                    Log.d("json",jsonParam.toString());
                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj = new JSONObject(jsonReply);
                        ArrayList<Item_UserInfo> oData=new ArrayList<>();
                        get_user_information(oData,jobj);
                        get_best_destination(oData);
//                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                        intent.putExtra("order_number", order_number);
//                        intent.putExtra("list", oData);
//                        startActivityForResult(intent, 101);
//                        finish();
                    } else {
                        Log.d("error", "Connect fail");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void set_Tmap_Init() {
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.layout_go_to_store);
        linearLayoutTmap.addView(tMapView);
        tMapView.setZoomLevel(13);

    }
    public void get_best_destination(ArrayList<Item_UserInfo> oData) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "11");
                    JSONArray jarray=case_destination(oData);
                    jsonParam.put("array",jarray);

                    Log.d("json",jsonParam.toString());
                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj = new JSONObject(jsonReply);
                        ArrayList<Item_UserInfo> oData=new ArrayList<>();
                        get_user_information(oData,jobj);
//                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                        intent.putExtra("order_number", order_number);
//                        intent.putExtra("list", oData);
//                        startActivityForResult(intent, 101);
//                        finish();
//                        best_destination=oData;
                    } else {
                        Log.d("error", "Connect fail");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public JSONArray case_destination(ArrayList<Item_UserInfo> oData){
        if(oData.size()==2){
            try{
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm", Locale.KOREA);

                JSONArray jarray=new JSONArray();

                JSONObject jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(0).destination_long);
                jobj_start.put("endY",oData.get(0).destination_lat);

                JSONArray jarray_via=new JSONArray();
                JSONObject jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(1).destination);
                jobj_via.put("viaX",oData.get(1).destination_long);
                jobj_via.put("viaY",oData.get(1).destination_lat);
                jarray_via.put(jobj_via);
                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);

                jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(1).destination_long);
                jobj_start.put("endY",oData.get(1).destination_lat);

                jarray_via=new JSONArray();
                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(0).destination);
                jobj_via.put("viaX",oData.get(0).destination_long);
                jobj_via.put("viaY",oData.get(0).destination_lat);
                jarray_via.put(jobj_via);

                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);

                return jarray;
            }catch(JSONException e){
                e.printStackTrace();
                return null;

            }


        }else if(oData.size()==3){
            try{
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");

                JSONArray jarray=new JSONArray();

                // delivery - 1 - 2 - 0
                JSONObject jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(0).destination_long);
                jobj_start.put("endY",oData.get(0).destination_lat);
                JSONArray jarray_via=new JSONArray();

                JSONObject jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(1).destination);
                jobj_via.put("viaX",oData.get(1).destination_long);
                jobj_via.put("viaY",oData.get(1).destination_lat);
                jarray_via.put(jobj_via);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","2");
                jobj_via.put("viaPointName",oData.get(2).destination);
                jobj_via.put("viaX",oData.get(2).destination_long);
                jobj_via.put("viaY",oData.get(2).destination_lat);
                jarray_via.put(jobj_via);

                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);

                // delivery - 2 - 1 - 0
                jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(0).destination_long);
                jobj_start.put("endY",oData.get(0).destination_lat);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(2).destination);
                jobj_via.put("viaX",oData.get(2).destination_long);
                jobj_via.put("viaY",oData.get(2).destination_lat);
                jarray_via.put(jobj_via);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","2");
                jobj_via.put("viaPointName",oData.get(1).destination);
                jobj_via.put("viaX",oData.get(1).destination_long);
                jobj_via.put("viaY",oData.get(1).destination_lat);
                jarray_via.put(jobj_via);

                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);

                // delivery - 0 - 2 - 1
                jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(1).destination_long);
                jobj_start.put("endY",oData.get(1).destination_lat);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(0).destination);
                jobj_via.put("viaX",oData.get(0).destination_long);
                jobj_via.put("viaY",oData.get(0).destination_lat);
                jarray_via.put(jobj_via);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","2");
                jobj_via.put("viaPointName",oData.get(2).destination);
                jobj_via.put("viaX",oData.get(2).destination_long);
                jobj_via.put("viaY",oData.get(2).destination_lat);
                jarray_via.put(jobj_via);

                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);

                // delivery - 2 - 0 - 1
                jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(1).destination_long);
                jobj_start.put("endY",oData.get(1).destination_lat);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(2).destination);
                jobj_via.put("viaX",oData.get(2).destination_long);
                jobj_via.put("viaY",oData.get(2).destination_lat);
                jarray_via.put(jobj_via);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","2");
                jobj_via.put("viaPointName",oData.get(0).destination);
                jobj_via.put("viaX",oData.get(0).destination_long);
                jobj_via.put("viaY",oData.get(0).destination_lat);
                jarray_via.put(jobj_via);

                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);

                // delivery - 0 - 1 - 2
                jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(2).destination_long);
                jobj_start.put("endY",oData.get(2).destination_lat);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(0).destination);
                jobj_via.put("viaX",oData.get(0).destination_long);
                jobj_via.put("viaY",oData.get(0).destination_lat);
                jarray_via.put(jobj_via);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","2");
                jobj_via.put("viaPointName",oData.get(1).destination);
                jobj_via.put("viaX",oData.get(1).destination_long);
                jobj_via.put("viaY",oData.get(1).destination_lat);
                jarray_via.put(jobj_via);

                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);

                // delivery - 1 - 0 - 2
                jobj_start=new JSONObject();
                jobj_start.put("reqCoordType","WGS84GEO");
                jobj_start.put("resCoordType","WGS84GEO");
                jobj_start.put("startName","start");
                jobj_start.put("startX",UtilSet.longitude);
                jobj_start.put("startY",UtilSet.latitude);
                jobj_start.put("startTime",sdf.format(date));
                jobj_start.put("endName","destination");
                jobj_start.put("endX",oData.get(2).destination_long);
                jobj_start.put("endY",oData.get(2).destination_lat);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","1");
                jobj_via.put("viaPointName",oData.get(1).destination);
                jobj_via.put("viaX",oData.get(1).destination_long);
                jobj_via.put("viaY",oData.get(1).destination_lat);
                jarray_via.put(jobj_via);

                jobj_via=new JSONObject();
                jobj_via.put("viaPointId","2");
                jobj_via.put("viaPointName",oData.get(0).destination);
                jobj_via.put("viaX",oData.get(0).destination_long);
                jobj_via.put("viaY",oData.get(0).destination_lat);
                jarray_via.put(jobj_via);

                jobj_start.put("viaPoints",jarray_via);
                jarray.put(jobj_start);
                return jarray;
            }catch(JSONException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    public void get_user_information(ArrayList<Item_UserInfo> oData, JSONObject jobj) {
        try {
            JSONArray json_result_al = (JSONArray) jobj.get("user_order");
            for (int i = 0; i < json_result_al.length(); i++) {
                Item_UserInfo iu = new Item_UserInfo();
                JSONObject jobj_user = (JSONObject) json_result_al.get(i);
                String user_name = jobj_user.get("user_name").toString();
                String user_serial = jobj_user.get("user_serial").toString();
                String destination = jobj_user.get("destination").toString();
                String user_phone = jobj_user.get("user_phone").toString();
                String destination_lat = jobj_user.get("destination_lat").toString();
                String destination_long = jobj_user.get("destination_long").toString();
                String total_price = jobj_user.get("user_total_price").toString();
                String total_price_credit = jobj_user.get("user_pay_price").toString();
                String pay_status = jobj_user.get("user_pay_status").toString();

                iu.set_ItemUserInfo(order_number, user_serial, user_name, user_phone, destination, total_price);
                iu.set_pay_init(total_price_credit,pay_status);
                iu.set_destination(destination_lat, destination_long);
                JSONArray menu_jarry = (JSONArray) jobj_user.get("menu");
                for (int j = 0; j < menu_jarry.length(); j++) {
                    String menu_name = ((JSONObject) menu_jarry.get(j)).get("menu_name").toString();
                    String menu_count = ((JSONObject) menu_jarry.get(j)).get("menu_count").toString();
                    String menu_price = ((JSONObject) menu_jarry.get(j)).get("menu_price").toString();
                    Menu m = new Menu(menu_name, menu_count, menu_price);
                    iu.al_menu.add(m);
                }
                oData.add(iu);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private class Tmap_async extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {

            Bitmap bitmap_delivery = BitmapFactory.decodeResource(getResources(), R.drawable.marker3);
            Bitmap bitmap_store = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);


            while (true) {
                TMapPoint tmap_start = new TMapPoint(UtilSet.latitude, UtilSet.longitude); // delivery 위치
                TMapPoint tmap_end = new TMapPoint(store_latitude, store_longtite); // store 위치
                TMapMarkerItem markerItem1 = new TMapMarkerItem();

                markerItem1.setIcon(bitmap_delivery); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tmap_start); // 마커의 좌표 지정
                markerItem1.setName("delivery"); // 마커의 타이틀 지정

                TMapMarkerItem markerItem2 = new TMapMarkerItem();
                markerItem2.setIcon(bitmap_store); // 마커 아이콘 지정
                markerItem2.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem2.setTMapPoint(tmap_end); // 마커의 좌표 지정
                markerItem2.setName("store"); // 마커의 타이틀 지정

                Delivery_to_Store_Activity.tMapView.setCenterPoint(UtilSet.longitude, UtilSet.latitude);

                Delivery_to_Store_Activity.tMapView.removeAllMarkerItem();
                Delivery_to_Store_Activity.tMapView.addMarkerItem("source", markerItem1); // 지도에 마커 추가
                Delivery_to_Store_Activity.tMapView.addMarkerItem("destination", markerItem2); // 지도에 마커 추가
                try {
                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tmap_start, tmap_end);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(8);
                    Delivery_to_Store_Activity.tMapView.refreshDrawableState();
                    Delivery_to_Store_Activity.tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                    UtilSet.set_GPS_value(UtilSet.lm, Delivery_to_Store_Activity.this);
                    Thread.sleep(5000);
                    if(Delivery_to_Store_Activity.refresh_status==false)
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }


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
