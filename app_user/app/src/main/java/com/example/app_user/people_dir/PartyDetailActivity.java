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
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

public class PartyDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.party_detail_layout);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("참여 현황");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (UtilSet.loginLogoutInform.getLogin_flag() == 1) {
            navigationView.inflateMenu(R.menu.drawer_menu);
            View view=getLayoutInflater().inflate(R.layout.nav_header,null);
            TextView user_id=(TextView)view.findViewById(R.id.user_id);
            user_id.setText(UtilSet.my_user.getUser_name()+"님 반갑습니다!");
            TextView user_address=(TextView)view.findViewById(R.id.user_address);
            user_address.setText("수원시주소~");
            TextView hello_msg=(TextView)view.findViewById(R.id.please_login_text);
            hello_msg.setText(" ");
            navigationView.addHeaderView(view);
        } else {
            navigationView.inflateMenu(R.menu.logout_drawer_menu);
            View view=getLayoutInflater().inflate(R.layout.nav_header,null);

            ImageButton gps_btn = (ImageButton)view.findViewById(R.id.GPS_imageBtn);
            gps_btn.setVisibility(View.INVISIBLE);

            TextView user_id=(TextView)view.findViewById(R.id.user_id);
            user_id.setText(" ");
            TextView user_address=(TextView)view.findViewById(R.id.user_address);
            user_address.setText(" ");
            TextView hello_msg=(TextView)view.findViewById(R.id.please_login_text);
            hello_msg.setText("배달ONE과 함께하세요!");
            navigationView.addHeaderView(view);
        }
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        ListView listView_user = (ListView) findViewById(R.id.party_detail_layout_user_listview);
//        PartyDetailActivity.UserAdapter userAdapter=new PartyDetailActivity.UserAdapter();
//        listView_user.setAdapter(userAdapter);
        ListView listView_menu = (ListView) findViewById(R.id.party_detail_layout_menu_listview);
        PartyDetailActivity.CustomAdapter customAdapter = new PartyDetailActivity.CustomAdapter();
        listView_menu.setAdapter(customAdapter);


//        TextView text_user = (TextView) findViewById(R.id.user);
        TextView text_user_store_name_input = (TextView) findViewById(R.id.user_store_name_input);
        TextView text_user_store_number_input = (TextView) findViewById(R.id.user_store_number_input);
        TextView text_user_store_address_input = (TextView) findViewById(R.id.user_store_address_input);

        TextView text_user_order_price_sum_input = (TextView) findViewById(R.id.user_order_price_sum_input);
        TextView text_user_order_time_input = (TextView) findViewById(R.id.user_order_time_input);
        TextView text_user_pay_method_input = (TextView) findViewById(R.id.user_pay_method_input);
        TextView text_user_order_complete_time_input = (TextView) findViewById(R.id.user_order_complete_time_input);
        TextView text_user_deliver_start_time_input = (TextView) findViewById(R.id.user_deliver_start_time_input);
        TextView text_user_deliver_complete_time_input = (TextView) findViewById(R.id.user_deliver_complete_time_input);

        Order o = UtilSet.al_my_order.get(index);

        text_user_store_name_input.setText(o.getStore().getStore_name() + " " + o.getStore().getStore_branch_name());
        text_user_store_number_input.setText(o.getStore().getStore_phone());
        text_user_store_address_input.setText(o.getStore().getStore_address());

        for (int i = 0; i < o.getStore().getMenu_desc_al().size(); i++) {
           o.setMy_order_total_price(o.getStore().getMenu_desc_al().get(i).getMenu_price() * o.getStore().getMenu_desc_al().get(i).getMenu_count());
        }

        text_user_order_price_sum_input.setText(String.valueOf(o.getMy_order_total_price()) + "원");
        text_user_order_time_input.setText(o.getOrder_create_date());
        text_user_pay_method_input.setText("pay method input");
        text_user_order_complete_time_input.setText(o.getOrder_receipt_date());
        text_user_deliver_start_time_input.setText(o.getDelivery_departure_time());
        text_user_deliver_complete_time_input.setText(o.getDelivery_arrival_time());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(UtilSet.loginLogoutInform.getLogin_flag()==1){
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

            TextView text_name = (TextView) view.findViewById(R.id.menu_name);
            TextView text_menu_count_input = (TextView) view.findViewById(R.id.menu_count_input);
            TextView text_menu_price_input = (TextView) view.findViewById(R.id.menu_price_input);


            text_name.setText(UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_name());
            text_menu_count_input.setText(String.valueOf(UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_count()));
            text_menu_price_input.setText(String.valueOf(UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_price() * UtilSet.al_my_order.get(index).getStore().getMenu_desc_al().get(i).getMenu_count()));

            return view;
        }
    }

    class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return UtilSet.al_my_order.get(index).getUser_al().size();
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
            view = getLayoutInflater().inflate(R.layout.party_detail_layout_user_listview, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100));

            TextView text_name = (TextView) view.findViewById(R.id.party_detail_user_name);
            TextView text_user_time = (TextView) view.findViewById(R.id.party_detail_user_time);
            TextView text_user_price = (TextView) view.findViewById(R.id.party_detail_user_price);


            text_name.setText(UtilSet.al_my_order.get(index).getUser_al().get(i).getUser_id());
            text_user_time.setText(String.valueOf(UtilSet.al_my_order.get(index).getUser_al().get(i).getUser_time()));
            text_user_price.setText(String.valueOf(UtilSet.al_my_order.get(index).getUser_al().get(i).getUser_price()));

            return view;
        }
    }
    public void GPSonClick(View view){
        Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
        startActivityForResult(intent, 101);
    }
}