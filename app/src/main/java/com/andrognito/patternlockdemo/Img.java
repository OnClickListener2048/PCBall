package com.andrognito.patternlockdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxin on 2017/5/22.
 */

public class Img {
    private  Drawable drawable;

    private Img() {

    }

    private Img(Drawable drawable) {
        this.drawable = drawable;
    }

    public static List<Img> mocks(Context c) {
        List<Img> imgs = new ArrayList<>(7);


        return imgs;
    }

    public Drawable getImage() {
        return drawable;
    }

    private static Img fromRes(Context c, @DrawableRes int img) {
        return new Img(ContextCompat.getDrawable(c, img));
    }
}
