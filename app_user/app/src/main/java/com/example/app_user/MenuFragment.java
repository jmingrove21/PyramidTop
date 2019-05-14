package com.example.app_user;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    Bitmap[] bitmap;
    ListView listView;
    private MenuCustomAdapter menuCustomAdapter;
    public static ArrayList<MenuProductItem> menuProductItems;
    String[] menu_name = {"1","2","3","4","5","6","7","8"};
    String[] price = {"1000","2000","3000","4000","1000","2000","3000","4000"};

    int index;

    public void setIndex(int index) {
        this.index = index;
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.menu_fragment,container,false);

        listView = (ListView) view.findViewById(R.id.menu_choice_list);
        menuProductItems = getMenuProductItem();
        menuCustomAdapter = new MenuCustomAdapter(getActivity());

        listView.setAdapter(menuCustomAdapter);

        return view;
    }

    private ArrayList<MenuProductItem> getMenuProductItem(){
        ArrayList<MenuProductItem> list = new ArrayList<>();
        for(int i = 0; i < menu_name.length; i++){
            MenuProductItem menuProductItem = new MenuProductItem();
            menuProductItem.setOrder_number(1);
            menuProductItem.setMenu_inform(menu_name[i]);
            menuProductItem.setPrice_inform(price[i]);
            list.add(menuProductItem);
        }
        return list;
    }
}