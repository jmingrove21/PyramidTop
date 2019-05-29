package com.example.app_delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        appData=getSharedPreferences("appData",MODE_PRIVATE);
        load_before_data();

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
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivityForResult(intent,101);
                            finish();
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
}
