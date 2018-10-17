package com.example.data.tracker.mylibrary.test.floatSelect;

import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewID生成器，确定抓取控件的唯一ViewID
 * */
public class ViewIDMaker {

    private static final String TAG = "ViewIDMaker";
    private static final String ID_SEPARATOR = "/";
    public static final String CONNECTOR = "-";
    public static final String RESOURCE_TAG = "#";


    public static String getViewID(View view) {
        Log.e(TAG, "getViewID: " + view.getClass().getSimpleName() );
        StringBuilder resultBuilder = new StringBuilder();
        while (view != null && hasParent(view)) {

            String className = view.getClass().getName();
            StringBuilder sb = new StringBuilder();
            sb.append(className);
            int index = 0;
            String idName = getIdName(view);
            if (TextUtils.isEmpty(idName)) {
                index = getViewIndex(view);;
            }
            sb.append("[")
                    .append(index)
                    .append("]");
            if (!TextUtils.isEmpty(idName)) {
                sb.append(RESOURCE_TAG)
                        .append(idName);
            }
            sb.insert(0,CONNECTOR);
            resultBuilder.insert(0,sb.toString());
            view = (View) view.getParent();
        }
        if (resultBuilder.length() > 1) {
            resultBuilder.deleteCharAt(0);
        }
        Log.e(TAG, "getViewID: " + resultBuilder.toString() );
        return resultBuilder.toString();
    }


    private static String getIdName(View view) {
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
        Log.e(TAG, "getViewIndex: "  + view.getClass().getSimpleName());
        if (view.getParent() instanceof ListView) {
            Log.e(TAG, "getViewIndex: listView" );
            ListView listView = (ListView) view.getParent();
            return listView.getPositionForView(view);
        }

        if (view.getParent() instanceof RecyclerView) {
            Log.e(TAG, "getViewIndex: RecyclerView" );
            RecyclerView recyclerView = (RecyclerView) view.getParent();
            return recyclerView.getChildAdapterPosition(view);
        }

        if (view.getParent() instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) view.getParent();
            return viewPager.getCurrentItem();
        }

        int index;
        //Log.e(TAG, "getViewIndex: " + view.getId());
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
