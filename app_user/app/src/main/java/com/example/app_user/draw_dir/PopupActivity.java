package com.example.app_user.draw_dir;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.app_user.R;
import android.view.Window;


public class PopupActivity extends Activity implements
        View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_participate_people);
        findViewById(R.id.btnClose).setOnClickListener(this);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                this.finish();
                break;

        }
    }
}
