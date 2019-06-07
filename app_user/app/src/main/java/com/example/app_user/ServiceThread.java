package com.example.app_user;

import android.os.Handler;
import android.util.Log;

import com.example.app_user.Item_dir.LoginLogoutInform;
import com.example.app_user.Item_dir.UtilSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;
    static int result_output = -1;
    static ArrayList<ArrayList<String>> alert_info_al = new ArrayList<>();

    public ServiceThread(Handler handler) {
        this.handler = handler;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run() {
        //반복적으로 수행할 작업을 한다.
        while (isRun) {
            get_user_status_change();
            if (result_output == 1 && LoginLogoutInform.getLogin_flag() == 1) {
                for (int i = 0; i < alert_info_al.size(); i++) {
                    handler.sendEmptyMessage(i);//쓰레드에 있는 핸들러에게 메세지를 보냄
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }

                }
            } else {

            }
            try {
                Thread.sleep(10000); //10초씩 쉰다.
            } catch (Exception e) {
            }
        }
    }

    public int get_user_status_change() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    alert_info_al.clear();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "check_status");
                    if(UtilSet.my_user!=null)
                        jsonParam.put("user_serial", UtilSet.my_user.getUser_serial());
                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String result = UtilSet.convertStreamToString(response);
                        JSONObject jobj = new JSONObject(result);
                        Log.d("check_status",jobj.toString());
                        if(UtilSet.my_user!=null)
                            UtilSet.my_user.setUser_mileage(Integer.parseInt(jobj.get("mileage").toString()));
                        if (jobj.get("confirm").toString().equals("1")) {
                            JSONArray jarray = (JSONArray) jobj.get("data");
                            Log.d("check_status",jobj.toString());
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject jobj_store = (JSONObject) jarray.get(i);
                                JSONArray jobj_store_alert = (JSONArray) jobj_store.get("alarm_check");
                                for (int j = 0; j < jobj_store_alert.length(); j++) {
                                    ArrayList<String> al_str = new ArrayList<>();
                                    JSONObject jobj_store_detail = (JSONObject) jobj_store_alert.get(j);
                                    String msg=get_alarm_msg(jobj_store_detail.getInt("status"),jobj_store.getString("store_name"));
                                    al_str.add(msg);
                                    al_str.add(jobj_store_detail.getString("time"));
                                    alert_info_al.add(al_str);
                                }
                            }
                            result_output = 1;
                        } else {
                            result_output = 0;
                        }
                    } else {
                        Log.d("error", "Connect fail");
                        result_output = 0;
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    result_output = 0;
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result_output;
    }

    public String get_alarm_msg(int status,String store_name){
        String result=store_name;
        if(status==3){
            result+="에서 요청하신 주문이 접수되었습니다.";
        }else if(status==4){
            result+="에서 요청하신 음식이 완료되었습니다.";

        }else if(status==6){
            result+="에서 배달 출발하였습니다.";

        }else if(status==7){
            result+="에서 요청한 음식이 배달완료되었습니다.";
        }else if(status==8){
            result+="에서 주문이 취소되었습니다.";
        }else{

        }
        return result;
    }
}
