package com.andrognito.patternlockdemo;

import android.Manifest;
import android.animation.Animator;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.qiujuer.genius.blur.StackBlur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;


public class LanucherActivity extends BaseActivity implements MyService.CallFromService, Animation.AnimationListener, View.OnClickListener, Animator.AnimatorListener {

    private int count = 0;
    private View positionView;
    public TextView profile_name;
    public ImageView profile_image;
    public GaussinBulr gaussin;
    public LinearLayout linearLayout;
    private int blurRadius = 16;
    private int retryCount = 0;
    private FrameLayout frameLayout;
    private static final String TAG = "LanucherActivity";
    private PopupWindow popUpWindow;
    private PatternLockView mPatternLockView;
    private WindowManager mWindowManager;
    public View view;
    private View ensureView;
    private int seconds = 10;
    public FrameLayout fl_loading;
    public TextView tv_front;
    public TextView tv_back;
    public TextView tv_complete;
    private int REQUEST_CODE = 200;
    private static ArrayList<String> photos;
    public Bitmap bitmap2;
    public Bitmap decodeFile2;


    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MyService.MyBinder b = (MyService.MyBinder) service;
            b.setListener(LanucherActivity.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

    };

    private Handler popupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    addView();

                    break;
                case 1:
                    popUpWindow.showAtLocation(View.inflate(getBaseContext(), R.layout.activity_me02010100, null), Gravity.CENTER, 0, 0);
                    frameLayout.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mPatternLockView.clearPattern();
                    break;
                case 3:
                    view.animate().scaleX(2f).scaleY(2f).setDuration(300).alpha(0.3f).start();
                    finish();
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_close);
                    break;
                case 4:
                    startCounting();
                    break;
                case 5:
                    fl_loading.setVisibility(View.GONE);
                    break;
                default:
            }
        }

        private void startCounting() {
            profile_name.setText("Unlock after " + seconds + " seconds");
            seconds--;
            if (seconds == 0) {
                profile_name.setText("Draw the pattern");
                reset();
                return;
            }
            popupHandler.sendEmptyMessageDelayed(4, 1000);
        }

    };
    public Bitmap decodeFile;
    public Bitmap bitmap;


    public void reset() {
        mPatternLockView.setInputEnabled(true);
        retryCount = 0;
        seconds = 10;
        popupHandler.removeMessages(4);
        profile_name.setTextColor(getResources().getColor(R.color.header));

    }

    private PatternLockViewListener mPatternLockViewListenerBeforeTutorial = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started1111");
            profile_name.setText("完成后松开手指");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: 11111" +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete:11111 " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));

            String result = PatternLockUtils.patternToString(mPatternLockView, pattern);

            char[] chars = result.toCharArray();

            Log.d(getClass().getName(), "onComplete: " + chars.length);

            String savePattern = Utils.getString(LanucherActivity.this, "savePattern", "noPattern");

            if (TextUtils.equals(savePattern, "noPattern")) {

                Utils.putString(LanucherActivity.this, result, "savePattern");
                profile_name.setText("再次绘制图案进行确认");
                popupHandler.sendEmptyMessageDelayed(2, 2000);

            } else if (TextUtils.equals(savePattern, result)) {
                Utils.putString(LanucherActivity.this, result, "finalPattern");
                profile_name.setText("密码设置成功");
                Utils.sp_setBoolean(LanucherActivity.this, true);
                popupHandler.sendEmptyMessageDelayed(3, 1000);
            } else {
                retryCount++;
                if (retryCount == 3) {
                    tv_reset.setVisibility(View.VISIBLE);
                }
                profile_name.setText("两次输入的密码有误，请重试");
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                popupHandler.sendEmptyMessageDelayed(2, 1000);
                return;
            }

            if (chars.length < 4) {
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                profile_name.setText("至少需连接4个点，请重试");
                popupHandler.sendEmptyMessageDelayed(2, 1000);
                return;
            }


        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared11111");
        }
    };

    private PatternLockViewListener mPatternLockViewListenerAfterTutorial = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started1111");
            profile_name.setText("Release to finish");
            profile_name.setTextColor(getResources().getColor(R.color.header));
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: 11111" +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete:11111 " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
            String result = PatternLockUtils.patternToString(mPatternLockView, pattern);
            String finalPattern = Utils.getString(LanucherActivity.this, "finalPattern", "nothing");
            if (TextUtils.equals(result, finalPattern)) {
                profile_name.setText("Welcome! Miss Yuan!");
                popupHandler.sendEmptyMessageDelayed(3, 500);
            } else {
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                popupHandler.sendEmptyMessageDelayed(2, 2000);
                profile_name.setText("No Good! Do it again!");
                profile_name.setTextColor(getResources().getColor(R.color.color_red));
                retryCount++;
                if (retryCount == 3) {
                    tv_reset.setVisibility(View.VISIBLE);
                    popupHandler.sendEmptyMessage(4);
                    mPatternLockView.setInputEnabled(false);
                }
            }
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared11111");
        }
    };
    private BaseApplication mApplication;
    public TextView tv_reset;

    private void addView() {
        setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanucher);
        Log.d(TAG, "onCreate: ");
        mApplication = (BaseApplication) this.getApplication();
        mApplication.add(this);
        checkPerm();

        initView();
        initPopUpWindow();



        if (Utils.ifLockPatternUsing(this)) {
            popupHandler.sendEmptyMessageDelayed(1, 200);
        }

        if (Utils.sp_getBoolean(this)) {
            mPatternLockView.addPatternLockListener(mPatternLockViewListenerAfterTutorial);
            profile_name.setText("Draw the pattern");
        } else {
            mPatternLockView.addPatternLockListener(mPatternLockViewListenerBeforeTutorial);
            profile_name.setText("绘制解锁图案，请至少连接4个点");
        }
        if (Utils.getBoolean(this)) {
            if (photos != null) {
                String path = photos.get(new Random().nextInt(photos.size()));
                int indexOf = path.lastIndexOf("/");
                String substring = path.substring(indexOf);
                decodeFile = BitmapFactory.decodeFile(path);
                bitmap = readFromSDCard(substring);
                WeakReference<Bitmap> weakReferenceImage = new WeakReference<>(decodeFile);
                WeakReference<Bitmap> weakReferenceBackground = new WeakReference<>(bitmap);

                profile_image.setImageBitmap(weakReferenceImage.get());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    linearLayout.setBackground(new BitmapDrawable(weakReferenceBackground.get()));
                }

            }

        } else {
        }

        popupHandler.sendEmptyMessageDelayed(0, 1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int jobId = 1;
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(jobId, new ComponentName(getPackageName(), JobSchedulerService.class.getName()))
                    .setPeriodic(1000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setRequiresDeviceIdle(false)
                    .build();
            jobScheduler.schedule(jobInfo);
        }
    }

    private void checkPerm() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.SYSTEM_ALERT_WINDOW

                , Manifest.permission.RECEIVE_BOOT_COMPLETED
                , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                , Manifest.permission.BROADCAST_STICKY
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Permission permission) {
                if (permission.granted) {
                    Log.d(TAG, "onNext: granted permission.name"+permission.name);
                } else {
                    Log.d(TAG, "onNext: denid"+permission.name);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void startBlur(final ArrayList<String> photos) {
        Log.d(TAG, "subscribe: 订阅1");
        fl_loading.setVisibility(View.VISIBLE);
        tv_front.setVisibility(View.VISIBLE);
        tv_back.setVisibility(View.VISIBLE);
        Log.d(TAG, "subscribe: 订阅2");
        tv_complete.setText("/");
        tv_front.setText("0");

        Log.d(TAG, "subscribe: 订阅3");
        tv_back.setText("" + photos.size());
        Log.d(TAG, "subscribe: 订阅4");
        gaussin = new GaussinBulr(this);
        DisposableObserver<Bitmap> disposableObserver = Observable.create(new ObservableOnSubscribe<Bitmap>() {

            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {

                for (int i = 0; i < photos.size(); i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(photos.get(i));
                    Bitmap bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    bitmap.recycle();
                    Log.d(TAG, "subscribe: bitmapCopy size before compress" + bitmapCopy.getByteCount() / 1024);
                    Bitmap compressImage = BitmapUtils.compressImage(bitmapCopy);
                    Log.d(TAG, "subscribe: bitmapCopy size after compress" + compressImage.getByteCount() / 1024);
//                    Bitmap blur = gaussin.blur(bitmapCopy, blurRadius);
                    int radius = 25;
                    Bitmap blur = StackBlur.blurNativelyPixels(compressImage, radius, false);

                    String path = photos.get(i);
                    int indexOf = path.lastIndexOf("/");
                    String substring = path.substring(indexOf);
                    saveInSDCard(blur, substring);
                    e.onNext(blur);
                    bitmapCopy.recycle();
                    blur.recycle();

                }
                e.onComplete();

            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Bitmap>() {


                    @Override
                    public void onNext(Bitmap bitmap) {
                        count++;
                        tv_front.setText(count + "");
                        bitmap.recycle();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        gaussin.destory();
                        tv_back.setVisibility(View.INVISIBLE);
                        tv_front.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onComplete: onCompleteonCompleteonComplete");
                        tv_complete.setText("高斯模糊完毕！");
                        popupHandler.sendEmptyMessageDelayed(5, 1000);
                        String path = photos.get(new Random().nextInt(photos.size()));
                        int indexOf = path.lastIndexOf("/");
                        String substring = path.substring(indexOf);
                        decodeFile = BitmapFactory.decodeFile(path);
                        bitmap = readFromSDCard(substring);
                        if (bitmap == null) {
                            Log.d(TAG, "onComplete:bitmap是空的！！！！！ ");
                            return;
                        }
                        profile_image.setImageBitmap(decodeFile);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            linearLayout.setBackground(new BitmapDrawable(bitmap));
                        }
                        Utils.putBoolean(LanucherActivity.this, true);
//                        decodeFile.recycle();
//                        bitmap.recycle();
                        System.gc();

                    }
                });

    }

    private Bitmap readFromSDCard(String name) {
        File f = new File(Environment.getExternalStorageDirectory().getPath(), name);
        if (f.exists()) {
            double used = ((f.getTotalSpace() - f.getFreeSpace()) / 1024.0 / 1024 / 1024);
            double total = f.getTotalSpace() / 1024.0 / 1024 / 1024;
            double free = f.getFreeSpace() / 1024 / 1024 / 1024.0;
            Log.d(TAG, "readFromSDCard: File文件是存在的:used:" + used + "-----" + "total:" + total + "-------" + "free:" + free);

            return BitmapFactory.decodeFile(f.getPath());
        }
        return null;
    }

    private void saveInSDCard(Bitmap bitmap, String name) {
        Log.e(TAG, "保存图片" + name);
        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/" + name);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            bitmap.recycle();
            out.flush();
            out.close();
            Log.i(TAG, "已经保存" + Environment.getExternalStorageDirectory().getPath() + "/" + name);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void initView() {
        view = View.inflate(this, R.layout.activity_main, null);
        fl_loading = (FrameLayout) view.findViewById(R.id.fl);
        tv_complete = (TextView) view.findViewById(R.id.tv_complete);
        tv_front = (TextView) view.findViewById(R.id.tv_front);
        tv_back = (TextView) view.findViewById(R.id.tv_back);
        tv_reset = (TextView) view.findViewById(R.id.reset);
        positionView = view.findViewById(R.id.positionView);
        linearLayout = (LinearLayout) view.findViewById(R.id.main);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        View FlamelayoutView = View.inflate(this, R.layout.activity_me02010100, null);
        frameLayout = (FrameLayout) FlamelayoutView.findViewById(R.id.fl_alpha);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mPatternLockView = (PatternLockView) view.findViewById(R.id.patter_lock_view);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.header));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        tv_reset.setOnClickListener(this);
        fl_loading.setOnClickListener(this);
        profile_image.setOnClickListener(this);
    }

    private void initPopUpWindow() {
        ensureView = View.inflate(getBaseContext(), R.layout.dialog_weixin_copy, null);
        popUpWindow = new PopupWindow(ensureView
                , LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT);
        popUpWindow.setFocusable(true);
        popUpWindow.setOutsideTouchable(true);
        popUpWindow.setBackgroundDrawable(new BitmapDrawable());
        ensureView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                frameLayout.setVisibility(View.INVISIBLE);
            }
        });

        Button mBtnYes = (Button) ensureView.findViewById(R.id.btn_dialog_yes);
        Button mBtnNo = (Button) ensureView.findViewById(R.id.btn_dialog_no);
        mBtnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent in = new Intent(Settings.ACTION_PRINT_SETTINGS);
