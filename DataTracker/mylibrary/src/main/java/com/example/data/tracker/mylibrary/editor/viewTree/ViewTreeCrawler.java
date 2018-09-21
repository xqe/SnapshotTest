package com.example.data.tracker.mylibrary.editor.viewTree;

import android.app.Activity;
import android.os.Build;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ViewTreeCrawler {


    private ExecutorService executorService;

    public ViewTreeCrawler() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void writeViewTreeInfo(View rootView, JsonWriter jsonWriter) throws IOException {
        writeViewTree(jsonWriter,rootView);
    }

    private void writeViewTree(JsonWriter j, View view) throws IOException {
        if (view.getVisibility() == View.INVISIBLE) {
            return;
        }

        final int viewId = view.getId();
        final String viewIdName;
        if (-1 == viewId) {
            viewIdName = null;
        } else {
            //待优化：不必每次getResources 加载资源
            viewIdName = view.getResources().getResourceName(viewId);
        }

        j.beginObject();
        j.name("hashCode").value(view.hashCode());
        j.name("id").value(viewId);
        j.name("mp_id_name").value(viewIdName);

        final CharSequence description = view.getContentDescription();
        if (null == description) {
            j.name("contentDescription").nullValue();
        } else {
            j.name("contentDescription").value(description.toString());
        }

        final Object tag = view.getTag();
        if (null == tag) {
            j.name("tag").nullValue();
        } else if (tag instanceof CharSequence) {
            j.name("tag").value(tag.toString());
        }

        j.name("top").value(view.getTop());
        j.name("left").value(view.getLeft());
        j.name("width").value(view.getWidth());
        j.name("height").value(view.getHeight());
        j.name("scrollX").value(view.getScrollX());
        j.name("scrollY").value(view.getScrollY());
        j.name("visibility").value(view.getVisibility());

        float translationX = 0;
        float translationY = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            translationX = view.getTranslationX();
            translationY = view.getTranslationY();
        }

        j.name("translationX").value(translationX);
        j.name("translationY").value(translationY);

        //classes: [class1,class2,class3]
        j.name("classes");
        j.beginArray();
        Class<?> klass = view.getClass();
        do {
            j.value(klass.getSimpleName());
            klass = klass.getSuperclass();
        } while (klass != Object.class && klass != null);
        j.endArray();

        //addProperties(j, view);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams relativeLayoutParams = (RelativeLayout.LayoutParams) layoutParams;
            int[] rules = relativeLayoutParams.getRules();
            j.name("layoutRules");
            j.beginArray();
            for (int rule : rules) {
                j.value(rule);
            }
            j.endArray();
        }

        j.name("subviews");
        j.beginArray();
        if (view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = group.getChildAt(i);
                // child can be null when views are getting disposed.
                if (null != child) {
                    j.value(child.hashCode());
                }
            }
        }
        j.endArray();
        j.endObject();

        if (view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = group.getChildAt(i);
                // child can be null when views are getting disposed.
                if (null != child) {
                    writeViewTree(j, child);
                }
            }
        }
    }

    public ViewTreeInfo getViewTreeInfo(Activity activity) {
        ViewTreeCall viewTreeCall = new ViewTreeCall(activity);
        FutureTask<ViewTreeInfo> futureTask = new FutureTask<>(viewTreeCall);
        executorService.submit(futureTask);
        ViewTreeInfo viewTreeInfo = null;
        try {
            viewTreeInfo = futureTask.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return viewTreeInfo;
    }
}
