package com.example.app_user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MenuListFragment extends Fragment {

    private OnArrayList OnArrayList;

    public interface OnArrayList {
        void onArrayList(ArrayList<String> selectedItems);
    }

    int index;
    ListView listView;
    ArrayList<String> selectedItems = new ArrayList<>();

    public void setIndex(int index) {
        this.index = index;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_detail_fragment, container, false);

        listView = (ListView) view.findViewById(R.id.order_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.menulayout, R.id.checkbox_layout, UtilSet.al_store.get(index).getMenu_str());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedItem = ((TextView) v).getText().toString();
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem);
                } else {
                    selectedItems.add(selectedItem);
                }
                OnArrayList.onArrayList(selectedItems);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArrayList) {
            OnArrayList = (OnArrayList) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        OnArrayList = null;
    }
}