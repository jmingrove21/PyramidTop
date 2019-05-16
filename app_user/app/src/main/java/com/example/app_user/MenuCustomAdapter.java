package com.example.app_user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuCustomAdapter extends BaseAdapter {
    private Context context;

    int tmp_ordernum;
    int total = 0;
    ArrayList<String> selectedItems = new ArrayList<>();


    public interface OnArrayList {
    }

    public MenuCustomAdapter(Context context) {

        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return MenuFragment.menuProductItems.size();
    }

    @Override
    public Object getItem(int position) {
        return MenuFragment.menuProductItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_layout,null,true);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,250));

            holder.button_choice = (Button) convertView.findViewById(R.id.choice);
            holder.button_minus = (Button) convertView.findViewById(R.id.minus);
            holder.button_plus = (Button) convertView.findViewById(R.id.plus);
            holder.text_order_number = (TextView) convertView.findViewById(R.id.order_number);
            holder.text_menu_name = (TextView) convertView.findViewById(R.id.menu_inform);
            holder.text_price_inform = (TextView) convertView.findViewById(R.id.price_inform);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        ImageView image_images = (ImageView) convertView.findViewById(R.id.imageView);
        image_images.setImageBitmap(MenuFragment.menuProductItems.get(position).getMenu_image());

        holder.text_menu_name.setText(MenuFragment.menuProductItems.get(position).getMenu_inform());
        holder.text_price_inform.setText(MenuFragment.menuProductItems.get(position).getPrice_inform());
        holder.text_order_number.setText(String.valueOf(MenuFragment.menuProductItems.get(position).getOrder_number()));

        holder.button_plus.setTag(R.integer.btn_plus_view, convertView);
        holder.button_plus.setTag(R.integer.btn_plus_pos, position);
        holder.button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.button_plus.getTag(R.integer.btn_plus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.order_number);
                Integer pos = (Integer) holder.button_plus.getTag(R.integer.btn_plus_pos);

                int number = Integer.parseInt(tv.getText().toString()) + 1;
                tv.setText(String.valueOf(number));

                MenuFragment.menuProductItems.get(pos).setOrder_number(number);
            }
        });

        holder.button_minus.setTag(R.integer.btn_minus_view, convertView);
        holder.button_minus.setTag(R.integer.btn_minus_pos, position);
        holder.button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tempview = (View) holder.button_minus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.order_number);
                Integer pos = (Integer) holder.button_minus.getTag(R.integer.btn_minus_pos);

                int number = Integer.parseInt(tv.getText().toString()) - 1;
                if (number >= 0) {
                    tv.setText(String.valueOf(number));
                    MenuFragment.menuProductItems.get(pos).setOrder_number(number);
                }
            }
        });

        holder.button_choice.setTag(R.integer.btn_choice_view, convertView);
        holder.button_choice.setTag(R.integer.btn_choice_pos, position);
        holder.button_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tempview = (View) holder.button_choice.getTag(R.integer.btn_choice_view);
                Button tv = (Button) tempview.findViewById(R.id.choice);
                Integer pos = (Integer) holder.button_choice.getTag(R.integer.btn_choice_pos);
                String tmp_tv = tv.getText().toString();

                if(tmp_tv.equals("선택")){
                    tv.setText("선택 해제");
                    MenuFragment.menuProductItems.get(pos).setChoice(tv);
                    holder.button_minus.setVisibility(View.VISIBLE);
                    holder.button_plus.setVisibility(View.VISIBLE);
                    holder.text_order_number.setVisibility(View.VISIBLE);
                    tmp_ordernum = Integer.parseInt(holder.text_order_number.getText().toString());
                    total = total + (tmp_ordernum * Integer.parseInt(MenuFragment.menuProductItems.get(position).getPrice_inform()));

                    String selectedItem = ((TextView) holder.text_menu_name).getText().toString();
                    selectedItems.add(selectedItem);

                    Intent intent=new Intent(v.getContext(),MenuActivity.class);
                    intent.putExtra("selectedMenuList",selectedItems);
                    intent.putExtra("select_order_total",total);

                }else{
                    tv.setText("선택");
                    MenuFragment.menuProductItems.get(pos).setChoice(tv);

                    MenuFragment.menuProductItems.get(position).setOrder_number(0);
                    holder.text_order_number.setText(String.valueOf(MenuFragment.menuProductItems.get(position).getOrder_number()));

                    holder.button_minus.setVisibility(View.INVISIBLE);
                    holder.button_plus.setVisibility(View.INVISIBLE);
                    holder.text_order_number.setVisibility(View.INVISIBLE);

                    tmp_ordernum = Integer.parseInt(holder.text_order_number.getText().toString());
                    total = total - (tmp_ordernum * Integer.parseInt(MenuFragment.menuProductItems.get(position).getPrice_inform()));

                    String selectedItem = ((TextView) holder.text_menu_name).getText().toString();
                    selectedItems.remove(selectedItem);

                    Intent intent=new Intent(v.getContext(),MenuActivity.class);
                    intent.putExtra("selectedmenu",selectedItems);
                    intent.putExtra("order_total",total);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder{
        protected Button button_plus, button_minus, button_choice;
        private TextView text_order_number, text_price_inform, text_menu_name;
    }
}
