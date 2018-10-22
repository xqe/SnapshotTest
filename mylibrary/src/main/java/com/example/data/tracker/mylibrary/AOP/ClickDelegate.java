package com.example.data.tracker.mylibrary.AOP;

import android.util.Log;
import android.view.View;

import com.example.data.tracker.mylibrary.R;
import com.example.data.tracker.mylibrary.configCenter.BindEvent;

import java.util.Map;

public class ClickDelegate {
    private static final String TAG = "ClickDelegate";

    public static void setClickDelegate(View view, final BindEvent bindEvent) {
        if (view == null || bindEvent == null) {
            return;
        }
        view.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @SuppressWarnings("unchecked")
            @Override
            public void sendAccessibilityEvent(View host, int eventType) {
                super.sendAccessibilityEvent(host, eventType);
                Map<String,String> params = (Map<String, String>) host.getTag(R.string.view_tag);
                //是否拦截 从配置中获取
                //事件类型：独立延时、实时，路径延时、实时
                //曝光类型：
                Log.i(TAG, "sendAccessibilityEvent: " + bindEvent.toString());
            }
        });
    }
}
