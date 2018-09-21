package com.example.data.tracker.mylibrary.viewCrawler.floatSelect;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ViewFinder {
    private static final String TAG = "ViewFinder";

    public static View crawlClickView(Activity activity,int rawX,int rawY) {
        View rootView = activity.getWindow().getDecorView();
        return getClickView(rootView,rawX,rawY);
    }

    private static View getClickView(View view,int rawX,int rawY) {
        View targetView = null;
        if (!isInView(view,rawX,rawY)){
            Log.e(TAG, "outside view:" + view.getClass().getSimpleName() );
            return null;
        }
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            /*if (view instanceof RecyclerView) {
                return view;
            }*/
            for (int i = 0; i < childCount; i++) {
                View childView = ((ViewGroup) view).getChildAt(i);
                targetView = getClickView(childView,rawX,rawY);
                if (targetView != null) {
                    break;
                }
            }
            if (targetView == null) {
                targetView = view;
            }
        } else {
            targetView = view;
        }
        return targetView;
    }

    private static boolean isInView(View view, int rawX,int rawY) {
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        int viewW = view.getWidth();
        int viewH = view.getHeight();
        boolean isInX = rawX > viewX && rawX < viewX + viewW;
        boolean isInY = rawY > viewY && rawY < viewY + viewH;
        Log.e(TAG, "isInView x:" + rawX + "," + "x range" + ":" + viewX + "~" + (viewX + viewW));
        Log.e(TAG, "isInView y:" + rawY + "," + "x range" + ":" + viewY + "~" + (viewX + viewY + viewH));
        Log.e(TAG, "isInView:" + view.getClass().getSimpleName() + "--" + (isInX && isInY));
        return isInX && isInY;
    }
}
