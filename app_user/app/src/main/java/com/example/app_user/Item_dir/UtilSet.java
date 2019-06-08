package com.example.app_user.Item_dir;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_user.R;
import com.skt.Tmap.TMapView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UtilSet {
    public static String key = "31a0c8ab-6880-42ba-b6f2-18080fbe6070";
    public static User my_user;
    public static MapPoint mapPoint = new MapPoint(false);
    public static ArrayList<Store> al_searchstore = new ArrayList<>();
    public static ArrayList<Store> al_store = new ArrayList<>();
    public static ArrayList<Order> al_order=new ArrayList<>();
    public static ArrayList<Order> al_my_order=new ArrayList<>();
    public static ArrayList<Order> al_my_old_order=new ArrayList<>();
    public static final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
    public static final int cacheSize=maxMemory/8;

    public static LruCache<String, Bitmap> mMemoryCache=new LruCache<String, Bitmap>(cacheSize){
        @Override
        protected int sizeOf( String key, Bitmap bitmap){
            return bitmap.getByteCount() / 1024;
        }

    };

    public final static String url="http://54.180.102.7/get/JSON/user_app/user_manage.php";
    public static final int[] MENU_TYPE_IMAGE = {R.drawable.q01_image,R.drawable.q02_image,R.drawable.q03_image,R.drawable.q04_image,R.drawable.q05_image,R.drawable.q06_image,R.drawable.q07_image,R.drawable.q08_image,R.drawable.q09_image,R.drawable.q10_image,R.drawable.q11_image,R.drawable.q12_image,R.drawable.q13_image};
    public static final String[] MENU_TYPE_ID ={"Q01","Q02","Q03","Q04","Q05","Q06","Q07","Q08","Q09","Q10","Q11","Q12","Q13"};
    public static final String[] MENU_TYPE_TEXT = {"도시락","돈가스,일식","디저트","분식","야식","양식","족발,보쌈","중국음식","치킨","탕,찜","패스트푸드","피자","한식"};
    public static Store target_store;
    public final static int PERMISSION_REQUEST_CODE=1000;
    public static double latitude=0;
    public static double longitude=0;
    public static LoginLogoutInform loginLogoutInform = new LoginLogoutInform();
    public static ToolbarInform toolbarInform = new ToolbarInform();
    public static int height;
    public static int width;
    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            while (true) {
                final String line = reader.readLine();
                if (line == null) break;
                sb.append(line  + "\n");
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

    // 이미지 캐쉬 하기
    public static void addBitmapToMemoryCache( String key, Bitmap bitmap){
        if( getBitmapFromMemCache( key) == null){
            mMemoryCache.put( key, bitmap);
        }
    }

    // 캐쉬된 이미지 가져오기
    public static Bitmap getBitmapFromMemCache( String key){
        return mMemoryCache.get( key);
    }
    public static HttpURLConnection user_modify_set_Connect_info(JSONObject jsonParam){
        try{
            URL url = new URL("http://54.180.102.7:80/get/JSON/user_app/user_manage.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.i("JSON", jsonParam.toString());
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            os.write(jsonParam.toString());

            os.flush();
            os.close();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static HttpURLConnection user_info_set_Connect_info(JSONObject jsonParam){
        try{
            URL url = new URL("http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/get/JSON/user_app/user_join.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.i("JSON", jsonParam.toString());
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            os.write(jsonParam.toString());

            os.flush();
            os.close();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static HttpURLConnection set_Connect_info(JSONObject jsonParam){
        try{
            URL url = new URL(UtilSet.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.i("JSON", jsonParam.toString());
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            os.write(jsonParam.toString());

            os.flush();
            os.close();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void set_GPS_permission(LocationManager lm, Context con){
        int permissionCheck=ContextCompat.checkSelfPermission(con,Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(con, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show();
            //showSettingsAlert(con);
        }
        else if(mapPoint.isGps_flag()){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            mapPoint.setGps_flag(true);
        }
    }

    public final static LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            Log.d("location","위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude );

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };

    public static void load_user_data(){
        try{
            File loadFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/user_ser.ser");
            FileInputStream fis = new FileInputStream(loadFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            UtilSet.my_user=(User)ois.readObject();
            if(my_user==null){
                loginLogoutInform.setLogin_flag(0);
            }else
            loginLogoutInform.setLogin_flag(1);
            ois.close();
        }catch(Exception e){
            e.printStackTrace();
            loginLogoutInform.setLogin_flag(0);
        }
    }
    public static void write_user_data(){
        try{
            File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/user_ser.ser");
            FileOutputStream fos=new FileOutputStream(saveFile);
            ObjectOutputStream oos=new ObjectOutputStream(fos);

            oos.writeObject(my_user);
            loginLogoutInform.setLogin_flag(1);
            oos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void delete_user_data(){
        try{
            File deleteFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/user_ser.ser");
            deleteFile.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray,int num){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        byteArray = null;
        return bitmap;
    }

    public static void set_Drawer(NavigationView navigationView,View view){
        if (LoginLogoutInform.getLogin_flag() == 1) {
            navigationView.inflateMenu(R.menu.drawer_menu);
            CircleImageView circleImageView = view.findViewById(R.id.nav_header_image);
 //           circleImageView.setImageBitmap(byteArrayToBitmap(UtilSet.my_user.getUser_img().getBytes(),0));
            TextView user_id= view.findViewById(R.id.user_id);
            user_id.setText(UtilSet.my_user.getUser_name()+"님 반갑습니다!");
            TextView user_mil= view.findViewById(R.id.user_mileage);
            user_mil.setText("마일리지 : "+UtilSet.my_user.getUser_mileage()+"원");
            TextView user_address= view.findViewById(R.id.user_address);
            if(UtilSet.my_user.getUser_address()==null)
                user_address.setText("배달주소를 선택해주세요!");
            else
                user_address.setText(UtilSet.my_user.getUser_address());
            TextView hello_msg= view.findViewById(R.id.please_login_text);
            hello_msg.setText(" ");
            navigationView.addHeaderView(view);
        } else {
            navigationView.inflateMenu(R.menu.logout_drawer_menu);

            ImageButton gps_btn = view.findViewById(R.id.GPS_imageBtn);
            gps_btn.setVisibility(View.INVISIBLE);
            TextView user_mil= view.findViewById(R.id.user_mileage);
            user_mil.setText(" ");
            TextView user_id= view.findViewById(R.id.user_id);
            user_id.setText(" ");
            TextView user_address= view.findViewById(R.id.user_address);
            user_address.setText(" ");
            TextView hello_msg= view.findViewById(R.id.please_login_text);
            hello_msg.setText("배달ONE과 함께하세요!");
            navigationView.addHeaderView(view);
        }
    }

}
