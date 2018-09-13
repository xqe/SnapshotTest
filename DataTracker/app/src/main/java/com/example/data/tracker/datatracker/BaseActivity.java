package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.view.MotionEvent;

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /*if (ev.getAction() == MotionEvent.ACTION_UP) {
            View targetView = ViewCrawler.getClickView(this,ev);
            if (targetView != null) {
                String viewID = ViewIDMaker.getViewID(targetView);
                Log.e(TAG, "dispatchTouchEvent: viewID = " + viewID);
            } else {
                Log.e(TAG, "dispatchTouchEvent: targetView == null");
            }
        }*/
        return super.dispatchTouchEvent(ev);
    }
}
