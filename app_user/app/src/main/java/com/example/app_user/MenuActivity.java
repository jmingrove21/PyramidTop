package com.example.app_user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,MenuCustomAdapter.OnArrayList{
    private DrawerLayout drawer;
    int index;
    int serial;


    public static ArrayList<MenuProductItem > menuArrayList;
    ArrayList<String> selectedItems=new ArrayList<>();
    ArrayList<String> selectedMenu=new ArrayList<>();
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
        index = intent.getIntExtra("index",0);
        serial = intent.getIntExtra("serial",0);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        UtilSet.al_store.get(index).setMenu_str();

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView text_store_name = (TextView) findViewById(R.id.store_name);
        TextView text_store_phone = (TextView) findViewById(R.id.store_phone);
        TextView text_store_building_name = (TextView) findViewById(R.id.store_building_name);
        ImageView imageView=(ImageView) findViewById(R.id.store_image);

        imageView.setImageBitmap(UtilSet.al_store.get(index).getStore_image());
        text_store_name.setText(UtilSet.al_store.get(index).getStore_name());
        text_store_phone.setText(UtilSet.al_store.get(index).getStore_phone());
        text_store_building_name.setText(UtilSet.al_store.get(index).getStore_building_name());

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

    public void setFrag(int n){
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
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
        switch(menuItem.getItemId()){
            case R.id.old_olderlist:
                getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
                        new Old_Orderlist()).commit();
                break;
            case R.id.menu_idoption:
                getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
                        new Profile()).commit();
                break;
            case R.id.menu_logout:
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
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
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_orderlist:
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_party:
                            selectedFragment = new PeopleFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout_container,
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

    @Override
    public void onArrayList(ArrayList<String> Items){
        selectedItems = Items;
    }

    public void showSelectedItems(View view){

        Intent intent = getIntent();
        selectedMenu = intent.getStringArrayListExtra("selectedMenuList");
        int order_total = intent.getIntExtra("select_order_total",0);

        String items="";
        for(String item:selectedMenu){
            items+="-" + item + "\n";
        }
        items+="-" + order_total + "원\n";
        store_info_specification(selectedMenu);

        Toast.makeText(view.getContext(),"You have selected \n"+items,Toast.LENGTH_LONG).show();

    }
    public void store_info_specification(final ArrayList<String> getSelectedItems) {
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
                    JSONArray jArry=new JSONArray();
                    jsonParam.put("user_info", "make_order");
                    jsonParam.put("user_serial", 3);
                    jsonParam.put("store_serial", UtilSet.al_store.get(index).getStore_serial());
                    jsonParam.put("destination", "경기도 수원시 영통구 원천동 35 원천주공아파트");
                    jsonParam.put("destination_lat", 37.277218);
                    jsonParam.put("destination_long", 127.046708);

                    for(int idx=0;idx<UtilSet.al_store.get(index).getMenu_desc_al().size();idx++){
                        for(int j=0;j<getSelectedItems.size();j++){
                            if(UtilSet.al_store.get(index).getMenu_desc_al().get(idx).getMenu_name().equals(getSelectedItems.get(j))){
                                JSONObject jobj_temp=new JSONObject();
                                jobj_temp.put("menu_code",UtilSet.al_store.get(index).getMenu_desc_al().get(idx).getMenu_code());
                                jobj_temp.put("menu_name",UtilSet.al_store.get(index).getMenu_desc_al().get(idx).getMenu_name());
                                jobj_temp.put("menu_price",UtilSet.al_store.get(index).getMenu_desc_al().get(idx).getMenu_price());
                                jArry.put(jobj_temp);
                                break;
                            }
                        }

                    }

                    jsonParam.put("menu", jArry);

                    Log.i("JSON", jsonParam.toString());
                    OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
                    os.write(jsonParam.toString());

                    os.flush();
                    os.close();
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONObject jobj=new JSONObject(jsonReply);
                        String json_result=jobj.getString("confirm");
                        if(json_result.equals("1")){
                            System.out.println("Success order make");

                        }else{
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
        });
        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}