package com.example.app_user.home_dir;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app_user.util_dir.BackPressCloseHandler;
import com.example.app_user.util_dir.HomeFragment;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.people_dir.PeopleFragment;
import com.example.app_user.Profile;
import com.example.app_user.R;
import com.example.app_user.Item_dir.Store;
import com.example.app_user.Item_dir.UtilSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirstMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_main);

        backPressCloseHandler = new BackPressCloseHandler(this);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("음식 목록");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ListView listView = (ListView) findViewById(R.id.first_listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                get_store_information(position);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.old_olderlist:
                getSupportActionBar().setTitle("지난 주문 내역");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Old_Orderlist()).commit();
                break;
            case R.id.menu_idoption:
                getSupportActionBar().setTitle("계정 설정");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
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
                            UtilSet.target_store=null;
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_orderlist:
                            getSupportActionBar().setTitle("주문 현황");
                            UtilSet.target_store=null;
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_party:
                            getSupportActionBar().setTitle("참여 현황");
                            UtilSet.target_store=null;
                            selectedFragment = new PeopleFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return UtilSet.MENU_TYPE_IMAGE.length;
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
            view = getLayoutInflater().inflate(R.layout.activity_first_layout, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 180));

            ImageView imageView = (ImageView) view.findViewById(R.id.first_imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.first_name);

            imageView.setImageResource(UtilSet.MENU_TYPE_IMAGE[i]);
            textView_name.setText(UtilSet.MENU_TYPE_TEXT[i]);

            return view;
        }
    }

    public void get_store_information(final int position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_store.clear();
                    URL url = new URL(UtilSet.url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "store_info");
                //    jsonParam.put("user_lat", 37.282690);
                //    jsonParam.put("user_long", 127.050206);
                    jsonParam.put("user_lat", 37.267088);
                    jsonParam.put("user_long", 127.081193);
                    jsonParam.put("store_type", UtilSet.MENU_TYPE_ID[position]);
                    jsonParam.put("count", 5);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONArray jArray = new JSONArray(jsonReply);
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jobj = (JSONObject) jArray.get(i);
                                String store_serial = jobj.get("store_serial").toString();
                                String store_name = jobj.get("store_name").toString();
                                String store_branch_name = jobj.get("store_branch_name").toString();
                                String store_address = jobj.get("store_address").toString();
                                String store_phone=jobj.get("store_phone").toString();
                                String distance = jobj.get("distance").toString();
                                String minimum_order_price=jobj.get("minimum_order_price").toString();
                                String store_profile_img = jobj.get("store_profile_img").toString();
                                Store s = new Store(store_serial, store_name, store_branch_name, store_address, store_phone, minimum_order_price, distance,store_profile_img);
                                UtilSet.al_store.add(s);
                            }
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(intent, 101);
                            finish();
                        } catch (Exception e) {
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
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}