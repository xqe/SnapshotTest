package com.example.data.tracker.mylibrary.expose;

import android.util.Log;

public class LogUtil {

    private static final String DIFFER = "VivoData.";

    public static void i(String TAG,String msg) {
        Log.i(DIFFER + TAG, msg);
    }

    public static void e(String TAG,String msg) {
        Log.e(DIFFER + TAG, msg);
    }
}
