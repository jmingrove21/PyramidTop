package com.example.app_delivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class MapActivity extends AppCompatActivity {
    static boolean refresh_status = true;
    public ArrayList<Item_UserInfo> oData = new ArrayList<>();
    public int order_number;
    TMapView tMapView;
    ArrayList alTmapPoint;
    TMapData tmapdata = new TMapData();
    private ListView m_oListView = null;
    TextView detail_txt;
    public ArrayList<String> destination_time;
    public ArrayList<String> destination_distance;
    String total_time;
    String total_distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        order_number = getIntent().getIntExtra("order_number", 0);
        total_time = getIntent().getStringExtra("total_time");
        total_distance = getIntent().getStringExtra("total_distance");

        this.tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey(UtilSet.tmap_key);
        set_MapView();

        this.alTmapPoint = new ArrayList();
        Log.d("좌표", String.valueOf(UtilSet.latitude));
        Log.d("좌표", String.valueOf(UtilSet.longitude));
        if (getIntent().hasExtra("list")) {
                oData=(ArrayList<Item_UserInfo> )getIntent().getSerializableExtra("list");
                Log.d("oData",oData.toString());
                get_destination();
        }
        if (getIntent().hasExtra("list_time")) {
            destination_time=(ArrayList<String>)getIntent().getSerializableExtra("list_time");
        }
        if (getIntent().hasExtra("list_distance")) {
            destination_distance=(ArrayList<String>)getIntent().getSerializableExtra("list_distance");
        }

        detail_txt=(TextView)findViewById(R.id.detail_txt);
        String text="전체 거리 약 "+ Double.parseDouble(total_distance)/1000+" km, 예상소요시간 약 "+Integer.parseInt(total_time)/60+"분 입니다.";
        detail_txt.setText(text);
        CircleImageView go_to_user=(CircleImageView)findViewById(R.id.go_to_tmap_user);

        go_to_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapTapi tMapTapi = new TMapTapi(MapActivity.this);

                HashMap pathInfo = new HashMap();

                pathInfo.put("rGoName", "도착");
                pathInfo.put("rGoX", String.valueOf(((TMapPoint)alTmapPoint.get(alTmapPoint.size()-1)).getLongitude()));
                pathInfo.put("rGoY", String.valueOf(((TMapPoint)alTmapPoint.get(alTmapPoint.size()-1)).getLatitude()));

                pathInfo.put("rStName", "출발지");
                pathInfo.put("rStX", String.valueOf(UtilSet.longitude));
                pathInfo.put("rStY", String.valueOf(UtilSet.latitude));

                for(int i=0;i<alTmapPoint.size()-1;i++){
                    String str_x="rV"+String.valueOf(i+1)+"X";
                    String str_y="rV"+String.valueOf(i+1)+"Y";
                    String str_d="rV"+String.valueOf(i+1)+"Name";
                    pathInfo.put(str_d, "경유지"+String.valueOf(i+1));
                    pathInfo.put(str_x, String.valueOf(((TMapPoint)alTmapPoint.get(i)).getLongitude()));
                    pathInfo.put(str_y, String.valueOf(((TMapPoint)alTmapPoint.get(i)).getLatitude()));
                }

                tMapTapi.invokeRoute(pathInfo);
            }
        });
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

    private void get_destination() {
        try {
            for (int i = 0; i < oData.size(); i++) {
                double lat_dest = oData.get(i).destination_lat;
                double long_dest = oData.get(i).destination_long;
                alTmapPoint.add(new TMapPoint(lat_dest, long_dest));
            }
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    MapActivity.Tmap_async t_async = new MapActivity.Tmap_async();
                    t_async.execute();

                }
            });
            thread.start();
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
                    jsonParam.put("delivery_id", UtilSet.delivery_id);
                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);

                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String jsonReply = UtilSet.convertStreamToString(response);
                        Log.i("confirm", jsonReply);
                        alTmapPoint.remove(0);
                        if (jsonReply.equals("2\n")) {
                            Intent intentHome = new Intent(getBaseContext(), MainActivity.class);
                            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intentHome);
                            MapActivity.refresh_status = false;
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

            TextView oUserTotal = (TextView) convertView.findViewById(R.id.user_total_price);
            TextView oUserPayStatus = (TextView) convertView.findViewById(R.id.pay_status);
            final Button oButton = (Button) convertView.findViewById(R.id.delivery_finish_btn);
            Button tMap_btn = (Button) convertView.findViewById(R.id.user_menu_detail);
            oUserName.setText(m_oData.get(position).user_name);
            oUserPhone.setText(m_oData.get(position).user_phone);
            oUserDestination.setText(m_oData.get(position).destination);
            oUserTotal.setText(m_oData.get(position).total_price_credit + "원");
           // String menu = "";
