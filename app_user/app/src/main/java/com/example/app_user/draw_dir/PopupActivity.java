package com.example.app_user.draw_dir;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.people_dir.PartyDetailActivity;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class PopupActivity extends Activity implements View.OnClickListener {

    public int index=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_participate_people);
        Intent intent=getIntent();
        index=intent.getIntExtra("index",-1);
        findViewById(R.id.btnClose).setOnClickListener(this);
        ListView listView_user = (ListView) findViewById(R.id.party_detail_layout_user_list_view);
        PopupActivity.UserAdapter userAdapter = new PopupActivity.UserAdapter();
        listView_user.setAdapter(userAdapter);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                this.finish();
                break;

        }
    }

    class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return UtilSet.al_my_order.get(index).getUser_al().size();
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
            view = getLayoutInflater().inflate(R.layout.party_detail_layout_user_listview, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 400));

            TextView text_name = (TextView) view.findViewById(R.id.party_detail_user_name);
            TextView text_user_time = (TextView) view.findViewById(R.id.party_detail_user_time);
            TextView text_user_price = (TextView) view.findViewById(R.id.party_detail_user_price);


            text_name.setText(UtilSet.al_my_order.get(index).getUser_al().get(i).getUser_id());
            text_user_time.setText(String.valueOf(UtilSet.al_my_order.get(index).getUser_al().get(i).getUser_time()));
            text_user_price.setText(String.valueOf(UtilSet.al_my_order.get(index).getUser_al().get(i).getUser_price()));

            return view;
        }
    }
}
