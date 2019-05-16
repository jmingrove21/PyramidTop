//package com.example.app_user;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class MenuProduct extends ArrayAdapter<String> {
//
//    ArrayList<String> selectedItems = new ArrayList<>();
//
//    Context c;
//    String[] menu_name;
//    String[] price;
//    Bitmap[] images;
//    LayoutInflater inflater;
//
//    int total = 0;
//    int order_number;
//
//    public MenuProduct(Context context, String[] menu_name, String[] price, Bitmap[] images){
//        super(context,R.layout.orderlist_layout,menu_name);
//
//        this.c = context;
//        this.menu_name = menu_name;
//        this.price = price;
//        this.images  = images;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent){
//        final ViewHolder holder;
//        if(convertView == null){
//            holder = new ViewHolder();
//            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.menu_layout,null);
//            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,500));
//
//            holder.button_choice = (Button) convertView.findViewById(R.id.choice);
//            holder.button_minus = (Button) convertView.findViewById(R.id.minus);
//            holder.button_plus = (Button) convertView.findViewById(R.id.plus);
//            holder.text_order_number = (TextView) convertView.findViewById(R.id.order_number);
//            holder.text_menu_name = (TextView) convertView.findViewById(R.id.menu_inform);
//            holder.text_price_inform = (TextView) convertView.findViewById(R.id.price_inform);
//
//            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder)convertView.getTag();
//        }
//        ImageView image_images = (ImageView) convertView.findViewById(R.id.imageView);
//
//        holder.text_menu_name.setText(menu_name[position]);
//        // image_images.setImageResource(images[position]);
//        holder.text_price_inform.setText(price[position]);
//
//        button_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                order_number = Integer.parseInt(text_order_number.getText().toString())+1;
//                text_order_number.setText(Integer.toString(order_number));
//            }
//        });
//
//        button_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                order_number = Integer.parseInt(text_order_number.getText().toString())-1;
//                if(order_number>0) {
//                    text_order_number.setText(Integer.toString(order_number));
//                }
//            }
//        });
//
//        button_choice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s = button_choice.getText().toString();
//                if(s.equals("선택")){
//                    order_number = Integer.parseInt(text_order_number.getText().toString());
//                    total = total + (order_number * Integer.parseInt(price[position]));
//                    s = "선택해제";
//                    button_choice.setText(s);
//
//                    String selectedItem = ((TextView) text_menu_name).getText().toString();
//                    selectedItems.add(selectedItem);
//
//                    Intent intent=new Intent(v.getContext(),MenuActivity.class);
//                    intent.putExtra("selectedmenu",selectedItems);
//                    intent.putExtra("order_total",total);
//                }else{
//                    order_number = Integer.parseInt(text_order_number.getText().toString());
//                    total = total - (order_number * Integer.parseInt(price[position]));
//                    s = "선택";
//                    button_choice.setText(s);
//
//                    String selectedItem = ((TextView) text_menu_name).getText().toString();
//                    selectedItems.remove(selectedItem);
//
//                    Intent intent=new Intent(v.getContext(),MenuActivity.class);
//                    intent.putExtra("selectedmenu",selectedItems);
//                    intent.putExtra("order_total",total);
//                }
//            }
//        });
//        return convertView;
//    }
//
//    public class ViewHolder{
//        protected Button button_plus, button_minus, button_choice;
//        private TextView text_order_number, text_price_inform, text_menu_name;
//    }
//}
