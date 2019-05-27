package com.example.app_user.util_dir;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;


public class StoreDetailFragment extends Fragment {
    int index;
   public void setIndex(int index){
       this.index=index;
   }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.store_detail_fragment,container,false);

        TextView text_store_rest = (TextView) view.findViewById(R.id.store_rest_day);
        TextView text_store_address = (TextView) view.findViewById(R.id.store_address);
        TextView text_store_phone=(TextView) view.findViewById(R.id.store_phone);
        TextView text_store_operation_start_time = (TextView) view.findViewById(R.id.store_operation_start_time);
        TextView text_store_operation_end_time = (TextView) view.findViewById(R.id.store_operation_end_time);
        TextView text_store_notice = (TextView) view.findViewById(R.id.store_notice);
        TextView text_store_minimum_price=(TextView) view.findViewById(R.id.store_mininum_price);


        text_store_rest.setText(UtilSet.target_store.getStore_restday());
        text_store_minimum_price.setText(UtilSet.target_store.getMinimum_order_price());
        text_store_phone.setText(UtilSet.target_store.getStore_phone());
        text_store_address.setText(UtilSet.target_store.getStore_address());
        text_store_operation_start_time.setText(UtilSet.target_store.getStart_time());;
        text_store_operation_end_time.setText(UtilSet.target_store.getEnd_time());
        text_store_notice.setText(UtilSet.target_store.getStore_notice());
        return view;
    }
}
