package com.example.app_user.home_dir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.MenuDesc;
import com.example.app_user.Item_dir.Store;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.Profile;
import com.example.app_user.R;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.people_dir.PeopleFragment;
import com.example.app_user.util_dir.HomeFragment;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Bitmap bitmap;
    int store_ser;
    ListView listView;
    EditText search;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        if(LoginLogoutInform.getLogin_flag()==1){
            setContentView(R.layout.activity_search_layout);
        }else{
            setContentView(R.layout.logout_activity_search_layout);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("검색 목록");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        listView = (ListView) findViewById(R.id.listView);
        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < UtilSet.al_store.size(); i++) {
                    try {
                        if (UtilSet.getBitmapFromMemCache(UtilSet.al_store.get(i).getStore_profile_img()) != null) {
                            bitmap = UtilSet.getBitmapFromMemCache(UtilSet.al_store.get(i).getStore_profile_img());
                            UtilSet.al_store.get(i).setStore_image(bitmap);
                        } else {
                            URL url = new URL(UtilSet.al_store.get(i).getStore_profile_img());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                            UtilSet.al_store.get(i).setStore_image(bitmap);
                            UtilSet.addBitmapToMemoryCache(UtilSet.al_store.get(i).getStore_profile_img(), bitmap);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                store_ser = UtilSet.al_store.get(position).getStore_serial();
                store_info_detail(store_ser, position);
                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < UtilSet.al_store.get(position).getMenu_al().size(); i++) {
                            for (int j = 0; j < UtilSet.al_store.get(position).getMenu_al().get(i).getMenu_desc_al().size(); j++) {
                                try {
                                    if (UtilSet.getBitmapFromMemCache(UtilSet.al_store.get(position).getMenu_al().get(i).getMenu_desc_al().get(j).getMenu_img()) != null) {
                                        bitmap = UtilSet.getBitmapFromMemCache(UtilSet.al_store.get(position).getMenu_al().get(i).getMenu_desc_al().get(j).getMenu_img());
                                        UtilSet.al_store.get(position).getMenu_al().get(i).getMenu_desc_al().get(j).setMenu_image(bitmap);
                                    } else {
                                        URL url = new URL(UtilSet.al_store.get(position).getMenu_al().get(i).getMenu_desc_al().get(j).getMenu_img());
                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                        conn.setDoInput(true);
                                        conn.connect();

                                        InputStream is = conn.getInputStream();
                                        bitmap = BitmapFactory.decodeStream(is);
                                        UtilSet.al_store.get(position).getMenu_al().get(i).getMenu_desc_al().get(j).setMenu_image(bitmap);
                                        UtilSet.addBitmapToMemoryCache(UtilSet.al_store.get(position).getMenu_al().get(i).getMenu_desc_al().get(j).getMenu_img(), bitmap);
                                    }

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                };
                mThread.start();
                try {
                    mThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                UtilSet.target_store = UtilSet.al_store.get(position);
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("serial", store_ser);
                intent.putExtra("index", position);
                startActivityForResult(intent, 101);
            }
        });

        search = (EditText) findViewById(R.id.search_input);
    }

    public void searchButton(View view){
        if(search.getText().toString().length()<2){
            SearchMainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText( SearchMainActivity.this, "두 글자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            get_store_information();
        }
    }

    public void get_store_information() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_store.clear();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "search_store");
                    jsonParam.put("search",search.getText().toString());
                    jsonParam.put("user_lat", UtilSet.latitude);
                    jsonParam.put("user_long", UtilSet.longitude);

                    jsonParam.put("count", 5);

                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);
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
                                String store_phone = jobj.get("store_phone").toString();
                                String distance = jobj.get("distance").toString();
                                String store_profile_img = jobj.get("store_profile_img").toString();
                                Store s = new Store(store_serial, store_name, store_branch_name, store_address, store_phone, "", distance, store_profile_img);
                                UtilSet.al_store.add(s);
                            }
                            listView.findViewById(R.id.listView);
                            customAdapter = new CustomAdapter();
                            listView.setAdapter(customAdapter);

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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void store_info_detail(final int store_serial, final int position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_store.get(position).getMenu_al().clear();
                    UtilSet.al_store.get(position).getMenu_desc_al().clear();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "store_detail");
                    jsonParam.put("store_serial", store_serial);

                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONObject jobj = new JSONObject(jsonReply);
                            String store_building_name = jobj.get("store_building_name").toString();
                            String start_time = jobj.get("start_time").toString();
                            String end_time = jobj.get("end_time").toString();
                            String store_restday = jobj.get("store_restday").toString();
                            String store_notice = jobj.get("store_notice").toString();
                            String store_main_type_name = jobj.get("store_main_type_name").toString();
                            String store_sub_type_name = jobj.get("store_sub_type_name").toString();

                            UtilSet.al_store.get(position).set_store_spec(store_building_name, start_time, end_time, store_restday, store_notice, store_main_type_name, store_sub_type_name);

                            JSONArray jobj_menu = (JSONArray) jobj.get("menu");
                            for (int j = 0; j < jobj_menu.length(); j++) {
                                JSONObject jobj_menu_spec = (JSONObject) jobj_menu.get(j);
                                String menu_type_code = jobj_menu_spec.get("menu_type_code").toString();
                                String menu_type_name = jobj_menu_spec.get("menu_type_name").toString();
                                UtilSet.al_store.get(position).getMenu_al().add(new com.example.app_user.Item_dir.Menu(menu_type_code, menu_type_name));
                                JSONArray menu_menu_desc = (JSONArray) jobj_menu_spec.get("menu description");
                                for (int k = 0; k < menu_menu_desc.length(); k++) {
                                    JSONObject jobj_menu_desc_spec = (JSONObject) menu_menu_desc.get(k);
                                    String menu_code = jobj_menu_desc_spec.get("menu_code").toString();
                                    String menu_name = jobj_menu_desc_spec.get("menu_name").toString();
                                    int menu_price = Integer.parseInt(jobj_menu_desc_spec.get("menu_price").toString());
                                    String menu_img = jobj_menu_desc_spec.get("menu_img").toString();
                                    UtilSet.al_store.get(position).getMenu_al().get(j).getMenu_desc_al().add(new MenuDesc(menu_code, menu_name, menu_price, menu_img));
                                }
                            }
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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                            UtilSet.target_store = null;
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_orderlist:
                            getSupportActionBar().setTitle("주문 현황");
                            UtilSet.target_store = null;
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_party:
                            getSupportActionBar().setTitle("참여 현황");
                            UtilSet.target_store = null;
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

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return UtilSet.al_store.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 300));

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
            TextView textView_phone = (TextView) view.findViewById(R.id.textView_phone);
            TextView textView_branch_name = (TextView) view.findViewById(R.id.branch_name);
            TextView textView_address = (TextView) view.findViewById(R.id.address);

            imageView.setImageBitmap(UtilSet.al_store.get(i).getStore_image());
            textView_name.setText(UtilSet.al_store.get(i).getStore_name());
            textView_phone.setText(UtilSet.al_store.get(i).getStore_phone());
            textView_branch_name.setText(UtilSet.al_store.get(i).getStore_branch_name());
            textView_address.setText(UtilSet.al_store.get(i).getStore_address());
            return view;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(), FirstMainActivity.class);
            startActivityForResult(intent, 101);
            finish();
        }
    }
}