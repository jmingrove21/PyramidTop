package com.example.app_user.order_dir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;

public class OrderAdapter extends ArrayAdapter<String> {

    Context context;
    LayoutInflater inflater;

    public OrderAdapter(Context context,String[] store_name){
        super(context, R.layout.orderlist_layout,store_name);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.orderlist_layout,null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,300));
        }

        TextView text_date=(TextView)convertView.findViewById(R.id.date);
        TextView text_store_name = (TextView) convertView.findViewById(R.id.store_name);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);
        TextView text_party_number = (TextView) convertView.findViewById(R.id.party_number);
        TextView text_price = (TextView) convertView.findViewById(R.id.price);

        text_date.setText(UtilSet.al_order.get(position).getOrder_create_date());
        text_store_name.setText(UtilSet.al_order.get(position).getStore().getStore_name());
        img.setImageBitmap(UtilSet.al_order.get(position).getStore().getStore_image());
        text_party_number.setText(String.valueOf(UtilSet.al_order.get(position).getParticipate_person()));
        text_price.setText((UtilSet.al_order.get(position).getTotal_order_price())+" / "+UtilSet.al_order.get(position).getStore().getMinimum_order_price());

        return convertView;
    }
}
