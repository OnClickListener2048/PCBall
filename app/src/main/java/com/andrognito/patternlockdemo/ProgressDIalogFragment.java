package com.andrognito.patternlockdemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by dagou on 2018/2/23.
 */

public class ProgressDIalogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //消除Title区域
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //将背景变为透明
        setCancelable(false);  //点击外部不可取消

        View root = inflater.inflate(R.layout.fragment_progress_dialog, container);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        root.setLayoutParams(layoutParams);

        return root;
    }

    public static ProgressDIalogFragment newInstance() {
        return new ProgressDIalogFragment();
    }
}
