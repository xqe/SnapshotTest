package com.example.data.tracker.mylibrary.interaction.viewTree;

import android.app.Activity;
import android.view.View;

import java.util.concurrent.Callable;

public class ViewTreeCall implements Callable<ViewTreeInfo> {
    private Activity activity;

    public ViewTreeCall(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewTreeInfo call() throws Exception {
        return getViewTreeInfo(activity);
    }

    private ViewTreeInfo getViewTreeInfo(Activity activity) {
        String activityName = activity.getClass().getCanonicalName();
        View rootView = activity.getWindow().getDecorView().getRootView();
        ViewTreeInfo viewTreeInfo = null;

        return viewTreeInfo;
    }


}
