package com.example.app_user.draw_dir;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.app_user.Item_dir.MapPoint;
import com.example.app_user.Item_dir.User;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.home_dir.MenuActivity;
import com.example.app_user.util_dir.LoginActivity;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import javax.crypto.spec.GCMParameterSpec;

public class GpsActivity extends Activity implements TMapGpsManager.onLocationChangedCallback{
    private EditText GPS_editText;
    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapData tmapdata = null;
    private TMapGpsManager tMapGpsManager = null;
    private TMapView tMapView = null;
    private static int mMarkerID;

    private ArrayList<TMapMarkerItem> tMapMarkerItems = new ArrayList<TMapMarkerItem>();
    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();
    private ArrayList<Bitmap> m_bitMap = new ArrayList<Bitmap>();
    //Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gps_pin);
    private String address;
    private Double lat = null;
    private Double lon = null;

    private Button gps_button;

    @Override
    public void onLocationChange(Location location){
        if(m_bTrackingMode){
            tMapView.setLocationPoint(location.getLongitude(),location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mContext = this;

        gps_button = (Button) findViewById(R.id.GPS_button);

        tmapdata = new TMapData();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.map_view);
        tMapView = new TMapView(this);

        linearLayout.addView(tMapView);
        tMapView.setSKTMapApiKey(UtilSet.key);

        showMarkerPoint();

        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setLocationPoint(UtilSet.longitude,UtilSet.latitude);
        tMapView.setCenterPoint(UtilSet.longitude,UtilSet.latitude);

        tMapGpsManager = new TMapGpsManager(GpsActivity.this);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(5);
        tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);

        tMapGpsManager.OpenGps();

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                lat = tMapMarkerItem.latitude;
                lon = tMapMarkerItem.longitude;

                tmapdata.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String s) {
                        address = s;
                    }
                });
                Toast.makeText(GpsActivity.this,"주소 : "+address,Toast.LENGTH_SHORT).show();
            }
        });

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

    public void showMarkerPoint(){
        for(int i=0;i<m_mapPoint.size();i++){
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongtitude());

            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;

            bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gps_pin);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gps_pin);

            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("수원");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gps_pin);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pmarker%d",mMarkerID++);

            tMapView.addMarkerItem(strID,item1);
            mArrayMarkerID.add(strID);
        }
    }

    public void GPS_search_address(View view){
        convertToAddress();
    }

    public void convertToAddress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주소 설정하기");

// 마커 아이콘


        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strData = input.getText().toString();


                TMapData tMapData = new TMapData();
                tMapData.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                        for(int i=0;i<arrayList.size();i++) {
                            TMapPOIItem item = (TMapPOIItem)arrayList.get(i);
                            TMapPoint tmp_TMapPoint = new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude());
                            m_tmapPoint.add(tmp_TMapPoint);
                            tMapMarkerItems.add(new TMapMarkerItem());
                            //m_tmapPoint.set(i,new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude()));
                            m_bitMap.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gps_pin));
                            Log.d("주소로 찾기", "POI Name: " +
                                    item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") +
                                    ", " + "Point: " + item.getPOIPoint().toString());
                        }

                        //Make Marker
                        //m_tmapPoint.size()
                        for(int i=0;i< m_tmapPoint.size();i++) {

                            tMapMarkerItems.get(i).setIcon(m_bitMap.get(i)); // 마커 아이콘 지정
                            tMapMarkerItems.get(i).setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                            tMapMarkerItems.get(i).setTMapPoint( m_tmapPoint.get(i) ); // 마커의 좌표 지정
                            tMapMarkerItems.get(i).setName("아주대"); // 마커의 타이틀 지정
                            tMapView.addMarkerItem("1" +  i, tMapMarkerItems.get(i)); // 지도에 마커 추가
                            tMapView.setCenterPoint( m_tmapPoint.get(i).getLongitude(),m_tmapPoint.get(i).getLatitude());
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("취소",     new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
