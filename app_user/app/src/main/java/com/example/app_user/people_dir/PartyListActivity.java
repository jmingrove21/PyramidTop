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

import com.example.app_user.Item_dir.Store;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.Profile;
import com.example.app_user.R;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.home_dir.MainActivity;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.util_dir.HomeFragment;
import com.example.app_user.util_dir.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class PartyListActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    int[] FIRSTIMAGES = {1,2,3,4};
    String[] FIRSTNAMES = {"돈가스,일식","양식","중식","한식"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("참여 현황");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ListView listView = (ListView)findViewById(R.id.first_listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                store_info_specification();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.old_olderlist:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Old_Orderlist()).commit();
                break;
            case R.id.menu_idoption:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Profile()).commit();
                break;
            case R.id.menu_logout:
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,101);
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
                    switch(item.getItemId()){
                        case R.id.nav_home:
                            UtilSet.target_store=null;
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_orderlist:
                            UtilSet.target_store=null;
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_party:
                            UtilSet.target_store=null;
                            selectedFragment = new PeopleFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return FIRSTIMAGES.length;
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
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,300));

            ImageView imageView = (ImageView) view.findViewById(R.id.first_imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.first_name);

//            imageView.setImageResource();
              textView_name.setText(FIRSTNAMES[i]);

            return view;
        }
    }
    public void store_info_specification() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "store_info");
                    jsonParam.put("user_lat", 37.2799);
                    jsonParam.put("user_long", 127.0435);
                    jsonParam.put("store_type","한식");
                    jsonParam.put("count",1);

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        try {
                            JSONArray jArray = new JSONArray(jsonReply);
                            for (int i = 0; i < jArray.length(); i++) {
                                String store_serial=((JSONObject) jArray.get(i)).get("store_serial").toString();
                                String store_name=((JSONObject) jArray.get(i)).get("store_name").toString();
                                String store_branch_name=((JSONObject) jArray.get(i)).get("store_branch_name").toString();
                                String store_address=((JSONObject) jArray.get(i)).get("store_address").toString();
                                String store_phone = ((JSONObject) jArray.get(i)).get("store_phone").toString();
                                String distance=((JSONObject) jArray.get(i)).get("distance").toString();

                                String store_building_name = ((JSONObject) jArray.get(i)).get("store_building_name").toString();
                                String start_time =((JSONObject) jArray.get(i)).get("start_time").toString();
                                String end_time = ((JSONObject) jArray.get(i)).get("end_time").toString();
                                String store_restday = ((JSONObject) jArray.get(i)).get("store_restday").toString();
                                String store_notice = ((JSONObject) jArray.get(i)).get("store_notice").toString();
                                String store_profile_img = ((JSONObject) jArray.get(i)).get("store_profile_img").toString();
                                String store_main_type_name = ((JSONObject) jArray.get(i)).get("store_main_type_name").toString();
                                Store s = new Store(store_serial, store_name, store_branch_name, store_address, store_phone, distance);
                                //s.set_store_spec(store_address,store_building_name,start_time, end_time, store_restday, store_notice, store_profile_img, store_main_type_name);
                                UtilSet.al_store.add(s);
                            }

                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(intent,101);
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
    }
}