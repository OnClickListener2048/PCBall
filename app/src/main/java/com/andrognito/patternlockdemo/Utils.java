package com.andrognito.patternlockdemo;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by wangxin on 2017/5/18.
 */

public class Utils {


    public static boolean ifLockPatternUsing(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            boolean keyguardSecure = ((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE)).isKeyguardSecure();
            return keyguardSecure;
        } else {
            try {
                Class<?> clazz = Class.forName("com.android.internal.widget.LockPatternUtils");
                Constructor<?> constructor = clazz.getConstructor(Context.class);
                constructor.setAccessible(true);
                Object utils = constructor.newInstance();
                Method method = clazz.getMethod("isSecure");
                return (Boolean) method.invoke(utils);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void sp_setBoolean(Context context,Boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OnlySP", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isTutorialPassed",b).commit();
    }

    public static boolean sp_getBoolean(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OnlySP", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("isTutorialPassed")) {
            return sharedPreferences.getBoolean("isTutorialPassed", false);
        }
        return false;
    }

    public static void putString(Context context,String string,String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OnlySP", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(name, string).commit();
    }

    public static String getString(Context context,String name,String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OnlySP", Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, defValue);
    }

    public static boolean sp_remove(Context context,String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OnlySP", Context.MODE_PRIVATE);
        return sharedPreferences.edit().remove(name).commit();
    }

    public static boolean putBoolean(Context context, boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OnlySP", Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean("isAllBlur", b).commit();
    }

    public static boolean getBoolean(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OnlySP", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isAllBlur", false);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 小米手机设置darkMode
     */
    public static boolean setXiaomiDarkMode(Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkModeFlag, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 魅族手机设置darkMode
     */
    public static boolean setMeizuDarkMode(Activity activity) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            value |= bit;
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (Exception e) {
        }
        return result;
    }
}
