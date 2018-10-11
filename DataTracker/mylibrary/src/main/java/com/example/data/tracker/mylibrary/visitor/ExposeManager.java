package com.example.data.tracker.mylibrary.visitor;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class ExposeManager {

    private static final String TAG = "ExposeManager";

    public static boolean checkExposureViewDimension(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        Rect rect = new Rect();
        boolean isVisibleRect = view.getGlobalVisibleRect(rect);
        Log.e(TAG, "checkExposureViewDimension: " + isVisibleRect);
        if (isVisibleRect) {
            int visibleWidth = rect.width();
            int visibleHeight = rect.height();
            return (visibleWidth * 1.00 / width > 0.8) && (visibleHeight * 1.00 / height > 0.8);
        } else {
            return false;
        }
    }
}
