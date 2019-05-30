package com.example.app_user.Item_dir;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.example.app_user.R;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UtilSet {
    public static User my_User;
    public static int user_serial=7;
    public static ArrayList<Store> search_store = new ArrayList<>();
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
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
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

    public static void showSettingsAlert(final Context con){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(con);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 사용 승인이 필요합니다. \n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", "com.example.app_user", null));
                        con.startActivity(intent);
                    }
                });
        // Cancel 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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
}
