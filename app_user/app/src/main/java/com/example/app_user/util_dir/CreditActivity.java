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

import com.example.app_user.R;

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
    public int total_price;
    int mileage;
    TextView mileage_info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Intent intent=getIntent();
        final int total_price=intent.getIntExtra("total_price",0);
        final int mileage=intent.getIntExtra("mileage",0);
        BootpayAnalytics.init(this, application_id);

        spinner_pg = findViewById(R.id.spinner_pg);
        spinner_method = findViewById(R.id.spinner_method);
        price_text = findViewById(R.id.price_text);
        mileage_info=findViewById(R.id.mileage_info);
        mileage_edit = findViewById(R.id.mileage_edit);
        mileage_btn=findViewById(R.id.mileage_btn);
        total_price_credit=findViewById(R.id.total_price_credit);
        price_text.setText(String.valueOf(total_price));
        total_price_credit.setText(String.valueOf(total_price));
        mileage_info.setText(mileage_info.getText()+" - "+String.valueOf(mileage)+"원 사용가능");


        mileage_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                if(Integer.valueOf(mileage_edit.getText().toString())<=mileage){
                    total_price_credit.setText(String.valueOf(total_price-Integer.parseInt(mileage_edit.getText().toString())));
                }else{
                    Toast.makeText(CreditActivity.this,"마일리지가 부족합니다!",Toast.LENGTH_SHORT ).show();
                    total_price_credit.setText(String.valueOf(total_price));
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
                .addItem("배달배달", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
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
                        Toast.makeText(CreditActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CreditActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                        Toast.makeText(CreditActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d("close", "close");
                                Toast.makeText(CreditActivity.this, message, Toast.LENGTH_SHORT).show();

                            }
                        })
                .request();

    }

    public void goBootpayWindow(View v) {

        BootUser bootUser = new BootUser().setPhone("010-1234-5678"); // 구매자 정보
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});  // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
        List<String> methods = Arrays.asList("card", "phone");
        int price=0;
        try {
            price = Integer.parseInt(total_price_credit.getText().toString());
        } catch (Exception e){}


        Bootpay.init(getFragmentManager())
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값

                .setName("배달ONE") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setMethods(methods)
//                .setPG(PG.DANAL)
//                .setMethod(Method.SUBSCRIPT_CARD)
//                .setMe
//                .setMethods
//                .setExpireMonth()
//                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setPrice(price) // 결제할 금액
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
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
                        Toast.makeText(CreditActivity.this, message, Toast.LENGTH_SHORT).show();

                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {
                        Toast.makeText(CreditActivity.this, message, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(CreditActivity.this, message, Toast.LENGTH_SHORT).show();

                            }
                        })
                .request();

    }
}