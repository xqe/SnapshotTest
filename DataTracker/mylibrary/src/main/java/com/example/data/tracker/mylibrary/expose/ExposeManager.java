package com.example.data.tracker.mylibrary.expose;

import android.graphics.Rect;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.data.tracker.mylibrary.R;
import com.example.data.tracker.mylibrary.controler.DataConfig;
import com.example.data.tracker.mylibrary.viewCrawler.floatSelect.ViewIDMaker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExposeManager {

    private static final String TAG = "ExposeManager";

    private static ExposeManager instance;
    private Set<String> configViewList;
    private Map<String, ExposeModel> currentExposeList;
    private Map<String, ExposeModel> lastExposeList;
    private ExposeWorker exposeWorker;
    private ExposeConfig exposeConfig;
    private long lastExposeTime;

    public static ExposeManager getInstance() {
        if (instance == null) {
            instance = new ExposeManager();
        }
        return instance;
    }

    private ExposeManager() {
        configViewList = DataConfig.getInstance().getConfigViewMap().keySet();
        currentExposeList = new HashMap<>();
        lastExposeList = new HashMap<>();
        exposeWorker = new ExposeWorker();
        exposeConfig = new ExposeConfig();
    }

    /**
     * start checking expose for all view on the screen
     */
    public void checkExpose(int exposeType,View view) {
        currentExposeList.clear();
        long time = System.currentTimeMillis();
        LogUtil.i(TAG, "checkExpose: " + time + ",lastTime:" + lastExposeTime);
        if (time - lastExposeTime < exposeConfig.getMiniTime()) {
            LogUtil.i(TAG, "last expose time : " + (time - lastExposeTime) + "< " + exposeConfig.getMiniTime());
            return;
        }
        lastExposeTime = time;
        matchFromViewTree(view);
        commitExposeEvent(exposeType);
    }

    /**
     * match expose view down to the viewTree
     */
    private void matchFromViewTree(View view) {
        //LogUtil.i(TAG, "matchFromViewTree: " + view.getClass().getSimpleName());
        String viewId;
        //config中只保存ListView RecyclerView ViewPager的ViewID
        boolean isSpecialView = isListViewParent(view) || isReuseViewParent(view);
        if (isSpecialView) {
            viewId = ViewIDMaker.getViewID((View) view.getParent());
        } else {
            viewId = ViewIDMaker.getViewID(view);
        }
        if (configViewList.contains(viewId)) {
            if (isSpecialView) {
                viewId = ViewIDMaker.getViewID(view);
            }
            if (view instanceof RecyclerView || view instanceof ListView || view instanceof ViewPager) {
                LogUtil.i(TAG, "matchFromViewTree: do not expose listLayout or reuseLayout");
            } else {
                checkTargetExpose(viewId, view);
            }
        }

        //exclude specialView
        if (!isSpecialView && view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                matchFromViewTree(viewGroup.getChildAt(i));
            }
        }
    }

    /**
     * check if targetView has exposed and cache in the list
     */
    @SuppressWarnings("unchecked")
    private void checkTargetExpose(String viewId, View view) {
        //LogUtil.i(TAG, "checkTargetExpose: " + viewId);
        boolean hasFocus = view.hasWindowFocus();
        boolean exposed = checkExposureViewDimension(view);
        if (hasFocus && exposed) {
            if (lastExposeList.containsKey(viewId)) {
                currentExposeList.put(viewId,lastExposeList.get(viewId));
            } else {
                Map<String, String> params = (Map<String, String>) view.getTag(R.string.view_tag);
                ExposeModel exposeModel = new ExposeModel();
                exposeModel.setParams(params);
                exposeModel.setStartTime(System.currentTimeMillis());
                currentExposeList.put(viewId, exposeModel);
            }
        }
    }

    private void commitExposeEvent(int exposeType) {
        LogUtil.i(TAG, "commitExposeEvent: " + exposeType);
        ExposeMessageObj obj = new ExposeMessageObj();
        obj.setExposeType(exposeType);
        obj.getCurrentExposeList().putAll(currentExposeList);
        obj.getLastExposeList().putAll(lastExposeList);
        /*for (String key:obj.getCurrentExposeList().keySet()) {
            LogUtil.i(TAG, "msg currentExposeList: " + key);
        }

        for (String key:obj.getLastExposeList().keySet()) {
            LogUtil.i(TAG, "msg lastExposeList: " + key);
        }*/
        Message message = new Message();
        message.what = ExposeWorker.MSG_COMMIT_EXPOSE;
        message.obj = obj;
        exposeWorker.sendMessage(message);
        lastExposeList.clear();
        lastExposeList.putAll(currentExposeList);
    }

    private boolean checkExposureViewDimension(View view) {
        if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
            return false;
        }

        if (view.getParent() == null) {
            return false;
        }

        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Rect rect = new Rect();
        boolean isVisibleRect = view.getGlobalVisibleRect(rect);
        if (isVisibleRect) {
            int visibleWidth = rect.width();
            int visibleHeight = rect.height();
            return (visibleWidth * 1.00 / width > exposeConfig.getAreaRatio()) && (visibleHeight * 1.00 / height > exposeConfig.getAreaRatio());
        } else {
            return false;
        }
    }

    private boolean isListViewParent(View view) {
        View parent = (View) view.getParent();
        while (parent != null) {
            if (parent instanceof ListView || parent instanceof RecyclerView) {
                return true;
            }
            if (parent.getParent() instanceof View) {
                parent = (View) parent.getParent();
            } else {
                break;
            }
        }
        return false;
    }

    private boolean isReuseViewParent(View view) {
        return view.getParent() instanceof ViewPager;
    }
}
