package com.example.app_user.draw_dir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.app_user.Item_dir.Order;
import com.example.app_user.Item_dir.Store;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Old_Orderlist extends Fragment {

    ListView listView;
    private OldOrderCustomAdapter oldOrderCustomAdapter;
    public static ArrayList<OldOrderPrduct> oldOrderProducts;

    int index;

    public void setIndex(int index) {
        this.index = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_old_orderlist,container,false);

        listView = (ListView) view.findViewById(R.id.old_olderlist_listview);
        get_store_info_by_my_order();
        oldOrderProducts = getOldOderProduct();
        oldOrderCustomAdapter = new OldOrderCustomAdapter(getActivity());

        if(UtilSet.al_my_old_order.size()==0){
            return view;
        }
        listView.setAdapter(oldOrderCustomAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), OldOrderlistDetailActivity.class);

                intent.putExtra("serial", UtilSet.al_my_old_order.get(position).getStore().getStore_serial());
                intent.putExtra("index", position);

                startActivityForResult(intent,101);
            }
        });

        return view;
    }

    private ArrayList<OldOrderPrduct> getOldOderProduct(){
        ArrayList<OldOrderPrduct> list = new ArrayList<>();
        for(int i = 0; i < UtilSet.al_my_old_order.size(); i++){
            OldOrderPrduct oldOrderPrduct = new OldOrderPrduct();
            oldOrderPrduct.setUser_store_name_input(UtilSet.al_my_old_order.get(i).getStore().getStore_name());
            oldOrderPrduct.setUser_store_branch_name_input(UtilSet.al_my_old_order.get(i).getStore().getStore_branch_name());
            oldOrderPrduct.setUser_order_time_input(UtilSet.al_my_old_order.get(i).getOrder_create_date());
            oldOrderPrduct.setUser_order_price_sum_input(UtilSet.al_my_old_order.get(i).getTotal_order_price());
            list.add(oldOrderPrduct);
        }
        return list;
    }
    public void get_store_info_by_my_order() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_my_old_order.clear();
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "ordered_list");
                    jsonParam.put("order_info",0);
                    jsonParam.put("user_serial",UtilSet.my_user.getUser_serial());

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);
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

                                Order o = new Order(order_create_date, participate_persons, total_order_price, order_number,order_status);
                                o.setDateSpecification(order_create_date,order_receipt_date,delivery_departure_time);
                                Store s = new Store(store_serial, store_name, store_branch_name, minimum_order_price, store_profile_img);
                                o.setStore(s);
                                UtilSet.al_my_old_order.add(o);
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
