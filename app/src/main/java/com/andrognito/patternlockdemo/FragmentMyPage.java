package com.andrognito.patternlockdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dagou on 2018/2/22.
 */

public class FragmentMyPage extends Fragment {

    @BindView(R.id.red_new_dot)
    ImageView mRedNewDot;
    @BindView(R.id.fl_notification)
    FrameLayout mFlNotification;
    @BindView(R.id.fl_img_head)
    FrameLayout mFlImgHead;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_creditRating)
    TextView mTvCreditRating;
    @BindView(R.id.topView)
    LinearLayout mTopView;
    @BindView(R.id.login)
    TextView mLogin;
    @BindView(R.id.helper)
    TextView mHelper;
    @BindView(R.id.cleardata)
    TextView mCleardata;
    @BindView(R.id.caipiaochangshi)
    TextView mCaipiaochangshi;
    Unbinder unbinder;
    private ProgressDIalogFragment mProgressDIalogFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        unbinder = ButterKnife.bind(this, view);
        initProgressBar();
        return view;

    }

    private void initProgressBar() {
        mProgressDIalogFragment = ProgressDIalogFragment.newInstance();

    }

    public static FragmentMyPage newInstance(Bundle bundle) {
        FragmentMyPage fragmentMyPage = new FragmentMyPage();
        fragmentMyPage.setArguments(bundle);
        return fragmentMyPage;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.login, R.id.helper, R.id.cleardata, R.id.caipiaochangshi,R.id.fl_notification, R.id.tv_userName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                startActivityForResult(new Intent(getContext(), LoginActivity.class),2);
                break;
            case R.id.helper:
                Intent intent2 = new Intent();
                intent2.putExtra("url", "http://bbs.zhcw.com");
                intent2.setClass(getContext(), WebActivity.class);
                startActivity(intent2);
                break;
            case R.id.cleardata:
                showProgressDialog();

                Observable.timer(3000, TimeUnit.MILLISECONDS, Schedulers.newThread()).subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                mProgressDIalogFragment.dismissAllowingStateLoss();
                                ToastUtils.showLong("清除成功！");
                            }
                        });
                break;
            case R.id.caipiaochangshi:
                Intent intent1 = new Intent();
                intent1.putExtra("url", "http://caipiao.163.com/help/");
                intent1.setClass(getContext(), WebActivity.class);
                startActivity(intent1);
                break;
            case R.id.fl_notification:
                ToastUtils.showLong("正在开发中，敬请恭候～");
                break;
            case R.id.tv_userName:
                startActivityForResult(new Intent(getContext(), LoginActivity.class),2);
                break;
            default:
        }
    }

    private void showProgressDialog() {
        if (!mProgressDIalogFragment.isVisible()) {
            mProgressDIalogFragment.show(getActivity().getSupportFragmentManager(), "mypage");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2&&resultCode == RESULT_OK) {
            mTvUserName.setText("138****8888");
        }
    }
}
