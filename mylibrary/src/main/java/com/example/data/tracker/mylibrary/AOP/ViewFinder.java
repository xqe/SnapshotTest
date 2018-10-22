package com.example.data.tracker.mylibrary.AOP;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.data.tracker.mylibrary.configCenter.BindEvent;
import com.example.data.tracker.mylibrary.configCenter.ConfigManager;
import com.example.data.tracker.mylibrary.configCenter.PathElement;
import com.example.data.tracker.mylibrary.test.floatSelect.ViewIDMaker;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

public class ViewFinder {

    private static final String TAG = "ViewFinder";
    private Set<BindEvent> currentConfigEvents;
    private WeakReference<View> currentRootView;

    private ViewFinder() {
    }

    private static class Holder {
        private static final ViewFinder INSTANCE = new ViewFinder();
    }

    public static ViewFinder getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 正式运行时,发生点击或曝光时候，直接查看缓存中是否存在该ViewID
     */
    public BindEvent isViewConfiged(View view) {
        String viewId = ViewIDMaker.getViewID(view);
        return containsInCurrentConfig(viewId);
    }

    public void cacheCurrentConfig(Activity activity) {
        String activityName = activity.getClass().getCanonicalName();
        currentConfigEvents = ConfigManager.getInstance().getVisualConfig(activity, activityName);
        View rootView = activity.getWindow().getDecorView().getRootView();
        currentRootView = new WeakReference<>(rootView);
    }

    /**
     * set delegate for all the config View
     * */
    public void setDataDelegate() {
        for (BindEvent bindEvent : currentConfigEvents) {
            View target = findTargetView(bindEvent.getPath());
            ClickDelegate.setClickDelegate(target, bindEvent);
        }
    }

    private BindEvent containsInCurrentConfig(String viewID) {
        for (BindEvent bindEvent : currentConfigEvents) {
            if (viewID.equals(bindEvent.createViewID())) {
                return bindEvent;
            }
        }
        return null;
    }

    /**
     * find the targetView from current rootView
     */
    public View findTargetView(List<PathElement> pathElementList) {
        return findTargetView(currentRootView.get(), pathElementList);
    }

    /**
     * find the targetView from given rootView
     */
    public View findTargetView(View givenRootView, List<PathElement> pathElementList) {
        if (givenRootView == null) {
            return null;
        }
        if (pathElementList.isEmpty()) {
            Log.i(TAG, "findTargetView: pathList is null");
            return null;
        }
        return findTargetViewInMatched(givenRootView, pathElementList, 0);
    }

    private static View findTargetViewInMatched(View matchedView, List<PathElement> pathElementList, int position) {
        if (matchViewID(matchedView, pathElementList, position) && matchedView instanceof ViewGroup) {
            position++;
            final ViewGroup group = (ViewGroup) matchedView;
            final int childCount = group.getChildCount();
            View targetView = null;
            for (int i = 0; i < childCount; i++) {
                View childView = group.getChildAt(i);
                if (matchViewID(group.getChildAt(i), pathElementList, position)) {
                    targetView = childView;
                }
                if (targetView != null) {
                    break;
                }
            }

            if (targetView != null) {
                if (position == pathElementList.size()) {
                    return targetView;
                } else {
                    findTargetViewInMatched(targetView, pathElementList, position);
                }
            }
        }
        return null;
    }

    private static boolean matchViewID(View givenView, List<PathElement> pathElementList, int position) {
        String givenViewID = ViewIDMaker.getViewID(givenView);
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < position + 1; i++) {
            PathElement pathElement = pathElementList.get(i);
            idBuilder.append(pathElement.parseToViewID());
            if (i < position) {
                idBuilder.append(ViewIDMaker.CONNECTOR);
            }
        }
        String targetViewID = idBuilder.toString();
        Log.i(TAG, "matchViewID givenViewID:" + givenViewID);
        Log.i(TAG, "matchViewID targetViewID:" + targetViewID);
        return givenViewID.equals(targetViewID);
    }
}
