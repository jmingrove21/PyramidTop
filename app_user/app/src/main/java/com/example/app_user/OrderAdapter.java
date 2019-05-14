package com.example.app_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderAdapter extends ArrayAdapter<String> {

    Context context;
    LayoutInflater inflater;

    public OrderAdapter(Context context,String[] store_name){
        super(context,R.layout.orderlist_layout,store_name);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.orderlist_layout,null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,450));
        }

        TextView text_store_name = (TextView) convertView.findViewById(R.id.store_name);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);
        TextView text_party_number = (TextView) convertView.findViewById(R.id.party_number);
        TextView text_current_price = (TextView) convertView.findViewById(R.id.current_price);
        TextView text_minimum_price = (TextView) convertView.findViewById(R.id.minimum_price);

        text_store_name.setText(UtilSet.al_order.get(position).getStore().getStore_name());
        img.setImageBitmap(UtilSet.al_order.get(position).getStore().getStore_image());
        text_party_number.setText(String.valueOf(UtilSet.al_order.get(position).getParticipate_person()));
        text_current_price.setText(String.valueOf(UtilSet.al_order.get(position).getTotal_order_price()));
        text_minimum_price.setText(UtilSet.al_order.get(position).getStore().getMinimum_order_price());

        return convertView;
    }
}
