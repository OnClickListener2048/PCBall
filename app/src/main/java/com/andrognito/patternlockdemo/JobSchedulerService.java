package com.andrognito.patternlockdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by wangxin on 2017/6/1.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            startService(new Intent(JobSchedulerService.this, MyService.class));
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);

            return true;
        }
    });
    @Override
    public boolean onStartJob(JobParameters params) {
        Message message = Message.obtain();
        message.obj = params;
        handler.sendMessage(message);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        handler.removeCallbacksAndMessages(null);
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
