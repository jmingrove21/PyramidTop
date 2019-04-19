package com.example.app_user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            1Thread.sleep(4000);
            2store_info_init();
        }catch(InterruptedException e){
            3e.printStackTrace();
        }
        //startActivity(new Intent(this, MainActivity.class));
        //finish();
    }

    public void store_info_init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://54.180.102.7/get/JSON/user_app/user_manage.php");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "store_list");
                    jsonParam.put("user_lat", 127.0435);
                    jsonParam.put("user_long", 37.2799);
                    jsonParam.put("user_count", 4);


                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONArray jArray = new JSONArray(jsonReply);
                            for (int i = 0; i < jArray.length(); i++) {
                                String store_serial = ((JSONObject) jArray.get(i)).get("store_serial").toString();
                                String store_name = ((JSONObject) jArray.get(i)).get("store_name").toString();
                                String store_branch_name = ((JSONObject) jArray.get(i)).get("store_branch_name").toString();
                                String store_phone = ((JSONObject) jArray.get(i)).get("store_phone").toString();
                                String store_address = ((JSONObject) jArray.get(i)).get("store_address").toString();
                                String store_distance = ((JSONObject) jArray.get(i)).get("distance").toString();
                                Store s = new Store(store_serial, store_name, store_branch_name, store_address, store_phone, store_distance);
                                UtilSet.al_store.add(s);
                            }

                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivityForResult(intent,101);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
    }

}
