package com.example.app_user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

public class StoreDetailFragment extends Fragment {
    int index;
   public void setIndex(int index){
       this.index=index;
   }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.store_detail_fragment,container,false);

        TextView text_store_rest = (TextView) view.findViewById(R.id.store_rest_day);
        TextView text_store_branch_name = (TextView) view.findViewById(R.id.store_branch_name);
        TextView text_store_address = (TextView) view.findViewById(R.id.store_address);
        TextView text_store_operation_start_time = (TextView) view.findViewById(R.id.store_operation_start_time);
        TextView text_store_operation_end_time = (TextView) view.findViewById(R.id.store_operation_end_time);
        TextView text_store_notice = (TextView) view.findViewById(R.id.store_notice);

        text_store_rest.setText(UtilSet.al_store.get(index).getStore_restday());
        text_store_branch_name.setText(UtilSet.al_store.get(index).getStore_branch_name());
        text_store_address.setText(UtilSet.al_store.get(index).getStore_address());
        text_store_operation_start_time.setText(UtilSet.al_store.get(index).getStart_time());;
        text_store_operation_end_time.setText(UtilSet.al_store.get(index).getEnd_time());
        text_store_notice.setText(UtilSet.al_store.get(index).getStore_notice());
        return view;
    }
}
