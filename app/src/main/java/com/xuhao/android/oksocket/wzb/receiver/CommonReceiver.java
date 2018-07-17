package com.xuhao.android.oksocket.wzb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuhao.android.oksocket.wzb.service.CoreService;

/**
 * Created by Administrator on 2018-07-10.
 */

public class CommonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action=intent.getAction();
        if(action.equals("com.android.custom.oksocket_reboot")){
            context.startService(new Intent(context, CoreService.class));
        }
    }
}
