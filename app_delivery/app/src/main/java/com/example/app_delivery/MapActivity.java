package com.example.app_delivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {
    TMapView tMapView;
    ArrayList alTmapPoint;
    private ListView m_oListView=null;
    public ArrayList<Item_UserInfo> oData=new ArrayList<>();

    private String id = "delivery_1";
    TMapData tmapdata=new TMapData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.tMapView = new TMapView(this);
        this.alTmapPoint = new ArrayList();
        tMapView.setSKTMapApiKey(UtilSet.tmap_key);
        set_MapView();

        if(getIntent().hasExtra("json")) {
            try{
                JSONObject mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
                get_destination(mJsonObject);
                get_user_information(mJsonObject);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        m_oListView=(ListView)findViewById(R.id.user_delivery_list);
        ListAdapter_user oAdapter=new ListAdapter_user(oData);
        m_oListView.setAdapter(oAdapter);
        m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("byebye");
            }
        });
    }

    private void set_MapView() {
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);
    }

    public void get_user_information(JSONObject jobj){
        try {
            JSONArray json_result_al = (JSONArray) jobj.get("user_order");
            for(int i=0;i<json_result_al.length();i++){
                Item_UserInfo iu=new Item_UserInfo();
                String user_name=((JSONObject)json_result_al.get(i)).get("user_name").toString();
                String user_serial=((JSONObject)json_result_al.get(i)).get("user_serial").toString();
                String destination=((JSONObject)json_result_al.get(i)).get("destination").toString();
                String user_phone=((JSONObject)json_result_al.get(i)).get("user_phone").toString();
                iu.set_ItemUserInfo(user_serial,user_name,user_phone,destination);
                oData.add(iu);
              //  String price=((JSONObject)json_result_al.get(i)).get("user_name").toString();

                // String u
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void set_Marker_in_Map() {

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker3);
        final Bitmap bitmap_1 = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);

        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        // 마커 아이콘 지정
        markerItem1.setIcon(bitmap_1);// 마커 아이콘 지정
        // 마커의 좌표 지정
        markerItem1.setTMapPoint(new TMapPoint(37.279548, 127.043791));
        //지도에 마커 추가
        tMapView.addMarkerItem("markerItem0", markerItem1);

        for (int i = 0; i < alTmapPoint.size(); i++) {
            markerItem1 = new TMapMarkerItem();
            // 마커 아이콘 지정
            markerItem1.setIcon(bitmap);// 마커 아이콘 지정
            markerItem1.setName(String.valueOf(i));
            // 마커의 좌표 지정
            markerItem1.setTMapPoint((TMapPoint) alTmapPoint.get(i));
            markerItem1.setCanShowCallout(true);
            markerItem1.setCalloutTitle(String.valueOf(i));

            //지도에 마커 추가
            tMapView.addMarkerItem("markerItem" + (i + 1), markerItem1);

        } tMapView.setCenterPoint(127.043840, 37.278307);

        if(alTmapPoint.size()==1){
            tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,new TMapPoint(37.279548, 127.043791), (TMapPoint)alTmapPoint.get(alTmapPoint.size()-1), new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine polyLine) {
                    polyLine.setLineColor(Color.BLUE);
                    polyLine.setLineWidth(8.0f);
                    tMapView.addTMapPath(polyLine);
                }
            });
        }
        else {
            TMapPoint t_end=(TMapPoint)alTmapPoint.get(0);
            alTmapPoint.remove(0);
            tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(37.279548, 127.043791), t_end, alTmapPoint, 0,
                    new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine polyLine) {
                            polyLine.setLineColor(Color.BLUE);
                            polyLine.setLineWidth(8.0f);
                            tMapView.addTMapPath(polyLine);
                        }
                    });
        }
    }

    private void get_destination(JSONObject jobj) {
        try {
            JSONArray json_result_al = (JSONArray) jobj.get("user_order");
            for (int i = 0; i < json_result_al.length(); i++) {
                double lat_dest = Double.parseDouble(((JSONObject) json_result_al.get(i)).get("destination_lat").toString());
                double long_dest = Double.parseDouble(((JSONObject) json_result_al.get(i)).get("destination_long").toString());
                alTmapPoint.add(new TMapPoint(lat_dest, long_dest));
            }
            set_Marker_in_Map();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void complete_delievry(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://54.180.102.7:80/get/JSON/delivery_app/delivery_manage.php");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "complete");
                    jsonParam.put("delivery_state",0);//한 user complete
                    //jsonParam.put("delivery_state",1);//전체 user complete
                    jsonParam.put("order_number",0);
                    jsonParam.put("user_serial",0);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    if(conn.getResponseCode()==200){

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
    private class ListAdapter_user extends BaseAdapter {
        LayoutInflater inflater = null;
        private ArrayList<Item_UserInfo> m_oData = null;
        private int nListCnt = 0;

        public ListAdapter_user(ArrayList<Item_UserInfo> oData){
            m_oData=oData;
            nListCnt=m_oData.size();
        }

        @Override
        public int getCount() {
            Log.i("Tag","getCount");
            return nListCnt;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                final Context context=parent.getContext();
                if(inflater==null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView=inflater.inflate(R.layout.activity_user_information,parent,false);

            }
            TextView oUserName=(TextView)convertView.findViewById(R.id.user_name);
            TextView oUserPhone=(TextView)convertView.findViewById(R.id.user_phonenum);
            TextView oUserDestination=(TextView)convertView.findViewById(R.id.user_destination);
            TextView oUserMenu=(TextView)convertView.findViewById(R.id.user_menu);

            Button oButton=(Button)convertView.findViewById(R.id.delivery_finish_btn);
            oUserName.setText(m_oData.get(position).user_name);
            oUserPhone.setText(m_oData.get(position).user_phone);
            oUserDestination.setText(m_oData.get(position).destination);
            oUserMenu.setText("짜장면 1개");

            oButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("log","position:"+position);
                }
            });
            return convertView;
        }

    }
}
