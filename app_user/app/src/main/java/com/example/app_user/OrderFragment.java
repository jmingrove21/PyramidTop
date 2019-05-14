package com.example.app_user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class OrderFragment extends DialogFragment {
    Bitmap bitmap;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        store_info_specification();
        View view = inflater.inflate(R.layout.fragment_orderlist,container,false);
        listView = (ListView) view.findViewById(R.id.order_list);
        String[] store_name=new String[UtilSet.al_order.size()];
        for(int i=0;i<UtilSet.al_order.size();i++){
            store_name[i]=UtilSet.al_order.get(i).getStore().getStore_name();
        }

        OrderAdapter orderAdapter = new OrderAdapter(getActivity(),store_name);
        listView.setAdapter(orderAdapter);
        Thread mThread=new Thread(){
            @Override
            public void run(){
                for(int i=0;i<UtilSet.al_order.size();i++){
                    try{
                        URL url=new URL(UtilSet.al_order.get(i).getStore().getStore_profile_img());
                        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is=conn.getInputStream();
                        bitmap= BitmapFactory.decodeStream(is);
                        UtilSet.al_order.get(i).getStore().setStore_image(bitmap);
                    }catch(MalformedURLException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                int store_ser = UtilSet.al_order.get(position).getStore().getStore_serial();
                Intent intent=new Intent(v.getContext(),MenuActivity.class);
                intent.putExtra("serial",store_ser);
                intent.putExtra("index",position);
                startActivityForResult(intent,101);
            }
        });
        return view;
    }
    public void set_store_name_array(){

    }
    public void store_info_specification() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(UtilSet.url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "lookup_participate");
                    jsonParam.put("user_lat", 37.277218);
                    jsonParam.put("user_long", 127.046708);
                    jsonParam.put("user_count",4);

                    Log.i("JSON", jsonParam.toString());
                    OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
                    os.write(jsonParam.toString());

                    os.flush();
                    os.close();
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONArray jArray = new JSONArray(jsonReply);
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jobj = (JSONObject) jArray.get(i);

                                String store_serial = jobj.get("store_serial").toString();
                                String store_name = jobj.get("store_name").toString();
                                String store_branch_name = jobj.get("store_branch_name").toString();
                                String store_address = jobj.get("store_address").toString();
                                String minimum_order_price=jobj.get("minimum_order_price").toString();
                                String distance = jobj.get("distance").toString();
                                String store_profile_img = jobj.get("store_profile_img").toString();
                                String order_create_date=jobj.get("order_create_date").toString();
                                String participate_person=jobj.get("participate_persons").toString();
                                String total_order_price=jobj.get("total_order_price").toString();
                                Order o=new Order(order_create_date,participate_person,total_order_price);

                                Store s = new Store(store_serial, store_name, store_branch_name, store_address, minimum_order_price, distance,store_profile_img);
                                o.setStore(s);
                                UtilSet.al_order.add(o);
                            }
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
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
