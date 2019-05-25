package com.example.app_user.draw_dir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.app_user.R;

import java.util.ArrayList;

public class Old_Orderlist extends Fragment {

    String[] str = {"test1","test2","test3"};
    ListView listView;
    int store_ser;
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
        oldOrderProducts = getOldOderProduct();
        oldOrderCustomAdapter = new OldOrderCustomAdapter(getActivity());

        listView.setAdapter(oldOrderCustomAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), OldOrderlistDetailActivity.class);

                intent.putExtra("serial", store_ser);
                intent.putExtra("index", position);

                startActivityForResult(intent,101);
            }
        });

        return view;
    }

    private ArrayList<OldOrderPrduct> getOldOderProduct(){
        ArrayList<OldOrderPrduct> list = new ArrayList<>();
        for(int i = 0; i < str.length; i++){
            OldOrderPrduct oldOrderPrduct = new OldOrderPrduct();
            oldOrderPrduct.setUser_store_name_input(str[i]);
            oldOrderPrduct.setUser_order_time_input(str[i]);
            oldOrderPrduct.setUser_order_price_sum_input(str[i]);
            list.add(oldOrderPrduct);
        }
        return list;
    }
}
