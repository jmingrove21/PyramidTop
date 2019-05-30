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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {
    TMapView tMapView;
    ArrayList alTmapPoint;
    private ListView m_oListView = null;
    public ArrayList<Item_UserInfo> oData = new ArrayList<>();
    public int order_number;
    private String id = "delivery_1";
    TMapData tmapdata = new TMapData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.tMapView = new TMapView(this);
        this.alTmapPoint = new ArrayList();
        tMapView.setSKTMapApiKey(UtilSet.tmap_key);
        set_MapView();
        order_number = getIntent().getIntExtra("order_number", 0);
        if (getIntent().hasExtra("json")) {
            try {
                JSONObject mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
                get_user_information(mJsonObject);
                get_destination();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        m_oListView = (ListView) findViewById(R.id.user_delivery_list);
        ListAdapter_user oAdapter = new ListAdapter_user(oData);
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

    public void get_user_information(JSONObject jobj) {
        try {
            JSONArray json_result_al = (JSONArray) jobj.get("user_order");
            for (int i = 0; i < json_result_al.length(); i++) {
                Item_UserInfo iu = new Item_UserInfo();
                JSONObject jobj_user=(JSONObject) json_result_al.get(i);
                String user_name = jobj_user.get("user_name").toString();
                String user_serial = jobj_user.get("user_serial").toString();
                String destination = jobj_user.get("destination").toString();
                String user_phone = jobj_user.get("user_phone").toString();
                String destination_lat = jobj_user.get("destination_lat").toString();
                String destination_long = jobj_user.get("destination_long").toString();
                String total_price=jobj_user.get("user_total_price").toString();
                iu.set_ItemUserInfo(order_number, user_serial, user_name, user_phone, destination,total_price);
                iu.set_destination(destination_lat, destination_long);
                JSONArray menu_jarry=(JSONArray)jobj_user.get("menu");
                for(int j=0;j<menu_jarry.length();j++){
                    String menu_name=((JSONObject)menu_jarry.get(j)).get("menu_name").toString();
                    String menu_count=((JSONObject)menu_jarry.get(j)).get("menu_count").toString();
                    String menu_price=((JSONObject)menu_jarry.get(j)).get("menu_price").toString();
                    Menu m=new Menu(menu_name,menu_count,menu_price);
                    iu.al_menu.add(m);
                }
                oData.add(iu);
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
        markerItem1.setTMapPoint(new TMapPoint(UtilSet.latitude, UtilSet.longitude));
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

        }
        tMapView.setCenterPoint(UtilSet.longitude, UtilSet.latitude);

        if (alTmapPoint.size() == 1) {
            tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(UtilSet.latitude, UtilSet.longitude), (TMapPoint) alTmapPoint.get(alTmapPoint.size() - 1), new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine polyLine) {
                    polyLine.setLineColor(Color.BLUE);
                    polyLine.setLineWidth(8.0f);
                    tMapView.addTMapPath(polyLine);
                }
            });
        } else {
            TMapPoint t_end = (TMapPoint) alTmapPoint.get(0);
            alTmapPoint.remove(0);
            tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(UtilSet.latitude, UtilSet.longitude), t_end, alTmapPoint, 0,
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

    private void get_destination() {
        try {
            for (int i = 0; i < oData.size(); i++) {
                double lat_dest = oData.get(i).destination_lat;
                double long_dest = oData.get(i).destination_long;
                alTmapPoint.add(new TMapPoint(lat_dest, long_dest));
            }
            set_Marker_in_Map();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void complete_delievry(final int position, final int user_count) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("delivery_info", "complete");

                    int delivery_status = 0;
                    for (int i = 0; i < user_count; i++) {
                        delivery_status += oData.get(i).delivery_status;
                    }
                    if (delivery_status != 0)
                        jsonParam.put("delivery_status", 0);//한 user complete
                    else if (delivery_status == 0)
                        jsonParam.put("delivery_status", 1);//한 user complete
                    jsonParam.put("order_number", oData.get(position).order_number);
                    jsonParam.put("user_serial", oData.get(position).user_serial);

                    HttpURLConnection conn=UtilSet.set_Connect_info(jsonParam);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        Log.i("confirm", jsonReply);
                        if(jsonReply.equals("2\n")){
                            Intent intentHome = new Intent(getBaseContext(), MainActivity.class);
                            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intentHome);
                            finish();
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

    private class ListAdapter_user extends BaseAdapter {
        LayoutInflater inflater = null;
        private ArrayList<Item_UserInfo> m_oData = null;
        private int nListCnt = 0;

        public ListAdapter_user(ArrayList<Item_UserInfo> oData) {
            m_oData = oData;
            nListCnt = m_oData.size();
        }

        @Override
        public int getCount() {
            Log.i("Tag", "getCount");
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
            if (convertView == null) {
                final Context context = parent.getContext();
                if (inflater == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView = inflater.inflate(R.layout.activity_user_information, parent, false);

            }
            TextView oUserName = (TextView) convertView.findViewById(R.id.user_name);
            TextView oUserPhone = (TextView) convertView.findViewById(R.id.user_phonenum);
            TextView oUserDestination = (TextView) convertView.findViewById(R.id.user_destination);
            TextView oUserMenu = (TextView) convertView.findViewById(R.id.user_menu);
            TextView oUserTotal=(TextView)convertView.findViewById(R.id.user_total_price);
            final Button oButton = (Button) convertView.findViewById(R.id.delivery_finish_btn);
            oUserName.setText(m_oData.get(position).user_name);
            oUserPhone.setText(m_oData.get(position).user_phone);
            oUserDestination.setText(m_oData.get(position).destination);
            oUserTotal.setText(m_oData.get(position).total_price+"원");
            String menu="";
            for(int i=0;i<m_oData.get(position).al_menu.size();i++){
                menu+=m_oData.get(position).al_menu.get(i).menu_name+" "+m_oData.get(position).al_menu.get(i).menu_count+"개 ";
            }
            oUserMenu.setText(menu);

            oButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("log", "position:" + position);
                    oButton.setText("배달완료");
                    oButton.setEnabled(false);
                    oData.get(position).delivery_status = 0;
                    complete_delievry(position, oData.size());
                }
            });
            return convertView;
        }

    }
}
