package com.example.app_user.draw_dir;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_user.Item_dir.MapPoint;
import com.example.app_user.Item_dir.User;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.home_dir.MenuActivity;
import com.example.app_user.util_dir.BackPressCloseHandler;
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

import static com.example.app_user.Item_dir.UtilSet.gpsLocationListener;

public class GpsActivity extends Activity implements TMapGpsManager.onLocationChangedCallback, TMapView.OnLongClickListenerCallback {
    private EditText GPS_editText;
    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapData tmapdata = null;
    private TMapGpsManager tMapGpsManager = null;
    private TMapView tMapView = null;
    private static int mMarkerID;
    BackPressCloseHandler backPressCloseHandler;
    private ArrayList<TMapMarkerItem> tMapMarkerItems = new ArrayList<TMapMarkerItem>();
    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();
    private String address;
    private Double lat = null;
    private Double lon = null;

    private boolean cur_search_gps = false;
    private Double cur_lat;
    static LocationManager lm;


    private Button gps_button;

    private Thread thread;

    private TMapPoint first_TMapPoint;
    Bitmap mark_bitmap;

    TextView address_text;
    EditText detail_address_input;

    public static double latitude;
    public static double longitude;

    public static double gps_latitude;
    public static double gps_longitude;
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

        UtilSet.set_GPS_permission(lm, GpsActivity.this);//GPS
        Log.d("longitude",""+UtilSet.longitude);
        Log.d("latitude",""+UtilSet.latitude);

        backPressCloseHandler = new BackPressCloseHandler(this);
        address_text = findViewById(R.id.address_text);
        detail_address_input = findViewById(R.id.detail_address_input);

        mContext = this;

        gps_button = findViewById(R.id.GPS_button);

        tmapdata = new TMapData();
        LinearLayout linearLayout = findViewById(R.id.map_view);
        tMapView = new TMapView(this);

        linearLayout.addView(tMapView);
        tMapView.setSKTMapApiKey(UtilSet.key);

        tMapView.setSightVisible(true);

//        showMarkerPoint();

        //tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(20);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setLocationPoint(UtilSet.longitude,UtilSet.latitude);
        tMapView.setCenterPoint(UtilSet.longitude,UtilSet.latitude);

        //        tMapGpsManager = new TMapGpsManager(GpsActivity.this);
//        tMapGpsManager.setMinTime(1000);
//        tMapGpsManager.setMinDistance(5);
//        tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);
//
//        tMapGpsManager.OpenGps();

//        tMapView.setTrackingMode(true);

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
                    TMapMarkerItem markerItem = new TMapMarkerItem();
                    markerItem.setIcon(mark_bitmap);
                    markerItem.setTMapPoint(tMapPoint);

                    tMapView.setCenterPoint(tMapPoint.getLongitude(),tMapPoint.getLatitude());
                    tMapView.addMarkerItem("markerItem",markerItem);
                    latitude = tMapPoint.getLatitude();
                    longitude = tMapPoint.getLongitude();

                    cur_search_gps = false;

                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Tmap_async t_async = new Tmap_async(false);
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
                Tmap_async t_async = new Tmap_async(true);
                t_async.execute();
            }
        });
        thread.start();
    }

    private class Tmap_async extends AsyncTask<Integer, Integer, String>{
        boolean check=false;

        Tmap_async(boolean check){
            this.check=check;
        }
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                if(check==true){
                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        latitude=gps_latitude;
                        longitude=gps_longitude;
                        lm=null;
                }
                final String address = new TMapData().convertGpsToAddress(latitude,longitude);

                GpsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        tMapView.setCenterPoint(longitude,latitude);
                        Toast.makeText(GpsActivity.this, address, Toast.LENGTH_SHORT).show();
                        UtilSet.my_user.setUser_address(address);
                        address_text.setText(address);
                    }
                });

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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Tmap_async t_async = new Tmap_async(true);
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
                        for(int i=0;i<arrayList.size();i++) {
                            TMapPOIItem item = arrayList.get(i);
                            TMapPoint tmp_TMapPoint = new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude());
                            TMapMarkerItem markerItem = new TMapMarkerItem();
                            markerItem.setIcon(bitmap);
                            markerItem.setTMapPoint(tmp_TMapPoint);

                            tMapView.setCenterPoint(tmp_TMapPoint.getLongitude(),tmp_TMapPoint.getLatitude());

                            Log.d("주소로 찾기", "POI Name: " +
                                    item.getPOIName() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") +
                                    ", " + "Point: " + item.getPOIPoint().toString());
                        }
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
        Toast.makeText(GpsActivity.this,""+point.getLatitude(),Toast.LENGTH_SHORT);
    }

    public void GPS_ID_Complete(View view){
        if(detail_address_input.getText().toString().length()>0){
            Toast.makeText(GpsActivity.this,address_text.getText().toString()+" "+detail_address_input.getText().toString()+"\n주소 설정 완료",Toast.LENGTH_SHORT).show();
            UtilSet.my_user.setUser_address(address_text.getText().toString()+" "+detail_address_input.getText().toString());
            UtilSet.longitude = longitude;
            UtilSet.latitude = latitude;
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
    public static void set_GPS_permission(LocationManager lm, Context con){
        int permissionCheck= ContextCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(con,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();
            //showSettingsAlert(con);
        }else{
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);
            }
        }


    public  static LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            gps_longitude = location.getLongitude();
            gps_latitude = location.getLatitude();

            Log.d("location","GPS page 위치정보 : " + provider + "\n" +
                    "위도 : " + gps_longitude + "\n" +
                    "경도 : " + gps_latitude );
            Log.d("location","내 현재 위치정보 : " + provider + "\n" +
                    "위도 : " + UtilSet.longitude + "\n" +
                    "경도 : " + UtilSet.latitude );
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };
}
