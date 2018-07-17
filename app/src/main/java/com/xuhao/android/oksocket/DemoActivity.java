package com.xuhao.android.oksocket;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xuhao.android.oksocket.wzb.camera.CameraService;
import com.xuhao.android.oksocket.wzb.service.LkLongRunningService;
import com.xuhao.android.oksocket.wzb.service.UdLongRunningService;
import com.xuhao.android.oksocket.wzb.util.Cmd;
import com.xuhao.android.oksocket.wzb.service.CoreService;
import com.xuhao.android.oksocket.wzb.util.PermissionUtils;

/**
 * Created by didi on 2018/4/20.
 */

public class DemoActivity extends AppCompatActivity {

    private Button mSimpleBtn;

    private Button mComplexBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        mSimpleBtn = findViewById(R.id.btn1);
        mComplexBtn = findViewById(R.id.btn2);


        mSimpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this, SimpleDemoActivity.class);
                startActivity(intent);
            }
        });
        mComplexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this, ComplexDemoActivity.class);
                startActivity(intent);
            }
        });
        initPermission();
        test();
    }

    void test(){
        String imei= Cmd.IMEI;
        Log.e("wzb","test imei="+imei);
        startService(new Intent(this, CoreService.class));
        //startService(new Intent(this, LkLongRunningService.class));
       // startService(new Intent(this, CameraService.class));
        //UdLongRunningService.packUdInfo("A,-23.22,11.11");
    }

    private void initPermission(){
        PermissionUtils.requestPermissionsResult(this, 1, new String[]{    //权限提醒
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.READ_PHONE_STATE}
                , new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied() {
                        // PermissionUtils.showTipsDialog(CONTEXT);
                    }
                }
        );
    }
}
