package com.example.app_user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class PeopleFragment extends Fragment {

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_people,container,false);

        ListView listView = (ListView) view.findViewById(R.id.people_listview);
        


        return view;
    }
}
