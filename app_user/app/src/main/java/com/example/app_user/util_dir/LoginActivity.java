package com.example.app_user.util_dir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.MainActivity;
import com.example.app_user.util_dir.BackPressCloseHandler;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인");

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == 200) {
                String menu = data.getExtras().getString("menu");
                Toast.makeText(getApplicationContext(), "하잇!", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void login(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final EditText id=(EditText)findViewById(R.id.eid);
                    final EditText pw=(EditText)findViewById(R.id.epw);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info","login");
                    jsonParam.put("user_id", id.getText().toString());
                    jsonParam.put("user_password", pw.getText().toString());


                    Log.i("JSON", jsonParam.toString());

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        String json_result=jobj.getString("confirm");
                        Log.i("check_state", json_result);
                        if(json_result.equals("1")){
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(intent,101);
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
    }
}