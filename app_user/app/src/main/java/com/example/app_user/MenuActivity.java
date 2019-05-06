package com.example.app_user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, MenuListFragment.OnArrayList{
    private DrawerLayout drawer;
    int index;
    int serial;
    ArrayList<String> selectedItems = new ArrayList<>();
    Button store_inform_button, menu_list_button;
    FragmentManager fm;
    FragmentTransaction tran;
    MenuListFragment menulistfragment;
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

        menulistfragment = new MenuListFragment();
        menulistfragment.setIndex(index);


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
                tran.replace(R.id.sub_fragment_container, menulistfragment);
                tran.commit();
                break;
        }
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

    @Override
    public void onArrayList(ArrayList<String> Items){
        selectedItems = Items;
    }

    public void showSelectedItems(View view){
        String items="";
        for(String item:selectedItems){
            items+="-" + item + "\n";
        }
        Toast.makeText(view.getContext(),"You have selected \n"+items,Toast.LENGTH_LONG).show();
    }

}