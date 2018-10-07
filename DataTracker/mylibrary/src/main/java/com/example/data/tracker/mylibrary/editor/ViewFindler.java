package com.example.data.tracker.mylibrary.editor;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.data.tracker.mylibrary.dataBean.PathElement;
import com.example.data.tracker.mylibrary.viewCrawler.floatSelect.ViewIDMaker;

import java.util.List;

public class ViewFindler {

    private static final String TAG = "ViewFindler";

    public static View findTargetView(View givenRootView, List<PathElement> pathElementList) {
        if (pathElementList.isEmpty()) {
            Log.i(TAG, "findTargetView: pathList is null");
            return null;
        }
        return findTargetViewInMatched(givenRootView,pathElementList,0);
    }

    private static View findTargetViewInMatched(View matchedView,List<PathElement> pathElementList,int position){
        if (matchViewID(matchedView,pathElementList,position) && matchedView instanceof ViewGroup) {
            position++;
            final ViewGroup group = (ViewGroup) matchedView;
            final int childCount = group.getChildCount();
            View targetView = null;
            for (int i = 0; i < childCount; i++) {
                View childView = group.getChildAt(i);
                if (matchViewID(group.getChildAt(i),pathElementList,position)) {
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
                    findTargetViewInMatched(targetView,pathElementList,position);
                }
            }
        }
        return null;
    }

    private static boolean matchViewID(View givenView, List<PathElement> pathElementList,int position){
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
