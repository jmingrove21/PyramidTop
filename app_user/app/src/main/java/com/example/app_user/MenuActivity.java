package com.example.app_user;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    int index;
    int serial;
    ArrayList<String> selectedItems = new ArrayList<>();
    int[] IMAGES = {R.drawable.alchon};
    String[] data={"a","b","c","d","e","f","g","h"};
    ListView listView;

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        listView = (ListView) findViewById(R.id.checkbox_ListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.menulayout,R.id.checkbox_layout,data);

        listView.setAdapter(adapter);

        TextView text_store_name = (TextView) findViewById(R.id.store_name);
        TextView text_store_phone = (TextView) findViewById(R.id.store_phone);
        TextView text_store_building_name = (TextView) findViewById(R.id.store_building_name);
        TextView text_store_rest = (TextView) findViewById(R.id.store_rest_day);
        TextView text_store_branch_name = (TextView) findViewById(R.id.store_branch_name);
        TextView text_store_address = (TextView) findViewById(R.id.store_address);
        TextView text_store_operation_start_time = (TextView) findViewById(R.id.store_operation_start_time);
        TextView text_store_operation_end_time = (TextView) findViewById(R.id.store_operation_end_time);
        TextView text_store_notice = (TextView) findViewById(R.id.store_notice);
        ImageView imageView=(ImageView) findViewById(R.id.store_image);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if(selectedItems.contains(selectedItem)){
                    selectedItems.remove(selectedItem);
                }else{
                    selectedItems.add(selectedItem);
                }
            }
        });

        imageView.setImageBitmap(UtilSet.al_store.get(index).getStore_image());
        text_store_name.setText(UtilSet.al_store.get(index).getStore_name());
        text_store_phone.setText(UtilSet.al_store.get(index).getStore_phone());
        text_store_building_name.setText(UtilSet.al_store.get(index).getStore_building_name());
        text_store_rest.setText(UtilSet.al_store.get(index).getStore_restday());
        text_store_branch_name.setText(UtilSet.al_store.get(index).getStore_branch_name());
        text_store_address.setText(UtilSet.al_store.get(index).getStore_address());
        text_store_operation_start_time.setText(UtilSet.al_store.get(index).getStart_time());;
        text_store_operation_end_time.setText(UtilSet.al_store.get(index).getEnd_time());
        text_store_notice.setText(UtilSet.al_store.get(index).getStore_notice());
    }

    public void showSelectedItems(View view){
        String items="";
        for(String item:selectedItems){
            items+="-"+item+"\n";
        }

        Toast.makeText(this,"You have selected\n"+items,Toast.LENGTH_LONG).show();
    }

    public void confirm(View v){
        SparseBooleanArray booleans = listView.getCheckedItemPositions();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (booleans.get(i)) {
                sb.append(data[i]);
            }
        }
        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
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

    class CustomAdapter extends BaseAdapter {

        @Override
        public int
        getCount() {
            return IMAGES.length;
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
            view = getLayoutInflater().inflate(R.layout.menulayout, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,200));

            TextView textView_name = (TextView) view.findViewById(R.id.checkbox_layout);

            textView_name.setText(UtilSet.al_store.get(i).getStore_name());
            return view;
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}