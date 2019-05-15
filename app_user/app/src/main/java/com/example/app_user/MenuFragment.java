package com.example.app_user;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    ListView listView;
    private MenuCustomAdapter menuCustomAdapter;
    public static ArrayList<MenuProductItem> menuProductItems;

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
        for(int i = 0; i < UtilSet.target_store.getMenu_desc_al().size(); i++){
            MenuProductItem menuProductItem = new MenuProductItem();
            menuProductItem.setOrder_number(0);
            menuProductItem.setMenu_inform( UtilSet.target_store.getMenu_desc_al().get(i).getMenu_name());
            menuProductItem.setPrice_inform(String.valueOf(UtilSet.target_store.getMenu_desc_al().get(i).getMenu_price()));
            menuProductItem.setMenu_image(UtilSet.target_store.getMenu_desc_al().get(i).getMenu_image());
            menuProductItem.setMenu_code(UtilSet.target_store.getMenu_desc_al().get(i).getMenu_code());
            list.add(menuProductItem);
        }
        return list;
    }


}