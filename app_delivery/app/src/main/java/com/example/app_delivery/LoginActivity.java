package com.example.app_delivery;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences appData;
    private boolean saveLoginData;
    private String id;
    private String pwd;
    private EditText idText;
    private EditText pwdText;
    private CheckBox checkBox;
    public int order_number;
    Delivery_list delivery_list;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        appData=getSharedPreferences("appData",MODE_PRIVATE);
        load_before_data();
        UtilSet.lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        UtilSet.set_GPS_value(UtilSet.lm, this);
        idText=(EditText) findViewById(R.id.idText);
        pwdText=(EditText) findViewById(R.id.pwdText);
        checkBox=(CheckBox) findViewById(R.id.login_checkbox);

        if(saveLoginData){
            idText.setText(id);
            pwdText.setText(pwd);
            checkBox.setChecked(saveLoginData);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == 200) {

            }
        }
    }

    public void login(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "login");
                    jsonParam.put("delivery_id", idText.getText().toString());
                    jsonParam.put("delivery_password", pwdText.getText().toString());

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);
                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        String json_result=jobj.getString("confirm");
                        Log.i("check_state", json_result);
                        if(json_result.equals("1")){//check_state : 1 (success), 0 (fail)
                            save_login_data();
                            UtilSet.delivery_id=idText.getText().toString();
                            UtilSet.deliver_password=pwdText.getText().toString();
                            set_delivery_status();
//                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                            startActivityForResult(intent,101);
//                            finish();
                        }
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

    public void set_delivery_status() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "status");
                    jsonParam.put("delivery_id", UtilSet.delivery_id);
                    jsonParam.put("delivery_password", pwdText.getText().toString());

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);
                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        int status=jobj.getInt("status");
                        Log.i("check_status", String.valueOf(status));
                        if(status==0){
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivityForResult(intent,101);
                            finish();
                        }else  if(status==1){
                            int order_num=jobj.getInt("order_number");
                            String store_name=jobj.getString("store_name");
                            String store_branch_name=jobj.getString("store_branch_name");
                            order_number=order_num;
                            delivery_list=new Delivery_list(order_number, store_name,store_branch_name);
                            delivery_go_to_store();
                        }else  if(status==2){
                            int order_num=jobj.getInt("order_number");
                            order_number=order_num;
                            delivery_start_for_user();
                        }else{
                            throw new Exception("배달원 상태 오류");
                        }
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
    private void save_login_data() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA",checkBox.isChecked());
        editor.putString("ID", idText.getText().toString().trim());
        editor.putString("PWD", pwdText.getText().toString().trim());

        editor.apply();
    }

    private void load_before_data(){
        saveLoginData=appData.getBoolean("SAVE_LOGIN_DATA",false);
        id=appData.getString("ID","");
        pwd=appData.getString("PWD","");

    }
    public void delivery_go_to_store() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "approve");
                    jsonParam.put("order_number", order_number);
                    jsonParam.put("delivery_id",UtilSet.delivery_id);
                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        Intent intent=new Intent(getApplicationContext(),Delivery_to_Store_Activity.class);
                        intent.putExtra("store_name",delivery_list.getStore_name());
                        intent.putExtra("store_address", jobj.getString("store_address"));
                        intent.putExtra("store_phone", jobj.getString("store_phone"));
                        intent.putExtra("store_latitude",  Double.parseDouble(jobj.getString("store_latitude")));
                        intent.putExtra("store_longitude", Double.parseDouble( jobj.getString("store_longitude")));
                        intent.putExtra("order_number",  delivery_list.getOrder_number());

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
    } public void delivery_start_for_user() {
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
                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                        intent.putExtra("order_number", order_number);
                        intent.putExtra("direct",1);
                        intent.putExtra("json", jobj.toString());
                        startActivityForResult(intent, 101);
                        finish();
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

}
