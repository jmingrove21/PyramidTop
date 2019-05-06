package com.example.app_user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuListFragment extends Fragment {

    int index;
    int serial;
    ArrayList<String> selectedItems = new ArrayList<>();
    String[] data={"a","b","c","d","e","f","g","h"};
    ListView listView;
    public void setIndex(int index){
        this.index=index;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.menu_detail_fragment,container,false);

        listView = (ListView) view.findViewById(R.id.order_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),R.layout.menu_detail_fragment,R.id.checkbox_layout,data);

        listView.setAdapter(adapter);

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
        return view;
    }

    public void showSelectedItems(View view){
        String items="";
        for(String item:selectedItems){
            items+="-"+item+"\n";
        }

        Toast.makeText(view.getContext(),"You have selected\n"+items,Toast.LENGTH_LONG).show();
    }

    public void confirm(View v){
        SparseBooleanArray booleans = listView.getCheckedItemPositions();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (booleans.get(i)) {
                sb.append(data[i]);
            }
        }
        Toast.makeText(v.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int
        getCount() {
            return 0;
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

}
