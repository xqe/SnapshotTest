package com.example.data.tracker.mylibrary.viewCrawler.viewTree;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewID生成器，确定抓取控件的唯一ViewID
 * */
public class ViewIDMaker {

    private static final String TAG = "ViewIDMaker";
    private static final String ID_SEPARATOR = "/";
    private static final String CONNECTOR = "-";
    private static final String RESOURCE_TAG = "#";


    public static String getViewID(View view) {
        StringBuilder sb = new StringBuilder();
        while (view != null && hasParent(view)) {
            String className = view.getClass().getSimpleName();
            sb.append(className);
            int index;
            String resourceId = getResourceId(view);
            int realIndex = getViewIndex(view);
            index = TextUtils.isEmpty(resourceId) ? realIndex : realIndex;
            sb.append("[");
            sb.append(index);
            sb.append("]");
            if (!TextUtils.isEmpty(resourceId)) {
                sb.append(RESOURCE_TAG);
                sb.append(resourceId);
            }
            sb.append(CONNECTOR);
            view = (View) view.getParent();
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    private static String getResourceId(View view) {
        Resources resources = view.getResources();
        int id = view.getId();
        String result = "";
        if (id != -1) {
            String resourceId = resources.getResourceName(id);
            result = resourceId;
            if (resourceId.contains(ID_SEPARATOR)) {
                int index = resourceId.indexOf(ID_SEPARATOR);
                result = resourceId.substring(index + 1,resourceId.length());
            }
        }
        return result;
    }

    private static int getViewIndex(View view) {
        int index;
        Log.e(TAG, "getViewIndex: " + view.getId());
        List<View> currentTypeViewList = new ArrayList<>();
        for (int i = 0; i < ((ViewGroup)view.getParent()).getChildCount(); i++) {
            String targetClassName = view.getClass().getSimpleName();
            String currentClassName = ((ViewGroup)view.getParent()).getChildAt(i).getClass().getSimpleName();
            if (targetClassName.equals(currentClassName)) {
                currentTypeViewList.add(((ViewGroup)view.getParent()).getChildAt(i));
            }
        }
        index = currentTypeViewList.indexOf(view);
        return index;
    }

    private static boolean hasParent(View view) {
        ViewParent viewParent = view.getParent();
        return viewParent instanceof ViewGroup;
    }
}
