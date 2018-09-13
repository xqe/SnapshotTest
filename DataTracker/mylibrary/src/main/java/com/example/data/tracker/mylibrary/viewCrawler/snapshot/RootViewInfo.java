package com.example.data.tracker.mylibrary.viewCrawler.snapshot;

import android.graphics.Bitmap;
import android.view.View;

public class RootViewInfo {

    private Bitmap snapshot;
    private View rootView;
    private String pageName;
    private float scale;

    public Bitmap getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Bitmap snapshot) {
        this.snapshot = snapshot;
    }

    public RootViewInfo(View viewRoot, String pageName) {
        this.rootView = viewRoot;
        this.pageName = pageName;
        scale = 1.0f;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public View getRootView() {
        return rootView;
    }

}
