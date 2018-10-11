package com.example.data.tracker.mylibrary.state;

import android.app.Activity;
import android.content.Context;

public class EditState implements ShakeState.ShakeListener,ActivityLifecycle.LifecycleListener {

    private ShakeState shakeState;
    private ActivityLifecycle activityLifecycle;
    private Activity activity;

    public EditState() {
        shakeState = new ShakeState(this);
        activityLifecycle = new ActivityLifecycle(this);
    }

    public void startListen(Context context) {
        shakeState.startListen(context);
        activityLifecycle.startListener(context);
    }

    @Override
    public void onShake() {

    }

    @Override
    public void onActivityChange(Activity activity) {
        this.activity = activity;
    }

    public Activity getCurrentActivity() {
        return activity;
    }
}
