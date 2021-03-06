package com.example.app_user.draw_dir;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_user.Item_dir.MapPoint;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.util_dir.BackPressCloseHandler;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;



public class GpsActivity extends Activity implements TMapGpsManager.onLocationChangedCallback, TMapView.OnLongClickListenerCallback {
    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapData tmapdata = null;
    private TMapView tMapView = null;
    BackPressCloseHandler backPressCloseHandler;
    private String address;
    private Double lat = null;
    private Double lon = null;

    private Thread thread;

    private TMapPoint first_TMapPoint;
    Bitmap mark_bitmap;

    TextView address_text;
    EditText detail_address_input;

    public static double latitude;
    public static double longitude;

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

        Log.d("longitude",""+UtilSet.my_user.get_user_longitude());
        Log.d("latitude",""+UtilSet.my_user.get_user_latitude());
        if(UtilSet.my_user.get_user_longitude()==0.0||UtilSet.my_user.get_user_latitude()==0.0){
            UtilSet.my_user.set_user_gps(UtilSet.latitude_gps,UtilSet.longitude_gps);
        }
        backPressCloseHandler = new BackPressCloseHandler(this);
        address_text = findViewById(R.id.address_text);
        detail_address_input = findViewById(R.id.detail_address_input);

        mContext = this;
        tmapdata = new TMapData();
        LinearLayout linearLayout = findViewById(R.id.map_view);
        tMapView = new TMapView(this);

        linearLayout.addView(tMapView);
        tMapView.setSKTMapApiKey(UtilSet.key);

        tMapView.setSightVisible(true);

        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(18);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setLocationPoint(UtilSet.my_user.get_user_longitude(),UtilSet.my_user.get_user_latitude());
        tMapView.setCenterPoint(UtilSet.my_user.get_user_longitude(),UtilSet.my_user.get_user_latitude(),true);

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

        mark_bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gps_pin);
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                first_TMapPoint = new TMapPoint(tMapPoint.getLatitude(),tMapPoint.getLongitude());

                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                if((first_TMapPoint.getLatitude()==tMapPoint.getLatitude()) && (first_TMapPoint.getLongitude()==tMapPoint.getLongitude())){

                    tMapView.setLocationPoint(tMapPoint.getLongitude(),tMapPoint.getLatitude());
                    tMapView.setCenterPoint(tMapPoint.getLongitude(),tMapPoint.getLatitude(),true);
                    latitude = tMapPoint.getLatitude();
                    longitude = tMapPoint.getLongitude();

                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Tmap_async t_async = new Tmap_async("move");
                            t_async.execute();
                        }
                    });
                    thread.start();
                }
                return false;
            }
        });

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Tmap_async t_async = new Tmap_async();
                t_async.execute();
            }
        });
        thread.start();
    }

    private class Tmap_async extends AsyncTask<Integer, Integer, String>{
        String check="default";
        Tmap_async(){

        }
        Tmap_async(String check){
            this.check=check;
        }
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                    if(check.equals("move")){
                        final String address = new TMapData().convertGpsToAddress(latitude,longitude);

                        GpsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                UtilSet.my_user.setUser_address(address);
                                address_text.setText(address);
                            }
                        });
                    }else  if(check.equals("gps")){
                        final String address = new TMapData().convertGpsToAddress(UtilSet.latitude_gps,UtilSet.longitude_gps);

                        GpsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(GpsActivity.this, address, Toast.LENGTH_SHORT).show();
                                UtilSet.my_user.setUser_address(address);
                                address_text.setText(address);
                            }
                        });
                    }else if(check.equals("default")){
                        final String address = new TMapData().convertGpsToAddress(UtilSet.my_user.get_user_latitude(),UtilSet.my_user.get_user_longitude());

                        GpsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //tMapView.setCenterPoint(UtilSet.latitude_gps,UtilSet.longitude_gps,true);
                                Toast.makeText(GpsActivity.this, address, Toast.LENGTH_SHORT).show();
                                UtilSet.my_user.setUser_address(address);
                                address_text.setText(address);
                            }
                        });
                    }

            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }  @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void GPS_current_position_track(View view){
        tMapView.setLocationPoint(UtilSet.longitude_gps,UtilSet.latitude_gps);
        tMapView.setCenterPoint(UtilSet.longitude_gps, UtilSet.latitude_gps,true);
        UtilSet.my_user.set_user_gps(UtilSet.latitude_gps,UtilSet.longitude_gps);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Tmap_async t_async = new Tmap_async("gps");
                t_async.execute();
            }
        });
        thread.start();
    }

    public void GPS_search_address(View view){
        convertToAddress();
    }

    public void convertToAddress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주소 설정하기");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strData = input.getText().toString();
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.gps_pin);


                TMapData tMapData = new TMapData();

                tMapData.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {

                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                        tMapView.removeAllMarkerItem();
                        for(int i=0;i<arrayList.size();i++) {
                            TMapPOIItem item = arrayList.get(i);
                            TMapPoint tmp_TMapPoint = new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude());
                            TMapMarkerItem markerItem = new TMapMarkerItem();
                            markerItem.setIcon(bitmap);
                            markerItem.setTMapPoint(tmp_TMapPoint);
                            tMapView.addMarkerItem(String.valueOf(i),markerItem);


                            Log.d("주소로 찾기", "POI Name: " +
                                    item.getPOIName() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") +
                                    ", " + "Point: " + item.getPOIPoint().toString());
                        }
                        tMapView.setCenterPoint(arrayList.get(0).getPOIPoint().getLongitude(),arrayList.get(0).getPOIPoint().getLatitude());
                    }
                });
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onLongPressEvent(ArrayList markerlist,
                                 ArrayList poilist, TMapPoint point) {
        Toast.makeText(GpsActivity.this," "+point.getLatitude(),Toast.LENGTH_SHORT);
    }

    public void GPS_ID_Complete(View view){
        if(detail_address_input.getText().toString().length()>0){
            Toast.makeText(GpsActivity.this,address_text.getText().toString()+" "+detail_address_input.getText().toString()+"\n주소 설정 완료",Toast.LENGTH_SHORT).show();
            UtilSet.my_user.setUser_address(address_text.getText().toString()+" "+detail_address_input.getText().toString());
            UtilSet.my_user.set_user_gps(latitude,longitude);
            Intent intent=new Intent(GpsActivity.this, FirstMainActivity.class);
                        UtilSet.write_user_data();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(GpsActivity.this,"상세 주소를 입력하시지 않았습니다.",Toast.LENGTH_SHORT).show();
        }
        return;
    }

}
