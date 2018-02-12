package com.andrognito.patternlockdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wangxin on 2017/5/29.
 */

public class BaseActivity extends AppCompatActivity {
    private BaseApplication mApplication;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mApplication = (BaseApplication) this.getApplication();
        mApplication.add(this);
    }
}
