package com.example.data.tracker.mylibrary.AOP.expose;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class TrackerManager {
    private static final String TAG = "TrackerManager";
    private LifecycleCallback lifecycleCallback;

    private TrackerManager() {
        lifecycleCallback = new LifecycleCallback();
    }

    public static TrackerManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder{
        private static final TrackerManager INSTANCE = new TrackerManager();
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(lifecycleCallback);
    }

    private static void attachTrackRootView(Activity activity) {
        //LogUtil.i(TAG, "attachTrackRootView: " + activity.getClass().getSimpleName());
        ViewGroup root = activity.findViewById(android.R.id.content);
        //LogUtil.i(TAG, "attachTrackRootView: " + root.getClass().getSimpleName());
        if (root == null) {
            //LogUtil.e(TAG, "addTrackRootView: root is null");
            return;
        }
        if (root.getChildCount() > 0) {
            if (root.getChildAt(0) instanceof TrackRootView) {
                //LogUtil.i(TAG, "addTrackRootView: already add trackRootView");
                return;
            }
            TrackRootView trackRootView = new TrackRootView(activity);
            while (root.getChildAt(0) != null) {
                View child = root.getChildAt(0);
                root.removeViewAt(0);
                trackRootView.addView(child,child.getLayoutParams());
            }
            root.addView(trackRootView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //LogUtil.i(TAG, "attachTrackRootView: attach success");
        }
    }

    private static void detachTrackRootView(Activity activity) {
        //LogUtil.i(TAG, "detachTrackRootView: " + activity.getClass().getSimpleName());
        ViewGroup root = activity.findViewById(android.R.id.content);
        if (root == null) {
            return;
        }
        if (root.getChildCount() > 0 && root.getChildAt(0) instanceof TrackRootView) {
            root.removeViewAt(0);
        }
    }

    private class LifecycleCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            //LogUtil.i(TAG, "onActivityResumed: " + activity.getClass().getSimpleName());
            attachTrackRootView(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            //LogUtil.i(TAG, "onActivityDestroyed: " + activity.getClass().getSimpleName());
            detachTrackRootView(activity);
        }
    }
}
