package com.xuhao.android.oksocket.wzb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuhao.android.oksocket.data.MsgDataBean;
import com.xuhao.android.oksocket.wzb.service.CoreService;
import com.xuhao.android.oksocket.wzb.util.Cmd;
import com.xuhao.android.oksocket.wzb.util.LogUtil;

/**
 * Created by Administrator on 2018-07-10.
 */

public class CommonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action=intent.getAction();
        LogUtil.logMessage("wzb","CommonReceiver action="+action);
        if(action.equals("com.android.custom.oksocket_reboot")){
            context.startService(new Intent(context, CoreService.class));
        }else if(action.equals("com.android.cutom.tracker_sos")){
            String sosMsg= Cmd.encode(Cmd.CS+Cmd.SPLIT+Cmd.IMEI+Cmd.SPLIT+"SSOS");
            CoreService.mManager.send(new MsgDataBean(sosMsg));
        }
    }
}
