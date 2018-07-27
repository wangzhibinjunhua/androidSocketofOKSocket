package com.xuhao.android.oksocket.wzb.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.xuhao.android.oksocket.DemoActivity;
import com.xuhao.android.oksocket.MyApplication;
import com.xuhao.android.oksocket.data.MsgDataBean;
import com.xuhao.android.oksocket.wzb.util.Cmd;
import com.xuhao.android.oksocket.wzb.receiver.LkAlarmReceiver;
import com.xuhao.android.oksocket.wzb.util.LocationUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018-07-10.
 */

public class LkLongRunningService extends Service {

    public static final int LK_INTERVAL=30*1000;//60 seconds

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doSomeThing();
        return super.onStartCommand(intent, flags, startId);
    }

    private void doSomeThing(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("wzb","LkLongRunningService executed at "+new Date().toString());
                String msg= Cmd.encode(Cmd.CS+Cmd.SPLIT+Cmd.IMEI+Cmd.SPLIT+Cmd.LK);
                CoreService.mManager.send(new MsgDataBean(msg));
            }
        }).start();

        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        long triggerAtTime= SystemClock.elapsedRealtime() + LK_INTERVAL;
        Intent i = new Intent(this, LkAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        //test
        //String info= LocationUtils.getInstance(MyApplication.CONTEXT).getLocation();
        //Log.e("wzb","location info="+info);
        //Toast.makeText(MyApplication.CONTEXT,info,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
