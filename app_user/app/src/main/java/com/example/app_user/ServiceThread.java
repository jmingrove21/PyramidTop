package com.example.app_user;

import android.os.Handler;
import android.util.Log;

import com.example.app_user.Item_dir.UtilSet;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;
    static int result_output=-1;
    static ArrayList<String> alert_info_al=new ArrayList<>();

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
           // get_user_status_change();
            if(result_output==1&&UtilSet.loginLogoutInform.getLogin_flag()==1){
               // handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
            }else{

            }
            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch (Exception e) {}
        }
    }
    public int get_user_status_change() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UtilSet.al_store.clear();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user_info", "check_status");

                    HttpURLConnection conn = UtilSet.set_Connect_info(jsonParam);
                    if (conn.getResponseCode() == 200) {
                        InputStream response = conn.getInputStream();
                        String result = UtilSet.convertStreamToString(response);
                        JSONObject jobj = new JSONObject(result);
                        if (jobj.get("confirm").toString().equals("1")) {
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

}
