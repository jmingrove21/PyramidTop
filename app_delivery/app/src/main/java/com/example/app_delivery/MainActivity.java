package com.example.app_delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Delivery_list> order_list=new ArrayList<>();
    private BackPressCloseHandler backPressCloseHandler;
    private ListView m_oListView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        backPressCloseHandler=new BackPressCloseHandler(this);

        ArrayList<ItemData> oData=new ArrayList<>();
        get_delivery_order_list();
        for(int i=0;i<order_list.size();i++){
            ItemData oItem=new ItemData();
            oItem.strTitle=order_list.get(i).getStore_name();
            oItem.strDate=order_list.get(i).getOrder_receipt_date();
            oData.add(oItem);
        }


        m_oListView=(ListView)findViewById(R.id.listView);
        ListAdapter oAdapter=new ListAdapter(oData);
        m_oListView.setAdapter(oAdapter);

    }
    public void get_delivery_order_list() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    order_list.clear();
                    URL url = new URL("http://54.180.102.7:80/get/JSON/delivery_app/delivery_manage.php");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "request");
                    jsonParam.put("delivery_lat", 37.276391);
                    jsonParam.put("delivery_long", 127.044021);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    if(conn.getResponseCode()==200){
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        JSONArray json_result_al=new JSONArray(jsonReply);
                        //JSONArray json_result_al = (JSONArray) jobj.get("data");
                        for (int i = 0; i < json_result_al.length(); i++) {
                            int order_number=Integer.parseInt(((JSONObject) json_result_al.get(i)).get("order_number").toString());
                            int store_serial=Integer.parseInt(((JSONObject) json_result_al.get(i)).get("store_serial").toString());
                            String order_receipt_date=((JSONObject) json_result_al.get(i)).get("order_receipt_date").toString();
                            String store_name=((JSONObject) json_result_al.get(i)).get("store_name").toString();
                            String store_branch_name=((JSONObject) json_result_al.get(i)).get("store_branch_name").toString();
                            float distance=Float.parseFloat(((JSONObject) json_result_al.get(i)).get("distance").toString());

                            order_list.add(new Delivery_list(order_number,store_serial,order_receipt_date,store_name,store_branch_name,distance));

                           // alTmapPoint.add(new TMapPoint(lat_dest, long_dest));
                        }
                    }else{
                        Log.d("error","Connect fail");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }
}
