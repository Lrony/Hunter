package com.example.hunter.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class ScreenUtils {

    static private ScreenUtils mInstance;

    static public ScreenUtils getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenUtils();
        }
        return mInstance;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 是否横屏
     *
     * @param context
     * @return true为横屏，false为竖屏
     */
    public boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否是平板
     * 这个方法是从 Google I/O App for Android 的源码里找来的，非常准确。
     *
     * @param context
     * @return
     */
    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
