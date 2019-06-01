package com.example.app_user.util_dir;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_user.Item_dir.User;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.FirstMainActivity;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;


public class LoginActivity extends AppCompatActivity {
    int user_serial;
    String user_name;
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인");
        if(UtilSet.loginLogoutInform.getLogin_flag()==1){
            UtilSet.loginLogoutInform.setLogin_flag(0);
            File deleteFIle=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/user_ser.ser");
            if(deleteFIle!=null)
                UtilSet.delete_user_data();
        }
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), FirstMainActivity.class);
        startActivityForResult(intent,101);
        finish();
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
                    final EditText id=(EditText)findViewById(R.id.id_input);
                    final EditText pw=(EditText)findViewById(R.id.pw_input);

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
                            user_name = jobj.getString("user_name");
                            user_serial = jobj.getInt("user_serial");

                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText( LoginActivity.this, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                                    UtilSet.my_user=new User(id.getText().toString(),pw.getText().toString(),user_serial,user_name);
                                    UtilSet.write_user_data();
                                }
                            });

                            Intent intent=new Intent(LoginActivity.this, FirstMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText( LoginActivity.this, "ID/PW가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    UtilSet.my_user=null;
                                }
                            });
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

    public void register(View view){
        Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent,101);
    }
}