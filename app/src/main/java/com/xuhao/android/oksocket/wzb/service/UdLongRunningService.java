package com.xuhao.android.oksocket.wzb.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.xuhao.android.oksocket.MyApplication;
import com.xuhao.android.oksocket.data.MsgDataBean;
import com.xuhao.android.oksocket.wzb.receiver.LkAlarmReceiver;
import com.xuhao.android.oksocket.wzb.receiver.UdAlarmReceiver;
import com.xuhao.android.oksocket.wzb.util.Cmd;
import com.xuhao.android.oksocket.wzb.util.LocationMulti;
import com.xuhao.android.oksocket.wzb.util.LocationUtils;
import com.xuhao.android.oksocket.wzb.util.LogUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-07-10.
 */

public class UdLongRunningService extends Service {

    public static final int UD_INTERVAL=20*1000;//60 seconds

    private LocationManager mLocationManager;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=MyApplication.CONTEXT;
        mLocationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        getContentResolver()
                .registerContentObserver(
                        Settings.Secure
                                .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);
       // openGPS(true);
    }

    private final ContentObserver mGpsMonitor=new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            boolean enabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            LogUtil.logMessage("wzb", "gps enabled? " + enabled);
            if(!enabled){
                openGPS(true);
            }
        }
    };

    private void openGPS(boolean open) {
        try{
            if (Build.VERSION.SDK_INT <19) {
                Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(),
                        LocationManager.GPS_PROVIDER, open);
            }else{
                if(!open){
                    Settings.Secure.putInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE, android.provider.Settings.Secure.LOCATION_MODE_OFF);
                }else{
                    Settings.Secure.putInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE, android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY);
                }
            }
        }catch (Exception e) {
            // TODO: handle exception
            Log.d("wzb", "open gps exception");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doSomeThing();
        return super.onStartCommand(intent, flags, startId);
    }

    private void doSomeThing(){
       // final String info= LocationUtils.getInstance(MyApplication.CONTEXT).getLocation();
       // Log.e("wzb","location info="+info);
        //Toast.makeText(MyApplication.CONTEXT,info,Toast.LENGTH_LONG).show();
        //new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        Log.e("wzb","UdLongRunningService executed at "+new Date().toString());
        //        String msg= Cmd.encode(Cmd.CS+Cmd.SPLIT+Cmd.IMEI+Cmd.SPLIT+Cmd.UD+","+packUdInfo(info));
        //        CoreService.mManager.send(new MsgDataBean(msg));
        //    }
        //}).start();

        LocationMulti.getInstance(MyApplication.CONTEXT).StartLocation();

        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        long triggerAtTime= SystemClock.elapsedRealtime() + UD_INTERVAL;
        Intent i = new Intent(this, UdAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

    }

    private String packUdInfo(String location){
        String info="";

        Calendar now=Calendar.getInstance();
        String dateYear=String.format("%04d",now.get(Calendar.YEAR));
        String dateMonth=String.format("%02d",now.get(Calendar.MONTH)+1);
        String dateDay=String.format("%02d",now.get(Calendar.DAY_OF_MONTH));
        String date=dateDay+dateMonth+dateYear.substring(2);
        String time=String.format("%02d",now.get(Calendar.HOUR_OF_DAY))+String.format("%02d",now.get(Calendar.MINUTE))
                +String.format("%02d",now.get(Calendar.SECOND));
        Log.e("wzb","date="+date+" time="+time);
        info+=date;
        info+=",";
        info+=time;
        info+=",";
        List<String> gps= Arrays.asList(location.split(","));
        String gpsStatus=gps.get(0);
        info+=gpsStatus;
        info+=",";
        String lat=gps.get(1);
        if(lat.substring(0,1).equals("-")){
            lat=lat+",S";
        }else{
            lat=lat+",N";
        }
        info+=lat;
        info+=",";
        String lon=gps.get(2);
        if(lon.substring(0,1).equals("-")){
            lon=lon+",W";
        }else{
            lon=lon+",E";
        }
        info+=lon;
        info+=",";
       // Log.e("wzb","info="+info);
        String speed="0";
        info+=speed;
        info+=",";

        String direction="0";
        info+=direction;
        info+=",";

        String height="0";
        info+=height;
        info+=",";

        String statelliteNum="0";
        info+=statelliteNum;
        info+=",";

        String gsmDbm="0";
        info+=gsmDbm;
        info+=",";

        String batteryLevel=String.valueOf(Cmd.getBatteryLevel());
        info+=batteryLevel;
        info+=",";

        String stepCounter="0";
        info+=stepCounter;
        info+=",";

        //String rollNum="0";
        //info+=rollNum;
        //info+=",";

        String deviceStatus="00000000";
        info+=deviceStatus;
        info+=",";

        String stationNum="0";
        info+=stationNum;
        info+=",";

        String gsmDelay="0";
        info+=gsmDelay;
        info+=",";

        String mcc="460";
        info+=mcc;
        info+=",";

        String mnc="0";
        info+=mnc;
        info+=",";

        String lac="0";//基站位置区域码
        info+=lac;
        info+=",";

        String cellid="0";//基站编号
        info+=cellid;
        info+=",";

        String stationDbm="0";
        info+=stationDbm;
        info+=",";

        String wifiNum="0";
        info+=wifiNum;


        Log.e("wzb","info="+info);
        return info;
        //return "220414,134652,A,22.571707,N,113.8613968,E,0.39,213.9,100,7,60,90,1000,00010000,4,1,460,02,9360,4082,131,9360,4092,148,9360,4091,143,9360,4153,141,2,TP-LINK1,1c:fa:68:13:a5:b4,-61,TP-LINK2,1c:fa:68:13:a5:b4,-55";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mGpsMonitor);
    }
}
