package com.andrognito.patternlockdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

/**
 * Created by dagou on 2018/2/16.
 */

public class MainAc extends BaseActivity {

    private Fragment mFragment;
    private Fragment mThird;
    private Fragment mSecond;
    private Fragment mFirst;
    private FragmentMyPage mForth;
    private long mExitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("500彩票网");
        setSupportActionBar(toolbar);
        Bundle bundle = new Bundle();
        bundle.putString("url", "http://m.500.com");
        mFirst = FragmentWeb.newInstance(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putString("url", "http://caipiao.163.com/t/");
        mSecond = FragmentWeb.newInstance(bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putString("url", "http://m.hao123.com/n/v/caipiao");
        mThird = FragmentWeb.newInstance(bundle3);
        mForth = FragmentMyPage.newInstance(new Bundle());
        addFragment(R.id.container,mForth);
        addFragment(R.id.container,mSecond);
        addFragment(R.id.container,mThird);
        addFragment(R.id.container,mFirst);
        PageNavigationView pageNavigationView = (PageNavigationView) findViewById(R.id.bottom_tab);
        final NavigationController navigationController = pageNavigationView.material()
                .addItem(R.drawable.ic_airport_shuttle_black_24dp, "500彩票网")
                .addItem(R.drawable.ic_location_city_black_24dp, "网易彩票")
                .addItem(R.drawable.ic_event_note_black_24dp, "好彩票")
                .addItem(R.drawable.ic_account_circle_black_24dp,"个人中心")
                .build();
        navigationController.setSelect(0);
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {


            @Override
            public void onSelected(int index, int old) {
                switch (index) {
                    case 0:
                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.setTitle("500彩票网");
                        navigationController.setSelect(0);
                        addFragment(R.id.container,mFirst);
                        break;
                    case 1:
                        toolbar.setTitle("网易彩票");
                        toolbar.setVisibility(View.VISIBLE);
                        navigationController.setSelect(1);
                        addFragment(R.id.container,mSecond);
                        break;
                    case 2:
                        toolbar.setTitle("好彩票网");
                        toolbar.setVisibility(View.VISIBLE);
                        navigationController.setSelect(2);
                        addFragment(R.id.container,mThird);
                        break;
                    case 3:
                        toolbar.setVisibility(View.GONE);
                        navigationController.setSelect(3);
                        addFragment(R.id.container,mForth);
                    default:
                }
            }

            @Override
            public void onRepeat(int index) {

            }
        });
    }

    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtils.d("mFirst.isVisible()"+mFirst.isVisible());
            LogUtils.d("mSecond.isVisible()"+mSecond.isVisible());
            LogUtils.d("mThird.isVisible()"+mThird.isVisible());
            if (mFirst.isVisible()) {
                if (((FragmentWeb) mFirst).getAgentWeb().back()) {
                    ((FragmentWeb) mFirst).getAgentWeb().handleKeyEvent(keyCode, event);
                    return true;
                }
            }

            if (mSecond.isVisible()) {
                if (((FragmentWeb) mSecond).getAgentWeb().back()) {
                    ((FragmentWeb) mSecond).getAgentWeb().handleKeyEvent(keyCode, event);
                    return true;
                }
            }

            if (mThird.isVisible()) {
                if (((FragmentWeb) mThird).getAgentWeb().back()) {
                    ((FragmentWeb) mThird).getAgentWeb().handleKeyEvent(keyCode, event);
                    return true;
                }
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.showLong("再按一次退出PC蛋蛋");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
