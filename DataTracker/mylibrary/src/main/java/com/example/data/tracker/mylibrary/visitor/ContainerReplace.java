package com.example.data.tracker.mylibrary.visitor;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ContainerReplace {

    private static final String TAG = "ContainerLayout";

    public static void replaceContainer(Activity activity) {
        try{
            ViewGroup container = activity.findViewById(android.R.id.content);
            View root = container.getChildAt(0);
            if (root instanceof ContainerLayout) {
                Log.i(TAG, "replaceContainer: ");
            } else {
                ContainerLayout containerLayout = new ContainerLayout(activity);
                if (container.getChildCount() > 0) {
                    while (container.getChildAt(0) != null) {
                        View child = container.getChildAt(0);
                        container.removeViewAt(0);
                        containerLayout.addView(child,child.getLayoutParams());
                    }
                    container.addView(containerLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            }
        }catch (Exception ex) {
            Log.e(TAG, "replaceContainer failed: " + ex.toString() );
        }
    }
}
