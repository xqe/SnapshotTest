package com.example.data.tracker.mylibrary.AOP;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.data.tracker.mylibrary.configCenter.PathElement;
import com.example.data.tracker.mylibrary.test.floatSelect.ViewIDMaker;

import java.util.List;

public class ViewFinder {

    private static final String TAG = "ViewFinder";

    /**
     * find the targetView which matches the given viewID
     */
    public static View findTarget(View givenRootView, String viewID) {
        if (givenRootView == null || TextUtils.isEmpty(viewID)) {
            return null;
        }
        String[] singleViewIDs = viewID.split(ViewIDMaker.CONNECTOR);

        return null;
    }

    public static View findTargetView(View givenRootView, List<PathElement> pathElementList) {
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
