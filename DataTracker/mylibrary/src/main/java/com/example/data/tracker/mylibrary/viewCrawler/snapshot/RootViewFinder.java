package com.example.data.tracker.mylibrary.viewCrawler.snapshot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * find RootView form Activity and store snapshot to RootViewInfo
 * Activity -> RootView ->snapshot -> RootViewInfo
 *
 * */
@TargetApi(16)
public class RootViewFinder implements Callable<RootViewInfo>{
    
    private static final String TAG = "RootViewFinder";

    private Activity activity;

    public RootViewFinder(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RootViewInfo call() throws Exception {
        String activityName = activity.getClass().getCanonicalName();
        View rootView = activity.getWindow().getDecorView().getRootView();
        RootViewInfo rootViewInfo = new RootViewInfo(rootView,activityName);
        loadSnapshot(rootViewInfo);
        return rootViewInfo;
    }

    private void loadSnapshot(RootViewInfo info) {
        View rootView = info.getRootView();
        float scale = 1.0f;
        info.setScale(scale);
        Bitmap rawBitmap = null;
        try {
            //1 createSnapshot 获取更清晰截图
            final Method createSnapshot = View.class.getDeclaredMethod("createSnapshot", Bitmap.Config.class, Integer.TYPE, Boolean.TYPE);
            createSnapshot.setAccessible(true);
            rawBitmap = (Bitmap) createSnapshot.invoke(rootView, Bitmap.Config.RGB_565, Color.WHITE, false);
        } catch (final NoSuchMethodException e) {
            Log.v(TAG, "Can't call createSnapshot, will use drawCache", e);
        } catch (final IllegalArgumentException e) {
            Log.d(TAG, "Can't call createSnapshot with arguments", e);
        } catch (final InvocationTargetException e) {
            Log.e(TAG, "Exception when calling createSnapshot", e);
        } catch (final IllegalAccessException e) {
            Log.e(TAG, "Can't access createSnapshot, using drawCache", e);
        } catch (final ClassCastException e) {
            Log.e(TAG, "createSnapshot didn't return a bitmap?", e);
        }

        Boolean originalCacheState = null;
        try {
            // 2 createSnapshot失败，调用DrawingCache
            if (null == rawBitmap) {
                originalCacheState = rootView.isDrawingCacheEnabled();
                rootView.setDrawingCacheEnabled(true);
                rootView.buildDrawingCache(true);
                rawBitmap = rootView.getDrawingCache();
            }
        } catch (final RuntimeException e) {
            Log.v(TAG, "Can't take a bitmap snapshot of view " + rootView + ", skipping for now.", e);
        }

        info.setSnapshot(rawBitmap);
        if (null != originalCacheState && !originalCacheState) {
            rootView.setDrawingCacheEnabled(false);
        }

    }
}
