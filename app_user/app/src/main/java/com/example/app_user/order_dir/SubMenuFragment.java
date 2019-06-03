package com.example.app_user.order_dir;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.home_dir.MenuFragment;
import com.example.app_user.util_dir.StoreDetailFragment;

public class SubMenuFragment extends Fragment {
    private Bitmap bitmap;
    int index;
    int serial;
    private String type; //order_make, order_participate

    public void setIndex(int index){
        this.index=index;
    }

    String selectedMenu;
    Button store_inform_button, menu_list_button;
    FragmentManager fm;
    FragmentTransaction tran;
    MenuFragment menuFragment;
    StoreDetailFragment storedetailfragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.store_detail_fragment,container,false);

        store_inform_button = (Button) view.findViewById(R.id.store_inform_button);
        menu_list_button = (Button) view.findViewById(R.id.menu_list_button);

        storedetailfragment = new StoreDetailFragment();
        storedetailfragment.setIndex(index);

        menuFragment = new MenuFragment();
        menuFragment.setIndex(index);

        setFrag(0);

        TextView text_store_name = (TextView) view.findViewById(R.id.store_name);
        TextView text_store_phone = (TextView) view.findViewById(R.id.store_phone);
        TextView text_store_branch_name = (TextView) view.findViewById(R.id.store_branch_name);
        ImageView imageView = (ImageView) view.findViewById(R.id.store_image);

        imageView.setImageBitmap(UtilSet.target_store.getStore_image());
        text_store_name.setText(UtilSet.target_store.getStore_name());
        text_store_phone.setText(UtilSet.target_store.getStore_phone());
        text_store_branch_name.setText(UtilSet.target_store.getStore_branch_name());

        store_inform_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrag(0);
            }
        });
        menu_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrag(1);
            }
        });

        return view;
    }

    public void setFrag(int n) {
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n) {
            case 0:
                tran.replace(R.id.sub_fragment_container, storedetailfragment);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.sub_fragment_container, menuFragment);
                tran.commit();
                break;
        }
    }
}
