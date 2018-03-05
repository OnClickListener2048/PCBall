package com.andrognito.patternlockdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dagou on 2018/2/23.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_warnning)
    TextView mTvWarnning;
    @BindView(R.id.iv_login_phone)
    ImageView mIvLoginPhone;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.iv_verification)
    ImageView mIvVerification;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.require_verify)
    TextView mRequireVerify;
    @BindView(R.id.tv_newAccount)
    TextView mTvNewAccount;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.laws)
    TextView mLaws;
    private CountDownTimerUtils mCountDownTimerUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mCountDownTimerUtils = new CountDownTimerUtils(mRequireVerify, 60000, 1000);

    }

    @OnClick({R.id.require_verify, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.require_verify:
                if (RegexUtils.isMobileExact(mEtUsername.getText())) {
                    mCountDownTimerUtils.start();
                } else {
                    ToastUtils.showShort("请输入正确的手机号码");
                }
                break;
            case R.id.btn_login:
                if (TextUtils.equals(getValue(mEtUsername), "13888888888") && TextUtils.equals("8888", getValue(mEtPassword))) {
                    ToastUtils.showShort("登录成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtils.showLong("请输入正确的验证码");
                }
                break;

                default:
        }
    }

    private String getValue(TextView textView) {
        return textView.getText().toString().trim();
    }
}
