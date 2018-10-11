package com.example.data.tracker.mylibrary.state;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private LifecycleListener lifecycleListener;

    public ActivityLifecycle(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
    }

    public void startListener(Context context) {
        Application app = (Application) context.getApplicationContext();
        app.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        lifecycleListener.onActivityChange(activity);
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

    }

    public interface LifecycleListener{
        void onActivityChange(Activity activity);
    }
}
