package com.example.app_user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class OrderFragment extends DialogFragment {
    Bitmap bitmap;
    ListView listView;

    FragmentManager fm;
    FragmentTransaction tran;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UtilSet.al_order.clear();
        store_info_specification();
        View view = inflater.inflate(R.layout.fragment_orderlist, container, false);
        listView = (ListView) view.findViewById(R.id.order_list);
        final String[] store_name = new String[UtilSet.al_order.size()];
        for (int i = 0; i < UtilSet.al_order.size(); i++) {
            store_name[i] = UtilSet.al_order.get(i).getStore().getStore_name();
        }

        OrderAdapter orderAdapter = new OrderAdapter(getActivity(), store_name);
        listView.setAdapter(orderAdapter);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < UtilSet.al_order.size(); i++) {
                    try {
                        URL url = new URL(UtilSet.al_order.get(i).getStore().getStore_profile_img());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        UtilSet.al_order.get(i).getStore().setStore_image(bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                int store_ser = UtilSet.al_order.get(position).getStore().getStore_serial();
                store_info_detail(store_ser,position);
                Thread mThread=new Thread(){
                    @Override
                    public void run(){
                        for(int i=0;i<UtilSet.al_order.get(position).getStore().getMenu_al().size();i++){
                            for(int j=0;j<UtilSet.al_order.get(position).getStore().getMenu_al().get(i).getMenu_desc_al().size();j++) {
                                try {

                                    URL url = new URL(UtilSet.al_order.get(position).getStore().getMenu_al().get(i).getMenu_desc_al().get(j).getMenu_img());
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setDoInput(true);
                                    conn.connect();

                                    InputStream is = conn.getInputStream();
                                    bitmap = BitmapFactory.decodeStream(is);
                                    UtilSet.al_order.get(position).getStore().getMenu_al().get(i).getMenu_desc_al().get(j) .setMenu_image(bitmap);                       } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                UtilSet.target_store=UtilSet.al_order.get(position).getStore();
                UtilSet.target_store.setOrder_number(UtilSet.al_order.get(position).getOrder_number());
                Intent intent = new Intent(v.getContext(), SubMenuActivity.class);

                intent.putExtra("serial", store_ser);
                intent.putExtra("index", position);

                startActivityForResult(intent, 101);
            }
        });
        return view;
    }

    public void store_info_detail(final int store_serial, final int position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_order.get(position).getStore().getMenu_al().clear();
                    UtilSet.al_order.get(position).getStore().getMenu_desc_al().clear();
                    URL url = new URL(UtilSet.url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "store_detail");
                    jsonParam.put("store_serial", store_serial);

                    Log.i("JSON", jsonParam.toString());
                    OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    os.write(jsonParam.toString());

                    os.flush();
                    os.close();
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONObject jobj = new JSONObject(jsonReply);


                            String store_building_name = jobj.get("store_building_name").toString();
                            String start_time = jobj.get("start_time").toString();
                            String end_time = jobj.get("end_time").toString();
                            String store_restday = jobj.get("store_restday").toString();
                            String store_notice = jobj.get("store_notice").toString();
                            String store_main_type_name = jobj.get("store_main_type_name").toString();
                            String store_sub_type_name = jobj.get("store_sub_type_name").toString();

                            UtilSet.al_order.get(position).getStore().set_store_spec(store_building_name, start_time, end_time, store_restday, store_notice, store_main_type_name, store_sub_type_name);

                            JSONArray jobj_menu = (JSONArray) jobj.get("menu");
                            for (int j = 0; j < jobj_menu.length(); j++) {
                                JSONObject jobj_menu_spec = (JSONObject) jobj_menu.get(j);
                                String menu_type_code = jobj_menu_spec.get("menu_type_code").toString();
                                String menu_type_name = jobj_menu_spec.get("menu_type_name").toString();
                                UtilSet.al_order.get(position).getStore().getMenu_al().add(new com.example.app_user.Menu(menu_type_code, menu_type_name));
                                JSONArray menu_menu_desc = (JSONArray) jobj_menu_spec.get("menu description");
                                for (int k = 0; k < menu_menu_desc.length(); k++) {
                                    JSONObject jobj_menu_desc_spec = (JSONObject) menu_menu_desc.get(k);
                                    String menu_code = jobj_menu_desc_spec.get("menu_code").toString();
                                    String menu_name = jobj_menu_desc_spec.get("menu_name").toString();
                                    int menu_price = Integer.parseInt(jobj_menu_desc_spec.get("menu_price").toString());
                                    String menu_img = jobj_menu_desc_spec.get("menu_img").toString();
                                    UtilSet.al_order.get(position).getStore().getMenu_al().get(j).getMenu_desc_al().add(new MenuDesc(menu_code, menu_name, menu_price, menu_img));
                                }
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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                    jsonParam.put("user_count", 4);

                    Log.i("JSON", jsonParam.toString());
                    OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
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
                                String store_phone = jobj.get("store_phone").toString();
                                String minimum_order_price = jobj.get("minimum_order_price").toString();
                                String distance = jobj.get("distance").toString();
                                String store_profile_img = jobj.get("store_profile_img").toString();
                                String order_create_date = jobj.get("order_create_date").toString();
                                String participate_person = jobj.get("participate_persons").toString();
                                String total_order_price = jobj.get("total_order_price").toString();
                                String order_number=jobj.get("order_number").toString();
                                Order o = new Order(order_create_date, participate_person, total_order_price,order_number);

                                Store s = new Store(store_serial, store_name, store_branch_name, store_address, store_phone, minimum_order_price, distance, store_profile_img);
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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
