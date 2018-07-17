package com.xuhao.android.oksocket.wzb.util;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Administrator on 2018-07-17.
 */

public class WakeLockManager {
    static PowerManager.WakeLock wakeLock=null;

    public static void acquire(Context context){
        PowerManager powerManager=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
        wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"cameraservice");
        if(wakeLock !=null){
            wakeLock.acquire();
        }
    }

    public static void release(){
        if(wakeLock!=null){
            wakeLock.release();
        }
    }
}
