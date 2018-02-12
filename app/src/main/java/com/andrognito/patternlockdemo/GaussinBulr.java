package com.andrognito.patternlockdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.Type;

/**
 * Created by wangxin on 2017/4/25.
 */

public class GaussinBulr {

    private RenderScript renderScript;

    public GaussinBulr(Context context) {
        renderScript = RenderScript.create(context);
    }

    public Bitmap blur(Bitmap bitmapOri, float radius) {

        Bitmap bitmap = Bitmap.createBitmap(bitmapOri);

        Allocation input = Allocation.createFromBitmap(renderScript, bitmapOri);

        Type type = input.getType();
        Allocation output = Allocation.createTyped(renderScript, type);

        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        scriptIntrinsicBlur.setRadius(radius);

        scriptIntrinsicBlur.setInput(input);

        scriptIntrinsicBlur.forEach(output);

        output.copyTo(bitmap);

        input.destroy();
        output.destroy();
        scriptIntrinsicBlur.destroy();
        type.destroy();
        return bitmap;
    }

    public void destory() {
        this.renderScript.destroy();
    }
}
