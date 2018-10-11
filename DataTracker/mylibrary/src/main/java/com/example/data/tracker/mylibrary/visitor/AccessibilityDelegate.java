package com.example.data.tracker.mylibrary.visitor;

import android.util.Log;
import android.view.View;

public class AccessibilityDelegate extends View.AccessibilityDelegate {

    private static final String TAG = "AccessibilityDelegate";

    @Override
    public void sendAccessibilityEvent(View host, int eventType) {
        super.sendAccessibilityEvent(host, eventType);
        Log.e(TAG, "sendAccessibilityEvent: =================" + eventType);
    }


}
