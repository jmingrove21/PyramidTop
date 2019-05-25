package com.example.app_user.util_dir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.MainActivity;
import com.example.app_user.home_dir.MenuActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원가입");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    public void register(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final EditText name = (EditText)findViewById(R.id.register_name);
                    final EditText id=(EditText)findViewById(R.id.register_id);
                    final EditText pw=(EditText)findViewById(R.id.register_pwd);
                    final EditText repw=(EditText)findViewById(R.id.register_repwd);
                    final EditText phone=(EditText)findViewById(R.id.register_phone);

                    if(name.getText().toString().equals("")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( RegisterActivity.this, "이름을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    else if(id.getText().toString().equals("")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( RegisterActivity.this, "ID를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    else if(phone.getText().toString().equals("")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( RegisterActivity.this, "폰번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    else if(pw.getText().toString().equals("")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( RegisterActivity.this, "비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    else if(repw.getText().toString().equals("")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( RegisterActivity.this, "비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    else if(!pw.getText().toString().equals(repw.getText().toString())){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info","join");
                    jsonParam.put("user_id", id.getText().toString());
                    jsonParam.put("user_password", pw.getText().toString());
                    jsonParam.put("user_name",name.getText().toString());
                    jsonParam.put("user_phone",phone.getText().toString());
                    jsonParam.put("user_sex","남자");

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        String json_result=jobj.getString("confirm");
                        if(json_result.equals("1")){
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText( RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
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