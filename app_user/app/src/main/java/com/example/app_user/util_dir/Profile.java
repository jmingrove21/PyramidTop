package com.example.app_user.util_dir;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.ParcelFileDescriptor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
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
import com.example.app_user.R;
import com.example.app_user.draw_dir.GpsActivity;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.people_dir.PartyDetailActivity;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Profile extends Fragment {

    ImageView imageView;
    EditText name;
    EditText cur_pw;
    EditText change_pw;
    EditText change_repw;
    Button profile_changh_btn;
    String result;
    Bitmap trans_bitmap;
    byte[] byteArray;
    String url;
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
                Log.d("image_info",img_intent.getDataString());
            }
        });

        profile_changh_btn = (Button) pub_view.findViewById(R.id.profile_change_btn);
        profile_changh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
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

    public void send(){
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
        String original_pw=cur_pw.getText().toString();
        String change_pw=change_repw.getText().toString();
        post(url, "temp.jpg",original_pw,change_pw);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri image = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
            url=image.toString();
            Bitmap out_bitmap = convertRoundedBitmap(bitmap);
            imageView.setImageBitmap(out_bitmap);
            trans_bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            trans_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
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
    private void post(String uriString, String textString,String original_pw, String change_pw) {
        PostTask postTask = new PostTask();
        postTask.execute(uriString, textString,original_pw,change_pw);
    }

    class PostTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressDialog=ProgressDialog.show(Profile.this.getContext(), "포스트 업로드 중", "잠시만 기다려주세요...", true, false);
        }

        @Override
        protected Boolean doInBackground(String... strings) {

           // Glide.with(this).load(photoUri).into(ivPost);
            //Uri content: //media/external/images/media/255
            // Uri -> Bitmap -> File
            /** Ready for post */

            final Uri imageUri = Uri.parse(strings[0]);
            String text = strings[1];

            try {
                Bitmap bitmap = getBitmapFromUri(imageUri);
                File imageFile = null;
                String path=imageUri.getPath();
                Profile.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        String[] proj = { MediaStore.Images.Media.DATA };
                        Cursor cursor = Profile.this.getContext().getContentResolver().query(imageUri,  proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        cursor.getString(column_index);
                        cursor.moveToFirst();
                        result=cursor.getString(column_index);
                    }
                });



                final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
                final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
                final MediaType MEDIA_TYPE_GIF = MediaType.parse("image/gif");

//                if (path.contains(".jpg")) {
//                    k = RequestBody.create(MEDIA_TYPE_JPG, sourceFile);
//                    ext = ".jpg";
//                } else if (path.contains(".jpeg")) {
//                    ext = ".jpeg";
//                    k = RequestBody.create(MEDIA_TYPE_JPEG, sourceFile);
//                } else if (path.contains(".png")) {
//                    ext = ".png";
//                    k = RequestBody.create(MEDIA_TYPE_PNG, sourceFile);
//                } else if (path.contains(".gif")) {
//                    ext = ".gif";
//                    k = RequestBody.create(MEDIA_TYPE_GIF, sourceFile);
//                }

                /** HTTP POST */
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uploader", "jaimemin")
                        .addFormDataPart("text", text)
                        .addFormDataPart("user_img", makeImageFileName(),
                                RequestBody.create(MediaType.parse("image"), new File(result)))
                        .addFormDataPart("user_serial",String.valueOf(UtilSet.my_user.getUser_serial()))
                        .addFormDataPart("user_name", UtilSet.my_user.getUser_name())
                        .addFormDataPart("original_pw",strings[2])
                        .addFormDataPart("change_pw",strings[3])
                        .addFormDataPart("img_check","1")
                        .build();

                Request request = new Request.Builder()
                        .url("http://54.180.102.7:80/get/JSON/user_app/user_modify_info.php")
                        .post(requestBody)
                        .build();
                //Timeout 설정
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .build();

                Response response = okHttpClient.newCall(request).execute();
                if (response.code() == 200){
                    String result=response.body().string();
                   try {
                       JSONObject jobj = new JSONObject(result);
                        UtilSet.my_user.setUser_img(jobj.getString("path"));
                       Intent intent = new Intent(Profile.this.getContext(), FirstMainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                    return true;
                }
                else
                    return false;
            } catch (IOException e) {
                Log.d("PostTask", "post failed", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
         //   progressDialog.dismiss();
            if(aBoolean){
              //  Toast.makeText(Profile.this.getContext(), "ProgressDialog success", Toast.LENGTH_SHORT).show();
                Log.d("PostTask", "success");
            }
            else
                Log.d("PostTask", "failed");
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor = Profile.this.getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        //세부 정보 말고 크기 정보만 갖고 온다
        opts.inJustDecodeBounds = true;
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);

        // ex) 4096 * 3800
        int width = opts.outWidth;
        int height=opts.outHeight;

        float sampleRatio = getSampleRatio(width, height);

        opts.inJustDecodeBounds=false;
        opts.inSampleSize=(int)sampleRatio;

        Bitmap resizedBitmap=BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);
        Log.d("Resizing", "Resized Width / Height" + resizedBitmap.getWidth() + "/" + resizedBitmap.getHeight());
        parcelFileDescriptor.close();
        return resizedBitmap;
    }

    private float getSampleRatio(int width, int height) {
        //상한
        final int targetWidth = 1280;
        final int targetHeight = 1280;

        float ratio;

        if(width > height){
            //Landscape
            if(width > targetWidth){
                ratio = (float)width / (float)targetWidth;
            }
            else
                ratio = 1f;
        }
        else{
            //Portrait
            if(height > targetHeight){
                ratio=(float)height/(float)targetHeight;
            }
            else
                ratio = 1f;
        }

        return Math.round(ratio);
    }

    private File createFileFromBitmap(Bitmap bitmap) throws IOException {

        File newFile = new File( Profile.this.getContext().getFilesDir(), makeImageFileName());

        FileOutputStream fileOutputStream = new FileOutputStream(newFile);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

        fileOutputStream.close();

        return null;
    }

    private String makeImageFileName() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        Date date = new Date();
        String strDate = simpleDateFormat.format(date);
        return strDate + ".png";
    }
}
