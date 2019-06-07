package com.example.app_user.people_dir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.Order;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.Profile;
import com.example.app_user.R;
import com.example.app_user.draw_dir.GpsActivity;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.draw_dir.PopupActivity;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

public class PartyDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private DrawerLayout drawer;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.party_detail_layout);
        /////
        findViewById(R.id.btnAlert).setOnClickListener(this);
        ////
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("참여 현황");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = getLayoutInflater().inflate(R.layout.nav_header, null);
        UtilSet.set_Drawer(navigationView,view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        ListView listView_menu = findViewById(R.id.party_detail_layout_menu_listview);
        PartyDetailActivity.CustomAdapter customAdapter = new PartyDetailActivity.CustomAdapter();
        listView_menu.setAdapter(customAdapter);


//        TextView text_user = (TextView) findViewById(R.id.user);
        TextView text_user_store_name_input = findViewById(R.id.user_store_name_input);
        TextView text_user_store_number_input = findViewById(R.id.user_store_number_input);
        TextView text_user_store_address_input = findViewById(R.id.user_store_address_input);
        TextView text_user_store_delivery_cost= findViewById(R.id.user_deilvery_cost);
        TextView text_user_order_price_sum_input = findViewById(R.id.user_order_price_sum_input);
        TextView text_user_order_time_input = findViewById(R.id.user_order_time_input);
        TextView text_user_pay_method_input = findViewById(R.id.user_pay_method_input);
        TextView text_user_order_complete_time_input = findViewById(R.id.user_order_complete_time_input);
        TextView text_user_deliver_start_time_input = findViewById(R.id.user_deliver_start_time_input);
        TextView text_user_deliver_complete_time_input = findViewById(R.id.user_deliver_complete_time_input);

        Order o = UtilSet.al_my_order.get(index);

        text_user_store_name_input.setText(o.getStore().getStore_name() + " " + o.getStore().getStore_branch_name());
        text_user_store_number_input.setText(o.getStore().getStore_phone());
        text_user_store_address_input.setText(o.getStore().getStore_address());
        text_user_store_delivery_cost.setText(o.getStore().getDelivery_cost() +"원");
        for (int i = 0; i < o.getStore().getMenu_desc_al().size(); i++) {
           o.setMy_order_total_price(o.getStore().getMenu_desc_al().get(i).getMenu_price() * o.getStore().getMenu_desc_al().get(i).getMenu_count());
        }

        text_user_order_price_sum_input.setText(o.getMy_order_total_price() + "원");
        text_user_order_time_input.setText(o.getOrder_create_date());
        text_user_pay_method_input.setText("pay method input");
        text_user_order_complete_time_input.setText(o.getOrder_receipt_date());
        text_user_deliver_start_time_input.setText(o.getDelivery_departure_time());
        text_user_deliver_complete_time_input.setText(o.getDelivery_arrival_time());
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
                case R.id.menu_idoption:
                    getSupportActionBar().setTitle("계정 설정");
                    getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout_container,
                            new Profile()).commit();
                    break;
                case R.id.menu_logout:
                    UtilSet.loginLogoutInform.setLogin_flag(0);
                    UtilSet.delete_user_data();
                    Intent intent=new Intent(PartyDetailActivity.this, FirstMainActivity.class);
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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent intent = new Intent(getApplicationContext(), FirstMainActivity.class);
                            startActivityForResult(intent, 101);
                            break;
                        case R.id.nav_orderlist:
                            getSupportActionBar().setTitle("주문 현황");
                            UtilSet.target_store = null;
                            selectedFragment = new OrderFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout_container,
                                    selectedFragment).commit();
                            break;
                        case R.id.nav_party:
                            getSupportActionBar().setTitle("참여 현황");
                            UtilSet.target_store = null;
                            selectedFragment = new PeopleFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout_container,
                                    selectedFragment).commit();
                            break;

                    }
                    return true;
                }
            };

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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAlert:
                Intent intent=new Intent(this, PopupActivity.class);
                intent.putExtra("index",index);
                startActivity(intent);
                break;
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().size();
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


            text_name.setText(UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_name());
            text_menu_count_input.setText(String.valueOf(UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_count()));
            text_menu_price_input.setText(String.valueOf(UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_price() * UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_count()));

            return view;
        }
    }


    public void GPSonClick(View view){
        Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
        startActivityForResult(intent, 101);
    }
}