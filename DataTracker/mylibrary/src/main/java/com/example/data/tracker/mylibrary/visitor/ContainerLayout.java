package com.example.data.tracker.mylibrary.visitor;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ContainerLayout extends FrameLayout {

    private static final String TAG = "ContainerLayout sss";
    public ContainerLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.e(TAG, "onLayout: " );
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void dispatchVisibilityChanged(View changedView, int visibility) {
        Log.e(TAG, "dispatchVisibilityChanged: " + changedView.getClass().getSimpleName() + "," + visibility);
        super.dispatchVisibilityChanged(changedView, visibility);
    }


    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        Log.e(TAG, "dispatchWindowFocusChanged: " + hasFocus );
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
