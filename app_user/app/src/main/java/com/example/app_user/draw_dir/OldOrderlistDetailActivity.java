package com.example.app_user.draw_dir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.MenuDesc;
import com.example.app_user.Item_dir.Order;
import com.example.app_user.Item_dir.Store;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.util_dir.Profile;
import com.example.app_user.R;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class OldOrderlistDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    int index;
    int serial;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_old_orderlist_detail_layout);
        Intent intent = getIntent();
        //type = intent.getStringExtra("type");
        index = intent.getIntExtra("index", 0);
        serial = intent.getIntExtra("serial", 0);
        get_my_order_list(UtilSet.al_my_old_order.get(index).getStore().getStore_serial(), index);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("이전 주문 내역");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        view = getLayoutInflater().inflate(R.layout.nav_header, null);
        UtilSet.set_Drawer(navigationView,view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                resfresh_mileage(view);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ListView listView = findViewById(R.id.old_olderlist_detail_layout_listview);
        OldOrderlistDetailActivity.CustomAdapter customAdapter = new OldOrderlistDetailActivity.CustomAdapter();
        listView.setAdapter(customAdapter);

        TextView text_user_store_name_input = findViewById(R.id.user_store_name_input);
        TextView text_user_store_number_input = findViewById(R.id.user_store_number_input);
        TextView text_user_store_address_input = findViewById(R.id.user_store_address_input);

        TextView text_user_order_price_sum_input = findViewById(R.id.user_order_price_sum_input);
        TextView text_user_order_time_input = findViewById(R.id.user_order_time_input);
        TextView text_user_pay_method_input = findViewById(R.id.user_pay_method_input);
        TextView text_user_order_complete_time_input = findViewById(R.id.user_order_complete_time_input);
        TextView text_user_deliver_start_time_input = findViewById(R.id.user_deliver_start_time_input);
        TextView text_user_deliver_complete_time_input = findViewById(R.id.user_deliver_complete_time_input);

        Order o = UtilSet.al_my_old_order.get(index);
        Store s = o.getStore();

        text_user_store_name_input.setText(s.getStore_name());
        text_user_store_number_input.setText(s.getStore_phone());
        text_user_store_address_input.setText(s.getStore_address());
        o.setMy_order_total_price(0);
        for (int i = 0; i < o.getStore().getMenu_desc_al().size(); i++) {
            o.setMy_order_total_price(o.getStore().getMenu_desc_al().get(i).getMenu_price() * o.getStore().getMenu_desc_al().get(i).getMenu_count());
        }

        text_user_order_price_sum_input.setText(o.getMy_order_total_price() +"원");
        text_user_order_time_input.setText(o.getOrder_create_date());
        text_user_pay_method_input.setText("Card~");
        text_user_order_complete_time_input.setText(o.getOrder_receipt_date());
        text_user_deliver_start_time_input.setText(o.getDelivery_departure_time());
        text_user_deliver_complete_time_input.setText(o.getDelivery_arrival_time());
    }
    public void resfresh_mileage(View view){
        TextView user_mil= view.findViewById(R.id.user_mileage);
        user_mil.setText("마일리지 : "+UtilSet.my_user.getUser_mileage()+"원");
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(LoginLogoutInform.getLogin_flag()==1){
            switch (menuItem.getItemId()) {
                case R.id.old_olderlist:
                    getSupportActionBar().setTitle("지난 주문 내역");
                    getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout_container,
                            new Old_Orderlist()).commit();
                    break;
//                case R.id.menu_idoption:
//                    getSupportActionBar().setTitle("계정 설정");
//                    getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout_container,
//                            new Profile()).commit();
//                    break;

                case R.id.menu_logout:
                    UtilSet.loginLogoutInform.setLogin_flag(0);
                    UtilSet.my_user=null;
                    UtilSet.delete_user_data();
                    Intent intent=new Intent(OldOrderlistDetailActivity.this, FirstMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
            }
        }else{
            switch (menuItem.getItemId()) {
                case R.id.menu_register:
                    Intent register_intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivityForResult(register_intent, 101);
                    break;

                case R.id.menu_login:
                    Intent login_intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(login_intent, 101);
                    break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return UtilSet.al_my_old_order.get(index).getStore().getMenu_desc_al().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.detail_layout_menu_listview, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 70));


            TextView text_name = view.findViewById(R.id.menu_name);
            TextView text_menu_count_input = view.findViewById(R.id.menu_count_input);
            TextView text_menu_price_input = view.findViewById(R.id.menu_price_input);


            text_name.setText(UtilSet.al_my_old_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_name());
            text_menu_count_input.setText(String.valueOf(UtilSet.al_my_old_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_count()));
            text_menu_price_input.setText(String.valueOf(UtilSet.al_my_old_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_price()*UtilSet.al_my_old_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_count()));

            return view;
        }
    }

    public void get_my_order_list(final int store_serial, final int position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_my_old_order.get(position).getStore().getMenu_desc_al().clear();
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "ordered_list_detail");
                    jsonParam.put("order_info",0);
                    jsonParam.put("store_serial", UtilSet.al_my_old_order.get(position).getStore().getStore_serial());
                    jsonParam.put("order_number", UtilSet.al_my_old_order.get(position).getOrder_number());
                    jsonParam.put("user_serial",UtilSet.my_user.getUser_serial());
                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONObject jobj = new JSONObject(jsonReply);
                            JSONObject jarray_user = (JSONObject) jobj.get("user_info");
                            String arrival_time = jarray_user.get("arrival_time").toString();
                            JSONArray jarray_user_menu = (JSONArray) jarray_user.get("user_menu");
                            for (int j = 0; j < jarray_user_menu.length(); j++) {
                                JSONObject jobj_user_menu_info = (JSONObject) jarray_user_menu.get(j);
                                String menu_name = jobj_user_menu_info.get("menu_name").toString();
                                String menu_count = jobj_user_menu_info.get("menu_count").toString();
                                String menu_price = jobj_user_menu_info.get("menu_price").toString();
                                MenuDesc md = new MenuDesc(menu_name, Integer.parseInt(menu_count), Integer.parseInt(menu_price));
                                UtilSet.al_my_old_order.get(position).getStore().getMenu_desc_al().add(md);
                            }

                            JSONObject jobj_store = new JSONObject(jobj.get("store_info").toString());
                            String store_serial = jobj_store.get("store_serial").toString();
                            String store_building_name = jobj_store.get("store_building_name").toString();

                            String start_time = jobj_store.get("start_time").toString();
                            String end_time = jobj_store.get("end_time").toString();
                            String store_phone = jobj_store.get("store_phone").toString();
                            String store_address = jobj_store.get("store_address").toString();
                            String store_restday = jobj_store.get("store_restday").toString();
                            String store_notice = jobj_store.get("store_notice").toString();
                            UtilSet.al_my_old_order.get(position).setDelivery_arrival_time(arrival_time);
                            UtilSet.al_my_old_order.get(position).getStore().set_store_spec(store_serial, store_building_name, start_time, end_time, store_phone, store_address, store_restday, store_notice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("error", "Connect fail");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void GPSonClick(View view){
        if(UtilSet.my_user.get_user_latitude()==0.0||UtilSet.my_user.get_user_longitude()==0.0)
        {
            UtilSet.my_user.set_user_gps(UtilSet.latitude_gps,UtilSet.longitude_gps);
            Log.d("GPS Setting - my location update",String.valueOf(UtilSet.my_user.get_user_latitude())+" "+String.valueOf(UtilSet.my_user.get_user_longitude()));
        }
        Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
        startActivityForResult(intent, 101);
    }
}