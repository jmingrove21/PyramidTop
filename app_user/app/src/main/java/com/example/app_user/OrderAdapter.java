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

    Context c;
    String[] store_name;
    String[] party_number;
    String[] current_price;
    String[] minimum_price;
    int[] images;
    LayoutInflater inflater;

    public OrderAdapter(Context context, String[] store_name,
    String[] party_number, String[] current_price, String[] minimum_price, int[] images){
        super(context,R.layout.orderlist_layout,store_name);

        this.c = context;
        this.store_name = store_name;
        this.party_number = party_number;
        this.current_price = current_price;
        this.minimum_price = minimum_price;
        this.images  = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.orderlist_layout,null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,900));
        }
        TextView text_store_name = (TextView) convertView.findViewById(R.id.store_name);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);
        TextView text_party_number = (TextView) convertView.findViewById(R.id.party_number);
        TextView text_current_price = (TextView) convertView.findViewById(R.id.current_price);
        TextView text_minimum_price = (TextView) convertView.findViewById(R.id.minimum_price);

        text_store_name.setText(store_name[position]);
       // img.setImageResource(images[position]);
        text_party_number.setText(party_number[position]);
        text_current_price.setText(current_price[position]);
        text_minimum_price.setText(minimum_price[position]);


        return convertView;
    }
}