//            for (int i = 0; i < m_oData.get(position).al_menu.size(); i++) {
//                menu += m_oData.get(position).al_menu.get(i).menu_name + " " + m_oData.get(position).al_menu.get(i).menu_count + "개 ";
//            }
            oUserPayStatus.setText(m_oData.get(position).pay_status);

            oButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("log", "position:" + position);
                    oButton.setText("배달완료");
                    oButton.setEnabled(false);
                    tMap_btn.setEnabled(false);
                    oData.get(position).delivery_status = 0;
                    complete_delievry(position, oData.size());
                }
            });
            final TMapTapi tMapTapi = new TMapTapi(MapActivity.this);

            tMap_btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isTmapApp = tMapTapi.isTmapApplicationInstalled();
                    if (isTmapApp == true) {
                        MapActivity.refresh_status = false;
                        tMapTapi.invokeNavigate("destination", new Float(oData.get(position).destination_long), new Float(oData.get(position).destination_lat), 0, true);
                    } else {
                        Toast.makeText(MapActivity.this, "TMap이 설치되어 있지 않습니다.", Toast.LENGTH_LONG);
                    }
                }
            });
            return convertView;
        }
    }

    private class Tmap_async extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {

            Bitmap bitmap_delivery = BitmapFactory.decodeResource(getResources(), R.drawable.marker3);
            Bitmap bitmap_store = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);

            while (true) {

                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                // 마커 아이콘 지정
                markerItem1.setIcon(bitmap_store);// 마커 아이콘 지정
                // 마커의 좌표 지정
                markerItem1.setTMapPoint(new TMapPoint(UtilSet.latitude, UtilSet.longitude));
                //지도에 마커 추가
                tMapView.addMarkerItem("markerItem0", markerItem1);
                for (int i = 0; i < alTmapPoint.size(); i++) {
                    markerItem1 = new TMapMarkerItem();
                    // 마커 아이콘 지정
                    markerItem1.setIcon(bitmap_delivery);// 마커 아이콘 지정
                    markerItem1.setName(String.valueOf(i));
                    // 마커의 좌표 지정
                    markerItem1.setTMapPoint((TMapPoint) alTmapPoint.get(i));
                    markerItem1.setCanShowCallout(true);
                    markerItem1.setCalloutTitle(String.valueOf(i));

                    //지도에 마커 추가
                    tMapView.addMarkerItem("markerItem" + (i + 1), markerItem1);
                }
                Log.d("tmap_destination", alTmapPoint.toString());
                tMapView.setCenterPoint(UtilSet.longitude, UtilSet.latitude);

                if (alTmapPoint.size() == 1) {
                    tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(UtilSet.latitude, UtilSet.longitude), (TMapPoint) alTmapPoint.get(0), new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine polyLine) {
                            polyLine.setLineColor(Color.BLUE);
                            polyLine.setLineWidth(8.0f);
                            tMapView.addTMapPath(polyLine);
                        }
                    });
                    try {
                        Thread.sleep(10000);
                        if (MapActivity.refresh_status == false)
                            break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("tmapPoint size", String.valueOf(alTmapPoint.size()));
                    TMapPoint t_end = (TMapPoint) alTmapPoint.get(alTmapPoint.size() - 1);
                    ArrayList tMap_via = new ArrayList();
                    for (int i = 0; i < alTmapPoint.size() - 1; i++) {
                        tMap_via.add(alTmapPoint.get(i));
                    }
                    Log.d("Distance",String.valueOf( getDistance(UtilSet.longitude, UtilSet.latitude, ((TMapPoint) alTmapPoint.get(0)).getLongitude(), ((TMapPoint) alTmapPoint.get(0)).getLatitude()) ));
                    if (alTmapPoint.size() == 2 && getDistance(UtilSet.longitude, UtilSet.latitude, ((TMapPoint) alTmapPoint.get(0)).getLongitude(), ((TMapPoint) alTmapPoint.get(0)).getLatitude()) <= 0.03) {
                        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(UtilSet.latitude, UtilSet.longitude), (TMapPoint) alTmapPoint.get(1), new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine polyLine) {
                                polyLine.setLineColor(Color.BLUE);
                                polyLine.setLineWidth(8.0f);
                                tMapView.addTMapPath(polyLine);
                            }
                        });
                        try {
                            Thread.sleep(10000);
                            if (MapActivity.refresh_status == false)
                                break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (alTmapPoint.size() == 3 && getDistance(UtilSet.longitude, UtilSet.latitude, ((TMapPoint) alTmapPoint.get(0)).getLongitude(), ((TMapPoint) alTmapPoint.get(0)).getLatitude()) <= 0.03) {
                        tMap_via.remove(0);
                        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(UtilSet.latitude, UtilSet.longitude), t_end, tMap_via, 0,
                                new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine polyLine) {
                                polyLine.setLineColor(Color.BLUE);
                                polyLine.setLineWidth(8.0f);
                                tMapView.addTMapPath(polyLine);
                            }
                        });
                        try {
                            Thread.sleep(10000);
                            if (MapActivity.refresh_status == false)
                                break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, new TMapPoint(UtilSet.latitude, UtilSet.longitude), t_end, tMap_via, 0,
                                new TMapData.FindPathDataListenerCallback() {
                                    @Override
                                    public void onFindPathData(TMapPolyLine polyLine) {
                                        polyLine.setLineColor(Color.BLUE);
                                        polyLine.setLineWidth(8.0f);
                                        tMapView.addTMapPath(polyLine);
                                    }
                                });
                    }
                    try {
                        Thread.sleep(10000);
                        if (MapActivity.refresh_status == false)
                            break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    public static double getDistance(double startPointLon, double startPointLat, double endPointLon, double endPointLat) {
        double d2r = Math.PI / 180;
        double dStartPointLon =startPointLon;
        double dStartPointLat =startPointLat;
        double dEndPointLon = endPointLon;
        double dEndPointLat = endPointLat;

        double dLon = (dEndPointLon - dStartPointLon) * d2r;
        double dLat = (dEndPointLat - dStartPointLat) * d2r;

        double a = Math.pow(Math.sin(dLat / 2.0), 2)
                + Math.cos(dStartPointLat * d2r)
                * Math.cos(dEndPointLat * d2r)
                * Math.pow(Math.sin(dLon / 2.0), 2);

        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * 2;

        double distance = c * 6378;

        return distance;

    }
}
