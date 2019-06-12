package com.example.app_user.people_dir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.MenuDesc;
import com.example.app_user.Item_dir.Order;
import com.example.app_user.Item_dir.Store;
import com.example.app_user.Item_dir.User;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PeopleFragment extends DialogFragment {
    Bitmap bitmap;
    ListView listView;
    Button order_cancel_btn;

    public PeopleFragment() {
        UtilSet.al_my_order.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_people, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        UtilSet.height = displaymetrics.heightPixels;
        UtilSet.width = displaymetrics.widthPixels;
        LinearLayout frame = view.findViewById(R.id.recent_orderlist_linear);
        listView = view.findViewById(R.id.people_listview);

        order_cancel_btn = view.findViewById(R.id.order_cancel_btn);


        if(LoginLogoutInform.getLogin_flag()==0){
            Toast.makeText( getActivity(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return view;
        }
        get_store_info_by_my_order();



        final SwipeRefreshLayout mSwipeRefreshLayout = view.findViewById(R.id.swipe_my_party_order_list);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UtilSet.al_my_order.clear();
                refresh_fragment();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        final String[] store_name = new String[UtilSet.al_my_order.size()];
        if(UtilSet.al_my_order.size()==0){
            frame.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.no_order));

        }else {
            for (int i = 0; i < UtilSet.al_my_order.size(); i++) {
                store_name[i] = UtilSet.al_my_order.get(i).getStore().getStore_name();
            }
        }

        PeopleAdapter peopleAdapter = new PeopleAdapter(getActivity(), store_name);
        listView.setAdapter(peopleAdapter);

        get_my_order_list_image();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                int store_ser = UtilSet.al_my_order.get(position).getStore().getStore_serial();
                get_my_order_list(store_ser, position);
                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < UtilSet.al_my_order.get(position).getStore().getMenu_al().size(); i++) {
                            for (int j = 0; j < UtilSet.al_my_order.get(position).getStore().getMenu_al().get(i).getMenu_desc_al().size(); j++) {
                                try {

                                    URL url = new URL(UtilSet.al_order.get(position).getStore().getMenu_al().get(i).getMenu_desc_al().get(j).getMenu_img());
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setDoInput(true);
                                    conn.connect();

                                    InputStream is = conn.getInputStream();
                                    bitmap = BitmapFactory.decodeStream(is);
                                    UtilSet.al_my_order.get(position).getStore().getMenu_al().get(i).getMenu_desc_al().get(j).setMenu_image(bitmap);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                Intent intent = new Intent(v.getContext(), PartyDetailActivity.class);

                intent.putExtra("serial", store_ser);
                intent.putExtra("index", position);

                startActivityForResult(intent, 101);
            }
        });
        return view;
    }

    public void refresh_fragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

    public void get_my_order_list(final int store_serial, final int position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_my_order.get(position).getUser_al().clear();
                    UtilSet.al_my_order.get(position).getStore().getMenu_desc_al().clear();
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "ordered_list_detail");
                    jsonParam.put("store_serial", store_serial);
                    jsonParam.put("order_info", 1);
                    jsonParam.put("user_serial", UtilSet.my_user.getUser_serial());
                    jsonParam.put("order_number", UtilSet.al_my_order.get(position).getOrder_number());

                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONObject jobj = new JSONObject(jsonReply);
                            JSONObject jarray_user = (JSONObject) jobj.get("user_info");
                            int user_pay_price = Integer.parseInt(jarray_user.getString("pay_price"));
                            int user_pay_status = Integer.parseInt(jarray_user.getString("pay_status"));
                            UtilSet.al_my_order.get(position).setMy_pay_price(user_pay_price);
                            UtilSet.al_my_order.get(position).setMy_pay_status(user_pay_status);
                            JSONArray jarray_user_menu = (JSONArray) jarray_user.get("user_menu");
                            for (int j = 0; j < jarray_user_menu.length(); j++) {
                                JSONObject jobj_user_menu_info = (JSONObject) jarray_user_menu.get(j);
                                String menu_name = jobj_user_menu_info.get("menu_name").toString();
                                String menu_count = jobj_user_menu_info.get("menu_count").toString();
                                String menu_price = jobj_user_menu_info.get("menu_price").toString();
                                MenuDesc md = new MenuDesc(menu_name, Integer.parseInt(menu_count), Integer.parseInt(menu_price));
                                UtilSet.al_my_order.get(position).getStore().getMenu_desc_al().add(md);
                            }

                            JSONArray jarray_another = (JSONArray) jobj.get("another_info");
                            if (!((JSONArray) jobj.get("another_info")).get(0).equals(null)) {
                                for (int j = 0; j < jarray_another.length(); j++) {
                                    JSONObject jarray_another_info = (JSONObject) jarray_another.get(j);
                                    String user_id = jarray_another_info.get("user_id").toString();
                                    String user_price = jarray_another_info.get("total_price").toString();
                                    String user_time = jarray_another_info.get("make_order_time").toString();
                                    User u = new User(user_id);
                                    u.setUser_info(user_time, user_price);

                                    UtilSet.al_my_order.get(position).getUser_al().add(u);
                                }
                            }
                            JSONObject jobj_store = new JSONObject(jobj.get("store_info").toString());
                            String store_serial = jobj_store.get("store_serial").toString();
                            String store_building_name = jobj_store.get("store_building_name").toString();
                            String store_phone = jobj_store.get("store_phone").toString();
                            String start_time = jobj_store.get("start_time").toString();
                            String end_time = jobj_store.get("end_time").toString();
                            String store_address = jobj_store.get("store_address").toString();
                            String store_restday = jobj_store.get("store_restday").toString();
                            String store_notice = jobj_store.get("store_notice").toString();
                            String delivery_cost=jobj_store.get("delivery_cost").toString();
                            UtilSet.al_my_order.get(position).getStore().set_store_spec(store_serial, store_building_name, start_time, end_time, store_phone, store_address, store_restday, store_notice);
                            UtilSet.al_my_order.get(position).getStore().setDelivery_cost(Integer.parseInt(delivery_cost));
                        } catch (JSONException e) {
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

    public void get_my_order_list_image() {
        Thread mThread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < UtilSet.al_my_order.size(); i++) {
                    try {
                        if (UtilSet.getBitmapFromMemCache(UtilSet.al_my_order.get(i).getStore().getStore_profile_img()) != null) {
                            bitmap = UtilSet.getBitmapFromMemCache(UtilSet.al_my_order.get(i).getStore().getStore_profile_img());
                            UtilSet.al_my_order.get(i).getStore().setStore_image(bitmap);
                        } else {
                            URL url = new URL(UtilSet.al_my_order.get(i).getStore().getStore_profile_img());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                            UtilSet.al_my_order.get(i).getStore().setStore_image(bitmap);
                            UtilSet.addBitmapToMemoryCache(UtilSet.al_my_order.get(i).getStore().getStore_profile_img(), bitmap);
                        }
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
    }

    public void get_store_info_by_my_order() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "ordered_list");
                    jsonParam.put("order_info", 1);
                    jsonParam.put("user_serial", UtilSet.my_user.getUser_serial());

                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONArray jArray = new JSONArray(jsonReply);
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jobj = (JSONObject) jArray.get(i);

                                String order_number = jobj.get("order_number").toString();
                                String order_status = jobj.get("order_status").toString();
                                String store_serial = jobj.get("store_serial").toString();
                                String store_name = jobj.get("store_name").toString();
                                String store_branch_name = jobj.get("store_branch_name").toString();
                                String minimum_order_price = jobj.get("minimum_order_price").toString();
                                String store_profile_img = jobj.get("store_profile_img").toString();
                                String order_create_date = jobj.get("order_create_date").toString();
                                String order_receipt_date = jobj.get("order_receipt_date").toString();
                                String delivery_departure_time = jobj.get("delivery_departure_time").toString();

                                String participate_persons = jobj.get("participate_persons").toString();
                                String total_order_price = jobj.get("total_order_price").toString();

                                Order o = new Order(order_create_date, participate_persons, total_order_price, order_number, order_status);
                                o.setDateSpecification(order_create_date, order_receipt_date, delivery_departure_time);
                                Store s = new Store(store_serial, store_name, store_branch_name, minimum_order_price, store_profile_img);
                                o.setStore(s);
                                UtilSet.al_my_order.add(o);
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
