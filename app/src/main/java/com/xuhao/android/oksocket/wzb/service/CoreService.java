package com.xuhao.android.oksocket.wzb.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.IPulseSendable;
import com.xuhao.android.libsocket.sdk.bean.ISendable;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;
import com.xuhao.android.libsocket.sdk.connection.NoneReconnect;
import com.xuhao.android.oksocket.MyApplication;
import com.xuhao.android.oksocket.wzb.camera.CameraService;
import com.xuhao.android.oksocket.wzb.receiver.LkAlarmReceiver;
import com.xuhao.android.oksocket.wzb.receiver.ReConnectAlarmReceiver;
import com.xuhao.android.oksocket.wzb.util.Cmd;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.xuhao.android.libsocket.sdk.OkSocket.open;

/**
 * @author wzb<wangzhibin_x@qq.com>
 * @date 2018-07-02	16:43
 */
public class CoreService extends Service{

    private ConnectionInfo mInfo;
    public static  IConnectionManager mManager;
    private OkSocketOptions mOkOptions;


    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
            Log.e("wzb","onSocketConnectionSuccess 连接成功");
            //String msg=Cmd.encode(Cmd.IMEI+Cmd.SPLIT+Cmd.LK);
            //mManager.send(new MsgDataBean(msg));
            context.startService(new Intent(context,LkLongRunningService.class));
            context.startService(new Intent(context,UdLongRunningService.class));
        }

        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
            if (e != null) {
                Log.e("wzb","onSocketDisconnection 异常断开"+e.getMessage());
            } else {
                Log.e("wzb","onSocketDisconnection 正常断开");
            }
            sendReConnect();
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            Toast.makeText(context, "连接失败" + e.getMessage(), LENGTH_SHORT).show();
            Log.e("wzb","onSocketConnectionFailed 连接失败");
            sendReConnect();
        }

        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);
            String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
            //logRece(str);
            Log.e("wzb","CoreService/onSocketReadResponse rece:"+str);
            parseData(str);
            Log.e("wzb","1111111");
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
            String str = new String(data.parse(), Charset.forName("utf-8"));
            Log.e("wzb","CoreService/onSocketWriteResponse send:"+str);
            //logSend(str);
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
            String str = new String(data.parse(), Charset.forName("utf-8"));
            //logSend(str);
        }
    };


    private void initSocket(){
        mInfo = new ConnectionInfo("192.168.16.101", 8282);
        mOkOptions = new OkSocketOptions.Builder()
                .setReconnectionManager(new NoneReconnect())
                .setWritePackageBytes(1024)
                .build();
        mManager = open(mInfo).option(mOkOptions);
        Log.e("wzb","initSocket mManager="+mManager);
        if(mManager !=null) mManager.registerReceiver(adapter);
        connect();

    }

    private void releaseSocket(){
        if(mManager != null){
            mManager.disconnect();
            mManager.unRegisterReceiver(adapter);
        }
    }

    private void sendReConnect(){
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        long triggerAtTime= SystemClock.elapsedRealtime() + 5*1000;
        Intent i = new Intent(this, ReConnectAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

    public static void connect(){
        if(mManager == null) return;
        if(!mManager.isConnect()) mManager.connect();
    }

    public static void disconnect(){
        if(mManager == null) return;
        if(mManager.isConnect())mManager.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("wzb","CoreService onStartCommand");
        initSocket();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseSocket();
        sendBroadcast(new Intent("com.android.custom.oksocket_reboot"));
    }

    private void parseData(String msg){
        List<String> msgArr= Arrays.asList(msg.split("\\*"));
        String imei=msgArr.get(1);
        String cmd=msgArr.get(2);
       // String info=msgArr.get(3).substring(cmd.length()+1);
        String info="";
        Log.e("wzb","parseData imei="+imei+",cmd="+cmd+",info="+info);
        String rspMsg="";
        switch (cmd){
            case "SOS1":
                break;
            case "CR":
                break;
            case "PHOTO":
                startService(new Intent(MyApplication.CONTEXT, CameraService.class));
                break;
            case "UPLOAD":
                MyApplication.sp.set("upload",info);
                rspMsg= Cmd.CS+Cmd.SPLIT+imei+Cmd.SPLIT+"UPLOAD";
                Cmd.send(rspMsg);
                break;
            default:
                break;
        }
    }
}
