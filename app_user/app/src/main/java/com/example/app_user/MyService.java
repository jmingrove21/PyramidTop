package com.example.app_user;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import com.example.app_user.home_dir.FirstMainActivity;

public class MyService extends Service {
    NotificationManager noti_M;
    ServiceThread thread;
    Notification noti;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        noti_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();

        String id = "my_channel_01";

        CharSequence name = "test";

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(id, name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mChannel.setShowBadge(false);
        noti_M.createNotificationChannel(mChannel);

        thread=new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread=null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(MyService.this, FirstMainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            int notifyID = 1;

            String CHANNEL_ID = "my_channel_01";

            noti = new Notification.Builder(getApplicationContext())
                    .setContentTitle(ServiceThread.alert_info_al.get(msg.what).get(0))
                    .setContentText(ServiceThread.alert_info_al.get(msg.what).get(1))
                    .setTicker("알림")
                    .setSmallIcon(R.drawable.logo)
                    .setVibrate(new long[]{3000})
                    .setChannelId(CHANNEL_ID)
                    .build();
            Log.d("notification",noti.tickerText.toString());
            //소리추가
            noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            //확인하면 자동으로 알림이 제거 되도록
            noti.flags = Notification.FLAG_AUTO_CANCEL;
            noti_M.notify(notifyID, noti);
        }
    }

}
