package com.example.app_user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Old_Orderlist extends Fragment {

    String[] str = {"test1","test2","test3"};
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
        oldOrderProducts = getOldOderProduct();
        oldOrderCustomAdapter = new OldOrderCustomAdapter(getActivity());

        listView.setAdapter(oldOrderCustomAdapter);

        return view;
    }

    private ArrayList<OldOrderPrduct> getOldOderProduct(){
        ArrayList<OldOrderPrduct> list = new ArrayList<>();
        for(int i = 0; i < str.length; i++){
            OldOrderPrduct oldOrderPrduct = new OldOrderPrduct();
            oldOrderPrduct.setUser_store_name_input(str[i]);
            oldOrderPrduct.setUser_store_number_input(str[i]);
            oldOrderPrduct.setUser_store_address_input(str[i]);
            oldOrderPrduct.setUser_pay_method_input(str[i]);
            oldOrderPrduct.setUser_order_time_input(str[i]);
            oldOrderPrduct.setUser_order_price_sum_input(str[i]);
            oldOrderPrduct.setUser_order_complete_time_input(str[i]);
            oldOrderPrduct.setUser_deliver_start_time_input(str[i]);
            oldOrderPrduct.setUser_deliver_complete_time_input(str[i]);
            oldOrderPrduct.setUser_cooking_complete_time_input(str[i]);
            list.add(oldOrderPrduct);
        }
        return list;
    }
}
