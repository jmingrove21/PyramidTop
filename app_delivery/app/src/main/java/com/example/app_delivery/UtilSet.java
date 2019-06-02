package com.example.app_delivery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

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

public class UtilSet {
    public static String tmap_key = "31a0c8ab-6880-42ba-b6f2-18080fbe6070";
    public static String url="http://54.180.102.7:80/get/JSON/delivery_app/delivery_manage.php";
    public static LocationManager lm;
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

    public static void set_GPS_value(LocationManager lm, Context con){
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
//            if(my_user==null){
//            }else
            ois.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void write_user_data(){
        try{
            File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/user_ser.ser");
            FileOutputStream fos=new FileOutputStream(saveFile);
            ObjectOutputStream oos=new ObjectOutputStream(fos);

//            oos.writeObject(my_user);
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
}
