package com.example.app_user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;

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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MenuCustomAdapter.OnArrayList {
    private DrawerLayout drawer;
    private Bitmap bitmap;
    int index;
    int serial;
    private String type; //order_make, order_participate

    String selectedMenu;
    Button store_inform_button, menu_list_button;
    FragmentManager fm;
    FragmentTransaction tran;
    MenuFragment menuFragment;
    StoreDetailFragment storedetailfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_store_menu);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        index = intent.getIntExtra("index", 0);
        serial = intent.getIntExtra("serial", 0);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        UtilSet.target_store.setMenu_str();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("메뉴 선택");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        store_inform_button = (Button) findViewById(R.id.store_inform_button);
        menu_list_button = (Button) findViewById(R.id.menu_list_button);

        storedetailfragment = new StoreDetailFragment();
        storedetailfragment.setIndex(index);

        menuFragment = new MenuFragment();
        menuFragment.setIndex(index);

        setFrag(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView text_store_name = (TextView) findViewById(R.id.store_name);
        TextView text_store_phone = (TextView) findViewById(R.id.store_phone);
        TextView text_store_branch_name = (TextView) findViewById(R.id.store_branch_name);
        ImageView imageView = (ImageView) findViewById(R.id.store_image);

        imageView.setImageBitmap(UtilSet.target_store.getStore_image());
        text_store_name.setText(UtilSet.target_store.getStore_name());
        text_store_phone.setText(UtilSet.target_store.getStore_phone());
        text_store_branch_name.setText(UtilSet.target_store.getStore_branch_name());

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
        switch (menuItem.getItemId()) {
            case R.id.old_olderlist:
                getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
                        new Old_Orderlist()).commit();
                break;
            case R.id.menu_idoption:
                getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
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
                            UtilSet.target_store = null;
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_party:
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
            super.onBackPressed();
        }
    }


    public void showSelectedItems(View view) {

        store_info_specification(view);

    }

    public void store_info_specification(View view) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(UtilSet.url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    JSONArray jArry = new JSONArray();
                    jsonParam.put("user_info", "make_order");
                    jsonParam.put("user_serial", UtilSet.user_serial);
                    jsonParam.put("store_serial", UtilSet.target_store.getStore_serial());
                    jsonParam.put("order_number",UtilSet.target_store.getOrder_number());
                    jsonParam.put("destination", "경기도 수원시 팔달구 우만동 아주대학교");
                    jsonParam.put("destination_lat", 37.277999);
                    jsonParam.put("destination_long", 127.046799);

                    int total_price = 0;
                    if (MenuFragment.menuProductItems == null) {
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( MenuActivity.this, "선택메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                        return;
                    }

                    for (int idx = 0; idx < MenuFragment.menuProductItems.size(); idx++) {
                        if (MenuFragment.menuProductItems.get(idx).getOrder_number() != 0) {
                            JSONObject jobj_temp = new JSONObject();
                            jobj_temp.put("menu_code", MenuFragment.menuProductItems.get(idx).getMenu_code());
                            jobj_temp.put("menu_name", MenuFragment.menuProductItems.get(idx).getMenu_inform());
                            selectedMenu = "" + MenuFragment.menuProductItems.get(idx).getMenu_inform() + "\n";
                            jobj_temp.put("menu_count", MenuFragment.menuProductItems.get(idx).getOrder_number());
                            jobj_temp.put("menu_price", MenuFragment.menuProductItems.get(idx).getPrice_inform());
                            int menu_total_price = Integer.parseInt(MenuFragment.menuProductItems.get(idx).getPrice_inform()) * MenuFragment.menuProductItems.get(idx).getOrder_number();
                            total_price += menu_total_price;
                            jobj_temp.put("menu_total_price", menu_total_price);
                            jArry.put(jobj_temp);
                        }
                    }
                    if(total_price==0) {
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( MenuActivity.this, "선택메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    final String str = Integer.toString(total_price);
                    if(total_price!=0) {
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText( MenuActivity.this, str+"원 주문생성", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    jsonParam.put("total_price", total_price);
                    jsonParam.put("menu", jArry);

                    Log.i("JSON", jsonParam.toString());
                    OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    os.write(jsonParam.toString());

                    os.flush();
                    os.close();
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj = new JSONObject(jsonReply);
                        String json_result = jobj.getString("confirm");
                        if (json_result.equals("1")) {
                            System.out.println("Success order make");

                        } else {
                            Log.d("error", "Responce code : 0 - fail make order");
                        }
                    } else {
                        Log.d("error", "Connect fail");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            private Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    Toast.makeText(getApplicationContext(), "You Don't have any selected\n", Toast.LENGTH_LONG).show();
                    super.handleMessage(msg);
                }
            };
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}