//                startActivity(in);

                popUpWindow.dismiss();
            }
        });
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popUpWindow.dismiss();
                finish();
            }
        });
//        popUpWindow.setAnimationStyle(R.style.popupwindow);
        popUpWindow.update();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (decodeFile != null) {
            decodeFile.recycle();
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (decodeFile2 != null) {
            decodeFile2.recycle();
        }
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        mApplication.remove(this);
        mApplication.mActivityList.clear();
        mApplication.mActivityList = null;
        try {
            mWindowManager.removeView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        retryCount = 0;
        super.onDestroy();

    }


    @Override
    public void callBack() {
        Log.d(TAG, "callBack: callBackcallBackcallBackcallBack");

    }



    @Override
    public void onAnimationStart(Animation animation) {
        Log.d(TAG, "onAnimationStart: ");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG, "onAnimationEnd: ");
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_close);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

        Log.d(TAG, "onAnimationRepeat: ");
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                profile_name.setTextColor(ResourceUtils.getColor(this, R.color.header));
                tv_reset.setVisibility(View.INVISIBLE);
                reset();
                Utils.sp_setBoolean(this, false);
                Utils.sp_remove(this, "savePattern");
                Utils.sp_remove(this, "finalPattern");
                mPatternLockView.addPatternLockListener(mPatternLockViewListenerBeforeTutorial);
                profile_name.setText("绘制解锁图案，请至少连接4个点");
                break;
            case R.id.profile_image:
                PhotoPickerIntent intent = new PhotoPickerIntent(LanucherActivity.this);
                intent.setPhotoCount(9);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addView();
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Log.d(TAG, "onActivityResult: photos.size() " + photos.size());
                startBlur(photos);
            }
        }
    }



    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
