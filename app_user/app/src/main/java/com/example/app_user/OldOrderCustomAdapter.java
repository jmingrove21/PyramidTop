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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OldOrderCustomAdapter extends BaseAdapter {
    private Context context;

    int tmp_ordernum;
    int total = 0;
    ArrayList<String> selectedItems = new ArrayList<>();


    public interface OnArrayList {
    }

    public OldOrderCustomAdapter(Context context) {

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
        return Old_Orderlist.oldOrderProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return Old_Orderlist.oldOrderProducts.get(position);
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
            convertView = inflater.inflate(R.layout.fragment_old_orderlist_listview_layout,null,true);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,800));

            holder.text_user_store_name_input = (TextView) convertView.findViewById(R.id.user_store_name_input);
            holder.text_user_store_number_input = (TextView) convertView.findViewById(R.id.user_store_number_input);
            holder.text_user_store_address_input = (TextView) convertView.findViewById(R.id.user_store_address_input);

            holder.text_user_order_price_sum_input = (TextView) convertView.findViewById(R.id.user_order_price_sum_input);
            holder.text_user_order_time_input = (TextView) convertView.findViewById(R.id.user_order_time_input);
            holder.text_user_pay_method_input = (TextView) convertView.findViewById(R.id.user_pay_method_input);

            holder.text_user_order_complete_time_input = (TextView) convertView.findViewById(R.id.user_order_complete_time_input);
            holder.text_user_cooking_complete_time_input = (TextView) convertView.findViewById(R.id.user_cooking_complete_time_input);
            holder.text_user_deliver_start_time_input = (TextView) convertView.findViewById(R.id.user_deliver_start_time_input);
            holder.text_user_deliver_complete_time_input = (TextView) convertView.findViewById(R.id.user_deliver_complete_time_input);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.text_user_store_name_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_store_name_input());
        holder.text_user_store_number_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_store_number_input());
        holder.text_user_store_address_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_store_address_input());

        holder.text_user_order_price_sum_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_order_price_sum_input());
        holder.text_user_order_time_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_order_time_input());
        holder.text_user_pay_method_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_pay_method_input());
        holder.text_user_order_complete_time_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_order_complete_time_input());
        holder.text_user_cooking_complete_time_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_cooking_complete_time_input());
        holder.text_user_deliver_start_time_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_deliver_start_time_input());
        holder.text_user_deliver_complete_time_input.setText(Old_Orderlist.oldOrderProducts.get(position).getUser_deliver_complete_time_input());

        return convertView;
    }

    public class ViewHolder{
        private TextView text_user_store_name_input, text_user_store_number_input, text_user_store_address_input,
                text_user_order_price_sum_input, text_user_order_time_input, text_user_pay_method_input,
                text_user_order_complete_time_input, text_user_cooking_complete_time_input,
                text_user_deliver_start_time_input, text_user_deliver_complete_time_input;
    }
}
