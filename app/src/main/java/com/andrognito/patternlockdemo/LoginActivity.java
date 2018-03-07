package com.andrognito.patternlockdemo;

import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

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
//                    sendCode(this);
                    sendCode("86",getValue(mEtUsername));

                } else {
                    ToastUtils.showShort("请输入正确的手机号码");
                }
                break;
            case R.id.btn_login:
//                if (TextUtils.equals(getValue(mEtUsername), "13888888888") && TextUtils.equals("8888", getValue(mEtPassword))) {
//                    ToastUtils.showShort("登录成功");
//                    setResult(RESULT_OK);
//                    finish();
//                } else {
//                    ToastUtils.showLong("请输入正确的验证码");
//                }
                submitCode("86",getValue(mEtUsername),getValue(mEtPassword));
                break;

                default:
        }
    }

    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        page.setRegisterCallback(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {

                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    ToastUtils.showShort("发送成功");
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                } else{
                    // TODO 处理错误的结果
                    ToastUtils.showShort("发送错误 请检查网络");
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    ToastUtils.showShort("登录成功");
                    Intent intent = new Intent();
                    intent.putExtra("phone", getValue(mEtUsername));
                    setResult(RESULT_OK,intent);
                    finish();
                } else{
                    // TODO 处理错误的结果
                    ToastUtils.showShort("验证失败 请填写正确的验证码");
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };

    private String getValue(TextView textView) {
        return textView.getText().toString().trim();
    }
}
