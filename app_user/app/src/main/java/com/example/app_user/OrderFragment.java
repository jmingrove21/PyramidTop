package com.example.app_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OrderFragment extends DialogFragment {

    ListView listView;
    String[] store_name = {"1","2","3"};
    String[] party_number = {"a","b","c"};
    String[] minimum_number = {"6000","8000","5000"};
    String[] current_number = {"3000","2000","4000"};
    int[] images = {1,2,3};

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_orderlist,container,false);

        listView = (ListView) view.findViewById(R.id.order_list);

        getDialog().setTitle("현재 주문자들 현황");

        OrderAdapter orderAdapter = new OrderAdapter(getActivity(),store_name,party_number,minimum_number,
                current_number,images);

        listView.setAdapter(orderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                int store_ser = UtilSet.al_store.get(position).getStore_serial();
                Intent intent=new Intent(v.getContext(),MenuActivity.class);
                intent.putExtra("serial",store_ser);
                intent.putExtra("index",position);
                startActivityForResult(intent,101);
            }
        });
        return view;
    }
}
