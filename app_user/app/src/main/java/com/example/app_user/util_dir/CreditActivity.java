package com.example.app_user.util_dir;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_user.Item_dir.MenuDesc;
import com.example.app_user.Item_dir.UtilSet;
import com.example.app_user.R;
import com.example.app_user.draw_dir.OldOrderlistDetailActivity;
import com.example.app_user.home_dir.FirstMainActivity;
import com.example.app_user.home_dir.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.BootpayKeyValue;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listner.CancelListener;
import kr.co.bootpay.listner.CloseListener;
import kr.co.bootpay.listner.ConfirmListener;
import kr.co.bootpay.listner.DoneListener;
import kr.co.bootpay.listner.ErrorListener;
import kr.co.bootpay.listner.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class CreditActivity extends AppCompatActivity {
//private ApiPresenter presenter;
//    ApiPresenter present;

    private final String application_id = "5cf89d3fb6d49c3e74bf2950"; //pro

    Spinner spinner_pg;
    Spinner spinner_method;
    TextView price_text;
    EditText mileage_edit;
    Button mileage_btn;
    TextView total_price_credit;
    TextView delivery_text;
    TextView mileage_info;
    JSONObject jobj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Intent intent=getIntent();
        final int total_price=intent.getIntExtra("total_price",0);
        final int mileage=intent.getIntExtra("mileage",0);
        final int delivery_cost=intent.getIntExtra("delivery_cost",0);
        if(getIntent().hasExtra("json")) {
            try{
                jobj = new JSONObject(getIntent().getStringExtra("json"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        BootpayAnalytics.init(this, application_id);

        spinner_pg = findViewById(R.id.spinner_pg);
        spinner_method = findViewById(R.id.spinner_method);
        price_text = findViewById(R.id.price_text);
        mileage_info=findViewById(R.id.mileage_info);
        mileage_edit = findViewById(R.id.mileage_edit);
        mileage_btn=findViewById(R.id.mileage_btn);
        delivery_text=findViewById(R.id.delivery_text);
        total_price_credit=findViewById(R.id.total_price_credit);
        price_text.setText(String.valueOf(total_price));
        mileage_info.setText(mileage_info.getText()+" - "+ mileage +"원 사용가능");
        delivery_text.setText(delivery_cost+"원");
        total_price_credit.setText(String.valueOf(total_price+delivery_cost));

        mileage_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                if(Integer.valueOf(mileage_edit.getText().toString())<=mileage){
                    if(Integer.parseInt(total_price_credit.getText().toString())-Integer.parseInt(mileage_edit.getText().toString())>=1000)
                        total_price_credit.setText(String.valueOf(total_price-Integer.parseInt(mileage_edit.getText().toString())+delivery_cost));
                    else if((Integer.parseInt(total_price_credit.getText().toString())-Integer.parseInt(mileage_edit.getText().toString())<1000)&&(Integer.parseInt(total_price_credit.getText().toString())-Integer.parseInt(mileage_edit.getText().toString())>=0)){
                        if(spinner_method.getSelectedItem().toString().equals("현장결제")){
                            Toast.makeText(CreditActivity.this,"1000원 이상 결제가 가능합니다!",Toast.LENGTH_SHORT).show();
                        }else if(spinner_method.getSelectedItem().toString().equals("계좌이체")){
                            Toast.makeText(CreditActivity.this,"결제가 완료되었습니다!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CreditActivity.this,"결제금액이 옳바르지 않습니다!", Toast.LENGTH_SHORT ).show();
                    }
                }else{
                    Toast.makeText(CreditActivity.this,"마일리지가 부족합니다!",Toast.LENGTH_SHORT ).show();
                    total_price_credit.setText(String.valueOf(total_price+delivery_cost));
                }}catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void goRequest(View v) {
//        runOnUiThread();

//        BootpayRestService

//        Spinner mySpinner = (Spinner) findViewById(R.id.your_spinner);
//        String text = mySpinner.getSelectedItem().toString();

        if(spinner_method.getSelectedItem().toString().equals("현장결제")) {
            finish_credit(0);

        }else{
            BootUser bootUser = new BootUser().setPhone("010-1234-5678"); // 구매자 정보
            BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});  // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)

            String pg = BootpayKeyValue.getPGCode(spinner_pg.getSelectedItem().toString());
            String method = BPValue.methodToString(spinner_method.getSelectedItem().toString());
            UX ux = UX.valueOf("PG_DIALOG");
            Context context = this;
            int price=0;
            try {
                price = Integer.parseInt(total_price_credit.getText().toString());
            } catch (Exception e){}


            BootpayAnalytics.init(this, application_id);

//        Bootpay.init()

            Bootpay.init(getFragmentManager())
                    .setContext(context)
                    .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                    .setPG(pg) // 결제할 PG 사
                    .setBootUser(bootUser)
                    .setBootExtra(bootExtra)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                    .setUX(ux)
                    .setMethod(method) // 결제수단
                    .setName("배달ONE") // 결제할 상품명
                    .setOrderId("1234") // 결제 고유번호expire_month
                    .setPrice(price) // 결제할 금액
                    .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                        @Override
                        public void onConfirm(@Nullable String message) {

                            if (true) Bootpay.confirm(message); // 재고가 있을 경우.
                            else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                            Log.d("confirm", message);
                        }
                    })
                    .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                        @Override
                        public void onDone(@Nullable String message) {
                            Log.d("done", message);

                        }
                    })
                    .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                        @Override
                        public void onReady(@Nullable String message) {
                            Log.d("ready", message);
                        }
                    })
                    .onCancel(new CancelListener() { // 결제 취소시 호출
                        @Override
                        public void onCancel(@Nullable String message) {
                            Log.d("cancel", message);

                        }
                    })
                    .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                        @Override
                        public void onError(@Nullable String message) {
                            Log.d("error", message);

                        }
                    })
                    .onClose(
                            new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                @Override
                                public void onClose(String message) {
                                    Log.d("close", "close");

                                    finish_credit(1);

                                }
                            })
                    .request();

        }

    }
    public void finish_credit(final int type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jobj.put("pay_status",type); //0 현장결제 1 계좌이체
                    jobj.put("mileage",Integer.parseInt(mileage_edit.getText().toString()));
                    jobj.put("total_price_credit",Integer.parseInt(total_price_credit.getText().toString()));
                    HttpURLConnection conn = UtilSet.set_Connect_info(jobj);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        Log.d("json_Result", jsonReply);

                        JSONObject jobj = new JSONObject(jsonReply);

                        String json_result = jobj.getString("confirm");
                        if (json_result.equals("1")) {
                            System.out.println("Success order make - not finished");
                            CreditActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText( CreditActivity.this, Integer.parseInt(price_text.getText().toString())+"원 주문생성", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent=new Intent(CreditActivity.this, FirstMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else if(json_result.equals("2")){
                            System.out.println("Success order make - finished");
                            CreditActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText( CreditActivity.this, Integer.parseInt(price_text.getText().toString())+"원 주문생성", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent=new Intent(CreditActivity.this, FirstMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d("error", "Responce code : 0 - fail make order");
                        }
                    } else {
                        Log.d("error", "Connect fail");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}