package com.example.app_user.util_dir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app_user.draw_dir.Old_Orderlist;
import com.example.app_user.order_dir.OrderFragment;
import com.example.app_user.people_dir.PeopleFragment;
import com.example.app_user.Profile;
import com.example.app_user.R;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.home_dir.FirstMainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private DrawerLayout drawer;
    Bitmap bitmap;
    int store_ser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("검색");


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ListView listView = (ListView)findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        Thread mThread=new Thread(){
            @Override
            public void run(){
                for(int i = 0; i< UtilSet.al_store.size(); i++){
                    try{
                        if(UtilSet.al_store.get(i).getStore_profile_img().equals("null")){
                            Drawable drawable = getResources().getDrawable(R.drawable.no_image);
                            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                            UtilSet.al_store.get(i).setStore_image(bitmap);
                            continue;
                        }

                        URL url=new URL(UtilSet.al_store.get(i).getStore_profile_img());
                        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is=conn.getInputStream();
                        bitmap= BitmapFactory.decodeStream(is);
                        UtilSet.al_store.get(i).setStore_image(bitmap);
                    }catch(MalformedURLException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                store_ser = UtilSet.al_store.get(position).getStore_serial();
                Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
                intent.putExtra("serial",store_ser);
                intent.putExtra("index",position);
                startActivityForResult(intent,101);
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
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,600));

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
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            Intent intent=new Intent(getApplicationContext(), FirstMainActivity.class);
            startActivityForResult(intent,101);
            finish();
        }
    }
}
