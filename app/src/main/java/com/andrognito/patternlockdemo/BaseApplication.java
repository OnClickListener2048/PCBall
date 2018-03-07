package com.andrognito.patternlockdemo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wangxin on 2017/5/18.
 */

public class BaseApplication extends Application {
    public static final String TAG = "BaseApplication";
    /**
     * Activity
     */
    public List<BaseActivity> mActivityList = null;
    //在自己的Application中添加如下代码




    //在自己的Application中添加如下代码
    @Override
    public void onCreate() {

        super.onCreate();
//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        OkGo.getInstance().init(this);
        com.blankj.utilcode.util.Utils.init(this);

        MobSDK.init(this);
    }

    public void add(BaseActivity baseActivity) {
        if (mActivityList == null) {
            mActivityList = new ArrayList<>();
        }
        mActivityList.add(baseActivity);
        Log.d(TAG, "add: mActivityList.size() " + mActivityList.size());
    }

    public void remove(BaseActivity b) {
        if (mActivityList == null) {
            mActivityList = new ArrayList<>();
        }
        mActivityList.remove(b);
        Log.d(TAG, "add: mActivityList.size() " + mActivityList.size());
    }

    public int getActivityCount() {
        if (mActivityList != null) {
            return mActivityList.size();
        }
        return 0;
    }


}
