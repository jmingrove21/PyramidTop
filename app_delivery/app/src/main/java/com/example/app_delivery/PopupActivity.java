package com.example.app_delivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PopupActivity extends Activity implements View.OnClickListener {

    public int index=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_menu_list);
        Intent intent=getIntent();
        index=intent.getIntExtra("index",-1);
        findViewById(R.id.btnClose).setOnClickListener(this);
        ListView listView_user = findViewById(R.id.menu_list_frag);
        MenuAdapter menuAdapter = new MenuAdapter();
        listView_user.setAdapter(menuAdapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                this.finish();
                break;

        }
    }

    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return MapActivity.oData.get(index).al_menu.size();
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
            view = getLayoutInflater().inflate(R.layout.menu_detail_list, null);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 230));

            TextView text_name = view.findViewById(R.id.menu_name);
            TextView text_count = view.findViewById(R.id.menu_count);

            text_name.setText(MapActivity.oData.get(index).al_menu.get(i).menu_name);
            text_count.setText(MapActivity.oData.get(index).al_menu.get(i).menu_count+"개");
            LinearLayout frame = findViewById(R.id.participate_list);

            return view;
        }
    }
}
