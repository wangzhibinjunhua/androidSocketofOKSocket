package com.xuhao.android.oksocket.wzb.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xuhao.android.oksocket.MyApplication;

/**
 * Created by Administrator on 2018-07-11.
 */

public class Cmd {

    public static final String LK="LK";
    public static final String CS="CS";
    public static final String UD="UD";

    public static final String SPLIT="*";


    public static final String IMEI=getImei();









    public static String encode(String data){
        return String.format("%04x",data.length())+data;
    }

    public static String decode(String data){
        if(data.length()<=4)return null;
        return data.substring(4);
    }

    public static String getImei() {
        String defaultImei="1234567890123456";
        TelephonyManager telephonyManager = (TelephonyManager) MyApplication.CONTEXT.getSystemService(MyApplication.CONTEXT.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(MyApplication.CONTEXT, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return defaultImei;
        }
        String imei = telephonyManager.getDeviceId();
        Log.e("wzb","get imei:"+imei);
        return imei!=null? imei:defaultImei;
    }
}
