package com.example.app_user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuProduct extends ArrayAdapter<String> {

    ArrayList<String> selectedItems = new ArrayList<>();

    Context c;
    String[] menu_name;
    String[] price;
    Bitmap[] images;
    LayoutInflater inflater;

    int total = 0;
    TextView text_order_number;
    int order_number;
    Button button_choice;

    public MenuProduct(Context context, String[] menu_name, String[] price, Bitmap[] images){
        super(context,R.layout.orderlist_layout,menu_name);

        this.c = context;
        this.menu_name = menu_name;
        this.price = price;
        this.images  = images;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_layout,null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,700));
        }
        final TextView text_menu_name = (TextView) convertView.findViewById(R.id.menu_inform);
        ImageView image_images = (ImageView) convertView.findViewById(R.id.imageView);
        TextView text_price = (TextView) convertView.findViewById(R.id.price);
        text_order_number = (TextView) convertView.findViewById(R.id.order_number);
        Button button_plus = (Button) convertView.findViewById(R.id.plus);
        Button button_minus = (Button) convertView.findViewById(R.id.minus);
        button_choice = (Button) convertView.findViewById(R.id.choice);

        text_menu_name.setText(menu_name[position]);
        // image_images.setImageResource(images[position]);
        text_price.setText(price[position]);

        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_number = Integer.parseInt(text_order_number.getText().toString())+1;
                text_order_number.setText(Integer.toString(order_number));
            }
        });

        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_number = Integer.parseInt(text_order_number.getText().toString())-1;
                if(order_number>0) {
                    text_order_number.setText(Integer.toString(order_number));
                }
            }
        });

        button_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = button_choice.getText().toString();
                if(s.equals("선택")){
                    order_number = Integer.parseInt(text_order_number.getText().toString());
                    total = total + (order_number * Integer.parseInt(price[position]));

                    String selectedItem = ((TextView) text_menu_name).getText().toString();
                    selectedItems.add(selectedItem);

                    Intent intent=new Intent(v.getContext(),MenuActivity.class);
                    intent.putExtra("selectedmenu",selectedItems);
                    intent.putExtra("order_total",total);

                    s = "선택해제";
                    button_choice.setText(s);
                }else{
                    order_number = Integer.parseInt(text_order_number.getText().toString());
                    total = total - (order_number * Integer.parseInt(price[position]));

                    String selectedItem = ((TextView) text_menu_name).getText().toString();
                    selectedItems.remove(selectedItem);

                    Intent intent=new Intent(v.getContext(),MenuActivity.class);
                    intent.putExtra("selectedmenu",selectedItems);
                    intent.putExtra("order_total",total);

                    s = "선택";
                    button_choice.setText(s);
                }
            }
        });
        return convertView;
    }
}
