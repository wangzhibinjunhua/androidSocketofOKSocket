package com.xuhao.android.oksocket;

import android.app.Application;
import android.content.Context;

import com.xuhao.android.libsocket.sdk.OkSocket;
import com.xuhao.android.oksocket.wzb.camera.CameraWindow;
import com.xuhao.android.oksocket.wzb.util.LogUtil;
import com.xuhao.android.oksocket.wzb.util.SharedPreferencesUtil;


/**
 * Created by xuhao on 2017/5/22.
 */

public class MyApplication extends Application {

    /**
     * 全局上下文环境.
     */
    public static Context CONTEXT;
    /**
     * SP读写工具.
     */
    public static SharedPreferencesUtil sp;
    public static SharedPreferencesUtil sp_user;

    /**
     * SP文件名.
     */
    private final String SP_NAME = "oksocket";
    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT=getApplicationContext();
       // CameraWindow.show(CONTEXT);
        LogUtil.openLog();
        sp = new SharedPreferencesUtil(SP_NAME, SharedPreferencesUtil.PRIVATE, CONTEXT);
        OkSocket.initialize(this, true);
    }


}
