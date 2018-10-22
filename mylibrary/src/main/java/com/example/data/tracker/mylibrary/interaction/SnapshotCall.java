package com.example.data.tracker.mylibrary.interaction;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * find RootView form Activity and store snapshot
 *
 * */
@TargetApi(16)
public class SnapshotCall implements Callable<Bitmap>{
    
    private static final String TAG = "SnapshotCall";

    private View rootView;

    public SnapshotCall(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public Bitmap call() throws Exception {
        return loadSnapshot(rootView);
    }

    private Bitmap loadSnapshot(View rootView) {
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

        if (null != originalCacheState && !originalCacheState) {
            rootView.setDrawingCacheEnabled(false);
        }

        return rawBitmap;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
