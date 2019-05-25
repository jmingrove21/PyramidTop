package com.example.app_user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PartyDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    int type,index,serial;
 //   private String str = "Test";
    private String[] test = {"1","2","3","4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_detail_layout);

        Intent intent = getIntent();
        //type = intent.getStringExtra("type");
        index = intent.getIntExtra("index", 0);
       // serial = intent.getIntExtra("serial", 0);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        UtilSet.target_store.setMenu_str();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("참여 현황");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ListView listView = (ListView) findViewById(R.id.party_detail_layout_listview);
        PartyDetailActivity.CustomAdapter customAdapter = new PartyDetailActivity.CustomAdapter();
        listView.setAdapter(customAdapter);

        TextView text_other_user1 = (TextView) findViewById(R.id.other_user1);
        TextView text_user1_party_time_input = (TextView) findViewById(R.id.user1_party_time_input);
        TextView text_user1_order_price_input = (TextView) findViewById(R.id.user1_order_price_input);

        TextView text_other_user2 = (TextView) findViewById(R.id.other_user2);
        TextView text_user2_party_time_input = (TextView) findViewById(R.id.user2_party_time_input);
        TextView text_user2_order_price_input = (TextView) findViewById(R.id.user2_order_price_input);

        TextView text_user = (TextView) findViewById(R.id.user);
        TextView text_user_store_name_input = (TextView) findViewById(R.id.user_store_name_input);
        TextView text_user_store_number_input = (TextView) findViewById(R.id.user_store_number_input);
        TextView text_user_store_address_input = (TextView) findViewById(R.id.user_store_address_input);

        TextView text_user_order_price_sum_input = (TextView) findViewById(R.id.user_order_price_sum_input);
        TextView text_user_order_time_input = (TextView) findViewById(R.id.user_order_time_input);
        TextView text_user_pay_method_input = (TextView) findViewById(R.id.user_pay_method_input);
        TextView text_user_order_complete_time_input = (TextView) findViewById(R.id.user_order_complete_time_input);
        TextView text_user_cooking_complete_time_input = (TextView) findViewById(R.id.user_cooking_complete_time_input);
        TextView text_user_deliver_start_time_input = (TextView) findViewById(R.id.user_deliver_start_time_input);
        TextView text_user_deliver_complete_time_input = (TextView) findViewById(R.id.user_deliver_complete_time_input);

        Order o=UtilSet.al_my_order.get(index);
//        text_other_user1.setText(str);
//        text_user1_party_time_input.setText(str);
//        text_user1_order_price_input.setText(str);
//
//        text_other_user2.setText(str);
//        text_user2_party_time_input.setText(str);
//        text_user2_order_price_input.setText(str);

        text_user.setText("유저이름");
        text_user_store_name_input.setText(o.getStore().getStore_name()+" "+o.getStore().getStore_branch_name());
        text_user_store_number_input.setText(o.getStore().getStore_phone());
        text_user_store_address_input.setText(o.getStore().getStore_address());

       // text_user_order_price_sum_input.setText(o.getTotal_order_price());
        text_user_order_time_input.setText(o.getOrder_create_date());
        text_user_pay_method_input.setText("pay method input");
        text_user_order_complete_time_input.setText(o.getOrder_receipt_date());
        text_user_cooking_complete_time_input.setText(o.getDelivery_request_time());
        text_user_deliver_start_time_input.setText(o.getDelivery_approve_time());
        text_user_deliver_complete_time_input .setText(o.getDelivery_departure_time());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 101);
                break;
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
            return test.length;
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
            view = getLayoutInflater().inflate(R.layout.party_detail_layout_menu_listview, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 30));

            TextView text_name = (TextView) view.findViewById(R.id.menu_name);
            TextView text_menu_count_input = (TextView) view.findViewById(R.id.menu_count_input);
            TextView text_menu_price_input = (TextView) view.findViewById(R.id.menu_price_input);


            text_name.setText(test[i]);
            text_menu_count_input.setText(test[i]);
            text_menu_price_input.setText(test[i]);

            return view;
        }
    }
}