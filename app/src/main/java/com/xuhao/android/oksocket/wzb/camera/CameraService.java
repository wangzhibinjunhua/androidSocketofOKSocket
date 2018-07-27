package com.xuhao.android.oksocket.wzb.camera;

/**
 * Created by Administrator on 2018-07-17.
 */


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;

import com.xuhao.android.oksocket.wzb.util.Cmd;
import com.xuhao.android.oksocket.wzb.util.WakeLockManager;


/**
 * 后台拍照服务，配合全局窗口使用
 *
 * @author WuRS
 */
public class CameraService extends Service implements PictureCallback {

    private static final String TAG = CameraService.class.getSimpleName()+"wzb";

    private Camera mCamera;

    private boolean isRunning; // 是否已在监控拍照

    private String commandId; // 指令ID

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate...");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WakeLockManager.acquire(this);
        Log.d(TAG, "onStartCommand...");
        startTakePic(intent);
        return START_NOT_STICKY;
    }

    private void startTakePic(Intent intent) {
        Log.e(TAG,"isRunning="+isRunning);
        if (!isRunning) {
            commandId = intent.getStringExtra("commandId");
            SurfaceView preview = CameraWindow.getDummyCameraView();
            //if (!TextUtils.isEmpty(commandId) && preview != null) {
            Log.e(TAG,"preview="+preview);
            if (preview != null) {
                autoTakePic(preview);
            } else {
                stopSelf();
            }
        }

    }

    private void autoTakePic(SurfaceView preview) {
        Log.d(TAG, "autoTakePic...");
        isRunning = true;
        mCamera = getFacingBackCamera();
        if (mCamera == null) {
            Log.w(TAG, "getFacingFrontCamera return null");
            stopSelf();
            return;
        }
        //set param
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
        for (int i = 0; i < sizes.size(); i++) {
            if (sizes.get(i).width < size.width)
                size = sizes.get(i);
        }
        params.setPictureSize(size.width, size.height);
        mCamera.setParameters(params);
        //end

        try {
            mCamera.setPreviewDisplay(preview.getHolder());
            mCamera.startPreview();// 开始预览
            // 防止某些手机拍摄的照片亮度不够
            Thread.sleep(200);
            takePicture();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
            stopSelf();
        }
    }

    private void takePicture() throws Exception {
        Log.d(TAG, "takePicture...");
        try {
            mCamera.takePicture(null, null, this);
        } catch (Exception e) {
            Log.d(TAG, "takePicture failed!");
            e.printStackTrace();
            throw e;
        }
    }

    private Camera getFacingBackCamera() {
        CameraInfo cameraInfo = new CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                try {
                    return Camera.open(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Camera getFacingFrontCamera() {
        CameraInfo cameraInfo = new CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    return Camera.open(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d(TAG, "onPictureTaken...");
        releaseCamera();
        try {
            Options opts=new Options();
            opts.inSampleSize=2;
            Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length,opts);
            byte[] newData=Bitmap2Bytes(bitmap);
            if(savePic(newData, new File("/sdcard/custompic.jpg"))){
                String picBase64Str= Base64.encodeToString(newData,Base64.DEFAULT);
                String rspMsg= Cmd.CS+Cmd.SPLIT+Cmd.IMEI+Cmd.SPLIT+"PHOTO,"+picBase64Str;
                Cmd.send(rspMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
        }finally {
            stopSelf();
        }
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    // 保存照片
    private boolean savePic(byte[] data, File savefile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savefile);
            fos.write(data);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            Log.d(TAG, "releaseCamera...");
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy...");
        commandId = null;
        isRunning = false;
        releaseCamera();
        WakeLockManager.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

