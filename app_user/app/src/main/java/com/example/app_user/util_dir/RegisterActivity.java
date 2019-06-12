package com.example.app_user.util_dir;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.MainActivity;
import com.example.app_user.home_dir.MenuActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap profile_img_bitmap;
    Bitmap trans_bitmap;
    byte[] byteArray;
    int image_check = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원가입");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }

        imageView = findViewById(R.id.register_profile_img);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(img_intent,1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == Activity.RESULT_OK && data!=null){
            try {
                image_check=1;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                Bitmap out_bitmap = convertRoundedBitmap(bitmap);
                imageView.setImageBitmap(out_bitmap);
                trans_bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap convertRoundedBitmap(Bitmap bitmap){
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.GRAY;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }
        }
    }

    public void register(View v) {
        final EditText name = findViewById(R.id.register_name);
        final EditText id= findViewById(R.id.register_id);
        final EditText pw= findViewById(R.id.register_pwd);
        final EditText repw= findViewById(R.id.register_repwd);
        final EditText phone= findViewById(R.id.register_phone);

        if(name.getText().toString().equals("")){
            RegisterActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText( RegisterActivity.this, "닉네임을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
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

        OkHttpClient okHttpClient = new OkHttpClient();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        trans_bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_name",name.getText().toString())
                .addFormDataPart("user_id",id.getText().toString())
                .addFormDataPart("user_password",pw.getText().toString())
                .addFormDataPart("user_phone",phone.getText().toString())
                .addFormDataPart("user_img","profile_img.jpg",RequestBody.create(MediaType.parse("image/*jpg"),byteArray))
                .build();

        Request request = new Request.Builder()
                .url("http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/get/JSON/user_app/user_join.php")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("실패","failed",e);
                e.getStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if(response.isSuccessful()){
                    String result = response.body().string();
                    Log.d("결과",""+result);
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText( RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent,101);
                }
            }
        });
    }

//    public void register(View v) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final EditText name = findViewById(R.id.register_name);
//                    final EditText id= findViewById(R.id.register_id);
//                    final EditText pw= findViewById(R.id.register_pwd);
//                    final EditText repw= findViewById(R.id.register_repwd);
//                    final EditText phone= findViewById(R.id.register_phone);
//
//                    if(name.getText().toString().equals("")){
//                        RegisterActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText( RegisterActivity.this, "닉네임을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return;
//                    }
//                    else if(id.getText().toString().equals("")){
//                        RegisterActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText( RegisterActivity.this, "ID를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return;
//                    }
//                    else if(phone.getText().toString().equals("")){
//                        RegisterActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText( RegisterActivity.this, "폰번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return;
//                    }
//                    else if(pw.getText().toString().equals("")){
//                        RegisterActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText( RegisterActivity.this, "비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return;
//                    }
//                    else if(repw.getText().toString().equals("")){
//                        RegisterActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText( RegisterActivity.this, "비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return;
//                    }
//                    else if(!pw.getText().toString().equals(repw.getText().toString())){
//                        RegisterActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText( RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return;
//                    }
//
//                    JSONObject jsonParam = new JSONObject();
//                    jsonParam.put("user_info","join");
////                    jsonParam.put("user_img",byteArray);
//                    jsonParam.put("user_id", id.getText().toString());
//                    jsonParam.put("user_password", pw.getText().toString());
//                    jsonParam.put("user_name",name.getText().toString());
//                    jsonParam.put("user_phone",phone.getText().toString());
//
//                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);
//
//                    if(conn.getResponseCode()==200){
//                        Log.d("회원가입",""+conn.getResponseCode());
//                        InputStream response = conn.getInputStream();
//                        String jsonReply = UtilSet.convertStreamToString(response);
//                        Log.d("jsonReply",""+jsonReply);
//                        JSONObject jobj=new JSONObject(jsonReply);
//
//                        String json_result=jobj.getString("confirm");
//                        if(json_result.equals("1")){
//                            Log.d("json_result",""+json_result);
//                            RegisterActivity.this.runOnUiThread(new Runnable() {
//                                public void run() {
//                                    Toast.makeText( RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivityForResult(intent,101);
//                        }
//                    }else{
//                        Log.d("error","Connect fail");
//                    }
//                    conn.disconnect();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//    }
}