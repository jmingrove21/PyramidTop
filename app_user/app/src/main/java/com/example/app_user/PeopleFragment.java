package com.example.app_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PeopleFragment extends Fragment {
    String[] peopleItems = {"Hi","Hello","ExcuseMe"};
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view =  inflater.inflate(R.layout.fragment_people,container,false);

        Button button = (Button) view.findViewById(R.id.people_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PartyListActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.people_listview);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                peopleItems
        );
        listView.setAdapter(listViewAdapter);
        return view;
    }
}
