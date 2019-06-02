package com.example.app_delivery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Delivery_list> order_list=new ArrayList<>();
    private BackPressCloseHandler backPressCloseHandler;
    private ListView m_oListView=null;
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionCheck();
        backPressCloseHandler=new BackPressCloseHandler(this);

        ArrayList<ItemData> oData=new ArrayList<>();
        get_delivery_order_list();
        for(int i=0;i<order_list.size();i++){
            ItemData oItem=new ItemData();
            oItem.strTitle=order_list.get(i).getStore_name();
            oItem.strDate=order_list.get(i).getDelivery_request_time();

            oData.add(oItem);
        }

        // ListView, Adapter 생성 및 연결 ------------------------
        m_oListView=(ListView)findViewById(R.id.listView);
        ListAdapter oAdapter=new ListAdapter(oData);
        m_oListView.setAdapter(oAdapter);
        m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("hihi");
            }
        });
        UtilSet.lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        UtilSet.set_GPS_value(UtilSet.lm, this);
    }
    public void get_delivery_order_list() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    order_list.clear();
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "request");
                    jsonParam.put("delivery_lat", UtilSet.latitude);
                    jsonParam.put("delivery_long", UtilSet.longitude);
                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONArray json_result_al=new JSONArray(jsonReply);
                        for (int i = 0; i < json_result_al.length(); i++) {
                            int order_number=Integer.parseInt(((JSONObject) json_result_al.get(i)).get("order_number").toString());
                            int store_serial=Integer.parseInt(((JSONObject) json_result_al.get(i)).get("store_serial").toString());
                            String delivery_request_time=((JSONObject) json_result_al.get(i)).get("delivery_request_time").toString();
                            String store_name=((JSONObject) json_result_al.get(i)).get("store_name").toString();
                            String store_branch_name=((JSONObject) json_result_al.get(i)).get("store_branch_name").toString();
                            float distance=Float.parseFloat(((JSONObject) json_result_al.get(i)).get("distance").toString());

                            order_list.add(new Delivery_list(order_number,store_serial,delivery_request_time,store_name,store_branch_name,distance));
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

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

    class ListAdapter extends BaseAdapter {
        LayoutInflater inflater = null;
        private ArrayList<ItemData> m_oData = null;
        private int nListCnt = 0;

        public ListAdapter(ArrayList<ItemData> oData){
            m_oData=oData;
            nListCnt=m_oData.size();
        }

        @Override
        public int getCount() {
            Log.i("Tag","getCount");
            return nListCnt;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                final Context context=parent.getContext();
                if(inflater==null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView=inflater.inflate(R.layout.activity_main_list,parent,false);

            }
            TextView oTextTitle=(TextView)convertView.findViewById(R.id.textTitle);
            TextView oTextDate=(TextView)convertView.findViewById(R.id.textDate);
            Button oButton=(Button)convertView.findViewById(R.id.btn);
            oTextTitle.setText(m_oData.get(position).strTitle);
            oTextDate.setText(m_oData.get(position).strDate);

            oButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("log","position:"+position);
                    get_delivery_order_specification1(position);
                }
            });
            return convertView;
        }

    }

    public void get_delivery_order_specification1(final int position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "approve");
                    jsonParam.put("order_number", MainActivity.order_list.get(position).getOrder_number());
                    jsonParam.put("store_serial", MainActivity.order_list.get(position).getStore_serial());
                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                       // JSONArray json_result_al=new JSONArray(jsonReply);
                        JSONObject jobj=new JSONObject(jsonReply);
                        Intent intent=new Intent(getApplicationContext(),Delivery_to_Store_Activity.class);
                        intent.putExtra("store_address", jobj.get("store_address").toString());
                        intent.putExtra("store_phone", jobj.get("store_phone").toString());
                        intent.putExtra("store_latitude",  Double.parseDouble(jobj.get("store_latitude").toString()));
                        intent.putExtra("store_longitude", Double.parseDouble( jobj.get("store_longitude").toString()));
                        intent.putExtra("order_number",  MainActivity.order_list.get(position).getOrder_number());

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
    } public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            ArrayList<String> arrayPermission = new ArrayList<>();

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (arrayPermission.size() > 0) {
                String strArray[] = new String[arrayPermission.size()];
                strArray = arrayPermission.toArray(strArray);
                ActivityCompat.requestPermissions(this, strArray, UtilSet.PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case UtilSet.PERMISSION_REQUEST_CODE: {
                if (grantResults.length < 1) {
                    Toast.makeText(this, "Failed get permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission is denied : " + permissions[i], Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
