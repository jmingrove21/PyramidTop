package com.example.app_user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MenuListFragment extends Fragment {

    private OnArrayList OnArrayList;

    public interface OnArrayList{
        void onArrayList(ArrayList<String> selectedItems);
    }

    int index;
    ArrayList<String> menu_data=new ArrayList<>();
    ListView listView;
    ArrayList<String> selectedItems = new ArrayList<>();
    public void setIndex(int index){
        this.index=index;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.menu_detail_fragment,container,false);

        listView = (ListView) view.findViewById(R.id.order_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),R.layout.menulayout,R.id.checkbox_layout,menu_data);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedItem = ((TextView)v).getText().toString();
                if(selectedItems.contains(selectedItem)){
                    selectedItems.remove(selectedItem);
                }else{
                    selectedItems.add(selectedItem);
                }
                OnArrayList.onArrayList(selectedItems);
            }
        });
        return view;
    }

    public void set_menu_data(){
        ArrayList<Menu> menu_al=UtilSet.al_store.get(index).getMenu_al();
        for(int i=0;i<menu_al.size();i++){
            ArrayList<MenuDesc> menu_desc_al=menu_al.get(i).getMenu_desc_al();
            for(int j=0;j<menu_desc_al.size();j++){
                this.menu_data.add(menu_desc_al.get(j).getMenu_name());
            }
        }
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnArrayList){
            OnArrayList = (OnArrayList) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        OnArrayList = null;
    }
//    public void confirm(View v){
//        SparseBooleanArray booleans = listView.getCheckedItemPositions();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < data.length; i++) {
//            if (booleans.get(i)) {
//                sb.append(data[i]);
//            }
//        }
//        Toast.makeText(v.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
//    }

}
