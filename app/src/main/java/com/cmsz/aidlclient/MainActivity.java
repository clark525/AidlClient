package com.cmsz.aidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hangc.signaturepad.SignAidlInterface;

public class MainActivity extends Activity {


    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏

    }

    private void attempBind() {
        Intent intent = new Intent();
        intent.setAction("com.hangc.signature.aidl");
        intent.setPackage("com.hangc.signaturepad");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void satrtSign(View view) {
        byte[] msg = new byte[1024];
        try {
            int i = mAidl.satrtSign(10, 640, 1920, 360, 1080, "cc", "dd", msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        byte[] msg = new byte[1024];
        //        try {
        //            mAidl.satrtSign(30, 640, 1920, 360, 1080, "cc", "dd", msg);
        //        } catch (RemoteException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //}).start();
    }

    public void cancelSign(View view) {
        byte[] msg = new byte[1024];
        try {
            int i = mAidl.cancelSign(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void clearSign(View view) {
        byte[] msg = new byte[1024];
        try {
            int i = mAidl.clearSign(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void confirmSign(View view) {
        byte[] msg = new byte[1024];
        try {
            int i = mAidl.confirmSign(msg);
            Toast.makeText(this, i + "--", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attempBind();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            byte[] msg = new byte[1024];
            try {
                int i = mAidl.cancelSign(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private SignAidlInterface mAidl = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
            mAidl = SignAidlInterface.Stub.asInterface(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
            mBound = false;
        }
    };

}
