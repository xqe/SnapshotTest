package com.example.data.tracker.mylibrary.AOP;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.data.tracker.mylibrary.AOP.expose.LogUtil;
import com.example.data.tracker.mylibrary.AOP.expose.TrackRootView;

public class ScreenState implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ScreenState";
    private OnScreenChangeListener onScreenChangeListener;
    private Context context;

    public ScreenState(Context context, OnScreenChangeListener onScreenChangeListener) {
        this.context = context;
        this.onScreenChangeListener = onScreenChangeListener;
        ((Application)context).registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(final Activity activity) {
        LogUtil.i(TAG, "onActivityResumed: " + activity.getClass().getSimpleName());
        onScreenChangeListener.onActivityChange(activity);
        TrackRootView trackRootView = findTrackRootView(activity);
        if (trackRootView != null) {
            trackRootView.setLayoutChangeListener(new TrackRootView.LayoutChangeListener() {
                @Override
                public void onLayoutChange() {
                    LogUtil.i(TAG, "onLayoutChange: ");
                    onScreenChangeListener.onLayoutChange(activity);
                }
            });
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.i(TAG, "onActivityStopped: " + activity.getClass().getSimpleName());
        TrackRootView trackRootView = findTrackRootView(activity);
        if (trackRootView != null) {
            trackRootView.removeLayoutChangeListener();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private TrackRootView findTrackRootView(Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        if (viewGroup.getChildAt(0) instanceof TrackRootView) {
            return (TrackRootView) viewGroup.getChildAt(0);
        }
        return null;
    }

    public void close() {
        ((Application)context).unregisterActivityLifecycleCallbacks(this);
    }

    public interface OnScreenChangeListener {
        void onActivityChange(Activity activity);
        void onLayoutChange(Activity activity);
    }

}
