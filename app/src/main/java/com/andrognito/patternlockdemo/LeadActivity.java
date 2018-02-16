package com.andrognito.patternlockdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by edz on 2018/2/12.
 */

public class LeadActivity extends BaseActivity {
    private String testId = "1315209639";
    private String id = "540622134";
    private  final String URL = "https://appid-apkk.xx-app.com/frontApi/getAboutUs?appid="+id;
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Intent intent = new Intent();
                intent.setClass(LeadActivity.this, WebActivity.class);
                intent.putExtra("url", mBody.getWapurl());
                startActivity(intent);

            } else {
                Intent intent = new Intent(LeadActivity.this, MainAc.class);
                startActivity(intent);

            }
            finish();
        }
    };


    private Bean mBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable.empty().subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });

        OkGo.<Bean>get(URL).execute(new JsonCallback<Bean>(Bean.class) {
            @Override
            public void onSuccess(Response<Bean> response) {

                mBody = response.body();
                if ("1".equals(mBody.getIsshowwap())) {
                    mHandler.sendEmptyMessageDelayed(1, 2000);
                } else {
                    mHandler.sendEmptyMessageDelayed(0, 2000);
                }
            }
        });

    }

}
