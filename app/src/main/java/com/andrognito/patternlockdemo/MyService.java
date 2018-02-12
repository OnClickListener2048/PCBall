package com.andrognito.patternlockdemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;


public class MyService extends Service {
    CallFromService mCallFromService;

    private ScreenBroadcastReceiver mScreenReceiver;
    private  ScreenStateListener mScreenStateListener;
    public IntentFilter filter1;
    public IntentFilter filter2;


    public MyService() {
        mScreenReceiver = new ScreenBroadcastReceiver();

    }

    public void setCallBackListener(CallFromService callFromService) {
        this.mCallFromService = callFromService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        begin(new ScreenStateListener() {
            @Override
            public void onScreenOn(CallFromService callFromService) {
                Log.e("onScreenOn", "onScreenOn111");
//                callFromService.callBack();
            }

            @Override
            public void onScreenOff(CallFromService callFromService) {
                Log.e("onScreenOff", "onScreenOff111");
                BaseApplication baseApplication = (BaseApplication) getApplication();
                int activityCount = baseApplication.getActivityCount();
                if (activityCount == 0) {
                    Intent intent = new Intent(MyService.this, LanucherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onUserPresent(CallFromService callFromService) {
                Log.e("onUserPresent", "onUserPresent");
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * screen状态广播接收者
     */
    public  class ScreenBroadcastReceiver extends BroadcastReceiver {

        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {

//            action = intent.getAction();
//            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
//                mScreenStateListener.onScreenOn(mCallFromService);
//            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
//                mScreenStateListener.onScreenOff(mCallFromService);
//            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
//                mScreenStateListener.onUserPresent(mCallFromService);
//            } else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
//                mScreenStateListener.onScreenOff(mCallFromService);
//            } else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
//                mScreenStateListener.onScreenOff(mCallFromService);
//            }
        }
    }

    /**
     * 开始监听screen状态
     *
     * @param listener
     */
    public void begin(ScreenStateListener listener) {
        mScreenStateListener = listener;
        registerListener();
        getScreenState();
    }

    /**
     * 获取screen状态
     */
    private void getScreenState() {
        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn(mCallFromService);
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff(mCallFromService);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterListener();
    }

    /**
     * 停止screen状态监听
     */
    public void unregisterListener() {
        unregisterReceiver(mScreenReceiver);
    }

    /**
     * 启动screen状态广播接收器
     */
    private void registerListener() {
        filter1 = new IntentFilter();
        filter1.setPriority(1000);
        filter1.addAction(Intent.ACTION_SCREEN_ON);
        filter1.addAction(Intent.ACTION_SCREEN_OFF);
        filter1.addAction(Intent.ACTION_USER_PRESENT);
        filter1.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter1.addAction(Intent.ACTION_BOOT_COMPLETED);


        filter2 = new IntentFilter();
        filter2.setPriority(1000);

        filter2.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter2.addDataScheme("file");
        registerReceiver(mScreenReceiver, filter1);
        registerReceiver(mScreenReceiver, filter2);
    }

    public interface ScreenStateListener {// 返回给调用者屏幕状态信息

        void onScreenOn(CallFromService callFromService);

        void onScreenOff(CallFromService callFromService);

        void onUserPresent(CallFromService callFromService);
    }


    public interface CallFromService {
        void callBack();
    }

    public class MyBinder extends Binder {
        public MyService getMyService() {
            return MyService.this;
        }

        public void setListener(CallFromService callFromService) {
            setCallBackListener(callFromService);
        }
    }
}
