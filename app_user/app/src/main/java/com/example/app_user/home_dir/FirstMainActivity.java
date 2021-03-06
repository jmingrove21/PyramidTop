package com.example.app_user.home_dir;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
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
import com.example.app_user.Item_dir.ToolbarInform;
import com.example.app_user.Item_dir.User;
import com.example.app_user.MyService;
import com.example.app_user.draw_dir.GpsActivity;
import com.example.app_user.util_dir.BackPressCloseHandler;
import com.example.app_user.util_dir.HomeFragment;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.people_dir.PeopleFragment;
import com.example.app_user.util_dir.Profile;
import com.example.app_user.R;
import com.example.app_user.Item_dir.Store;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.util_dir.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

//idea supported by jaehoon pae
public class FirstMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private BackPressCloseHandler backPressCloseHandler;
    public static int store_type = -1;
    int delete_order_index;
    Point point;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_main);

        //기존 사용자가 주문한 내역 삭제에 사용됐던 코드
        //삭제 변경 완료시 현재 주석 코드 삭제하여도 무방
//        if(UtilSet.order_delete_check_flag){
//            Intent delete_order_intent = new Intent();
//            delete_order_intent.getIntExtra("delete_order_index",-1);
//
//            if(delete_order_index != -1){
//                //서버에게 delete_order_index를 삭제하기 위해 요청하는 곳
//            }
//            UtilSet.order_delete_check_flag = false;
//        }

        permissionCheck();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        UtilSet.set_GPS_permission(lm, this);//GPS



        Intent intent_alert = new Intent(FirstMainActivity.this, MyService.class);
        startService(intent_alert);//알림

        backPressCloseHandler = new BackPressCloseHandler(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("음식 목록");
        ToolbarInform.setToolbar_inform("음식 목록");

        point = getScreenSize(FirstMainActivity.this);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        view = getLayoutInflater().inflate(R.layout.nav_header, null);
        UtilSet.set_Drawer(navigationView, view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                UtilSet.resfresh_mileage(view);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ListView listView = findViewById(R.id.first_listView);
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
        if (LoginLogoutInform.getLogin_flag() == 1) {
            switch (menuItem.getItemId()) {
                case R.id.old_olderlist:
                    getSupportActionBar().setTitle("지난 주문 내역");
                    getSupportFragmentManager().beginTransaction().replace(R.id.relative_container,
                            new Old_Orderlist()).commit();
                    break;
                case R.id.menu_idoption:
                    getSupportActionBar().setTitle("계정 설정");
                    getSupportFragmentManager().beginTransaction().replace(R.id.relative_container,
                            new Profile()).commit();
                    break;
                case R.id.menu_logout:
                    UtilSet.loginLogoutInform.setLogin_flag(0);
                    UtilSet.my_user=null;
                    UtilSet.delete_user_data();
                    Intent intent = new Intent(FirstMainActivity.this, FirstMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
            }
        } else {
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
                            getSupportActionBar().setTitle(ToolbarInform.getToolbar_inform());
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.relative_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void search_store(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchMainActivity.class);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
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
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, point.y / 8));

            ImageView imageView = view.findViewById(R.id.first_imageView);
            TextView textView_name = view.findViewById(R.id.first_name);

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

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "store_info");
                    jsonParam.put("user_lat", UtilSet.my_user.get_user_latitude());
                    jsonParam.put("user_long", UtilSet.my_user.get_user_longitude());
                    jsonParam.put("store_type", UtilSet.MENU_TYPE_ID[position]);
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
                                String minimum_order_price = jobj.get("minimum_order_price").toString();
                                String store_profile_img = jobj.get("store_profile_img").toString();
                                Store s = new Store(store_serial, store_name, store_branch_name, store_address, store_phone, minimum_order_price, distance, store_profile_img);
                                UtilSet.al_store.add(s);
                            }
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            store_type = position;
                            startActivityForResult(intent, 101);

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

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            ArrayList<String> arrayPermission = new ArrayList<>();

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (arrayPermission.size() > 0) {
                String[] strArray = new String[arrayPermission.size()];
                strArray = arrayPermission.toArray(strArray);
                ActivityCompat.requestPermissions(this, strArray, UtilSet.PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case UtilSet.PERMISSION_REQUEST_CODE: {
                if (grantResults.length < 1) {
                    Toast.makeText(this, "Failed get permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission is denied : " + permissions[i], Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void GPSonClick(View view) {
        if(UtilSet.my_user.get_user_latitude()==0.0||UtilSet.my_user.get_user_longitude()==0.0)
        {
            UtilSet.my_user.set_user_gps(UtilSet.latitude_gps,UtilSet.longitude_gps);
            Log.d("GPS Setting - my location update",String.valueOf(UtilSet.my_user.get_user_latitude())+" "+String.valueOf(UtilSet.my_user.get_user_longitude()));
        }
        Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
        startActivityForResult(intent, 101);
    }

}
