package com.example.app_user;

import android.Manifest;
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
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class Profile extends Fragment {

    ImageView imageView;
    EditText name;
    EditText cur_pw;
    EditText change_pw;
    EditText change_repw;
    Button profile_changh_btn;

    String trans_bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View pub_view = inflater.inflate(R.layout.fragment_profile,container,false);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }

        imageView = pub_view.findViewById(R.id.register_profile_img);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);

        name = (EditText) pub_view.findViewById(R.id.profile_name);
        cur_pw = (EditText) pub_view.findViewById(R.id.profile_current_pw);
        change_pw = (EditText) pub_view.findViewById(R.id.profile_change_pw);
        change_repw = (EditText) pub_view.findViewById(R.id.profile_chang_re_pw);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(img_intent,1);
            }
        });

        profile_changh_btn = (Button) pub_view.findViewById(R.id.profile_change_btn);
        profile_changh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(name.getText().toString().equals("")){
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText( getActivity(), "닉네임을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            else if(cur_pw.getText().toString().equals("")){
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText( getActivity(), "비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            else if(change_pw.getText().toString().equals("")){
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText( getActivity(), "변경할 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            else if(change_repw.getText().toString().equals("")){
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText( getActivity(), "재확인 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            else if(!change_pw.getText().toString().equals(change_repw.getText().toString())){
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText( getActivity(), "변경할 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }

                            JSONObject jsonParam = new JSONObject();
                            jsonParam.put("user_serial",UtilSet.my_user.getUser_serial());
                            jsonParam.put("user_img",trans_bitmap);
                            jsonParam.put("user_name", name.getText().toString());
                            jsonParam.put("original_pw", cur_pw.getText().toString());
                            jsonParam.put("change_pw",change_pw.getText().toString());
                            Log.d("1",trans_bitmap);
                            HttpURLConnection conn= UtilSet.user_modify_set_Connect_info(jsonParam);

                            if(conn.getResponseCode()==200){
                                InputStream response = conn.getInputStream();
                                String jsonReply = UtilSet.convertStreamToString(response);
                                JSONObject jobj=new JSONObject(jsonReply);
                                String json_result=jobj.getString("confirm");
                                if(json_result.equals("1")){
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText( getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent intent=new Intent(getContext(), LoginActivity.class);
                                    startActivityForResult(intent,101);
                                }else{
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText( getActivity(), "현재 패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return;
                                }
                            }else{
                                Log.d("error","Connect fail");
                            }
                            conn.disconnect();

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });


        LinearLayout linearLayout = pub_view.findViewById(R.id.linear_container);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return pub_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri image = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
            Bitmap out_bitmap = convertRoundedBitmap(bitmap);
            trans_bitmap = getBase64String(out_bitmap);
            imageView.setImageBitmap(out_bitmap);
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
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
}
