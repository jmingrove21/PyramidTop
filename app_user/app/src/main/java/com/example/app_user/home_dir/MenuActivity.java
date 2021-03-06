package com.example.app_user.home_dir;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.ToolbarInform;
import com.example.app_user.draw_dir.GpsActivity;
import com.example.app_user.util_dir.CreditActivity;
import com.example.app_user.util_dir.HomeFragment;
import com.example.app_user.util_dir.LoginActivity;
import com.example.app_user.util_dir.MenuCustomAdapter;
import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.people_dir.PeopleFragment;
import com.example.app_user.util_dir.Profile;
import com.example.app_user.R;
import com.example.app_user.util_dir.RegisterActivity;
import com.example.app_user.util_dir.StoreDetailFragment;
import com.example.app_user.Item_dir.UtilSet;

import org.json.JSONArray;
import org.json.JSONObject;


public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MenuCustomAdapter.OnArrayList {
    private DrawerLayout drawer;
    int index;
    int serial;
    boolean flag = false;
    int total_price_send = 0;
    private String type; //order_make, order_participate

    String selectedMenu;
    ImageButton store_inform_button, menu_list_button;
    FragmentManager fm;
    FragmentTransaction tran;
    MenuFragment menuFragment;
    StoreDetailFragment storedetailfragment;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_store_menu);


        Intent intent = getIntent();
        type = intent.getStringExtra("order_make");
        index = intent.getIntExtra("index", 0);
        serial = intent.getIntExtra("serial", 0);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        UtilSet.target_store.setMenu_str();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(UtilSet.target_store.getStore_name() + " " + UtilSet.target_store.getStore_branch_name());
        ToolbarInform.setToolbar_inform(UtilSet.target_store.getStore_name() + " " + UtilSet.target_store.getStore_branch_name());

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        view = getLayoutInflater().inflate(R.layout.nav_header, null);
        UtilSet.set_Drawer(navigationView, view);


        navigationView.setNavigationItemSelectedListener(this);

        store_inform_button = findViewById(R.id.store_inform_button);
        menu_list_button = findViewById(R.id.menu_list_button);

        storedetailfragment = new StoreDetailFragment();
        storedetailfragment.setIndex(index);

        menuFragment = new MenuFragment();
        menuFragment.setIndex(index);

        setFrag(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                resfresh_mileage(view);
            }
        };        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ImageView imageView = findViewById(R.id.store_image);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);

        imageView.setImageBitmap(UtilSet.target_store.getStore_image());

        store_inform_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrag(0);
            }
        });
        menu_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrag(1);
            }
        });
    }
    public void resfresh_mileage(View view){
        TextView user_mil= view.findViewById(R.id.user_mileage);
        user_mil.setText("마일리지 : "+UtilSet.my_user.getUser_mileage()+"원");
    }
    public void setFrag(int n) {
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n) {
            case 0:
                tran.replace(R.id.sub_fragment_container, storedetailfragment);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.sub_fragment_container, menuFragment);
                tran.commit();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (LoginLogoutInform.getLogin_flag() == 1) {
            switch (menuItem.getItemId()) {
                case R.id.old_olderlist:
                    getSupportActionBar().setTitle("지난 주문 내역");
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
                            new Old_Orderlist()).commit();
                    break;
                case R.id.menu_idoption:
                    getSupportActionBar().setTitle("계정 설정");
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
                            new Profile()).commit();
                    break;
                case R.id.menu_logout:
                    UtilSet.loginLogoutInform.setLogin_flag(0);
                    UtilSet.my_user=null;
                    UtilSet.delete_user_data();
                    Intent intent = new Intent(MenuActivity.this, FirstMainActivity.class);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(), FirstMainActivity.class);
            startActivityForResult(intent, 101);
            finish();
        }
    }

    public void showSelectedItems(View view) {
        make_order(view);
    }

    public void make_order(View view) {
        flag = false;
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(LoginLogoutInform.getLogin_flag()==0||UtilSet.my_user==null){
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MenuActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                        return;
                    }
                    JSONObject jsonParam = new JSONObject();
                    JSONArray jArry = new JSONArray();
                    jsonParam.put("user_info", "make_order");
                    jsonParam.put("user_serial", UtilSet.my_user.getUser_serial());
                    jsonParam.put("store_serial", UtilSet.target_store.getStore_serial());
                    jsonParam.put("order_number", UtilSet.target_store.getOrder_number());
                    jsonParam.put("destination", UtilSet.my_user.getUser_address());
                    jsonParam.put("destination_lat", UtilSet.my_user.get_user_latitude());
                    jsonParam.put("destination_long", UtilSet.my_user.get_user_longitude());

                    int total_price = 0;
                    if (menuFragment.menuProductItems == null) {
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MenuActivity.this, "선택메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                        return;
                    }

                    for (int idx = 0; idx < menuFragment.menuProductItems.size(); idx++) {
                        if (menuFragment.menuProductItems.get(idx).getOrder_number() != 0) {
                            JSONObject jobj_temp = new JSONObject();
                            jobj_temp.put("menu_code", menuFragment.menuProductItems.get(idx).getMenu_code());
                            jobj_temp.put("menu_name", menuFragment.menuProductItems.get(idx).getMenu_inform());
                            selectedMenu = "" + menuFragment.menuProductItems.get(idx).getMenu_inform() + "\n";
                            jobj_temp.put("menu_count", menuFragment.menuProductItems.get(idx).getOrder_number());
                            jobj_temp.put("menu_price", menuFragment.menuProductItems.get(idx).getPrice_inform());
                            int menu_total_price = Integer.parseInt(menuFragment.menuProductItems.get(idx).getPrice_inform()) * menuFragment.menuProductItems.get(idx).getOrder_number();
                            total_price += menu_total_price;
                            jobj_temp.put("menu_total_price", menu_total_price);
                            jArry.put(jobj_temp);
                        }
                    }
                    if (total_price == 0) {
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MenuActivity.this, "선택메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                        return;
                    }

                    jsonParam.put("total_price", total_price);
                    jsonParam.put("menu", jArry);
                    if (UtilSet.my_user.getUser_address() == null) {
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MenuActivity.this, "배달받을 주소를 설정해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    Intent intent = new Intent(getApplicationContext(), CreditActivity.class);
                    intent.putExtra("total_price", total_price);
                    intent.putExtra("mileage", UtilSet.my_user.getUser_mileage());
                    intent.putExtra("delivery_cost", UtilSet.target_store.getDelivery_cost());
                    intent.putExtra("json", jsonParam.toString());
                    startActivityForResult(intent, 101);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });thread.start();

    }
    public void GPSonClick(View view) {
        if(UtilSet.my_user.get_user_latitude()==0.0||UtilSet.my_user.get_user_longitude()==0.0) {
            UtilSet.my_user.set_user_gps(UtilSet.latitude_gps, UtilSet.longitude_gps);
            Log.d("GPS Setting - my location update", String.valueOf(UtilSet.my_user.get_user_latitude()) + " " + String.valueOf(UtilSet.my_user.get_user_longitude()));
        }
        Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
        startActivityForResult(intent, 101);
    }
}