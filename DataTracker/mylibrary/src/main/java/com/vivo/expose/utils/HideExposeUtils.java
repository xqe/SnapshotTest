package com.vivo.expose.utils;

import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.ScrollView;

import com.vivo.expose.PromptlyReporterCenter;
import com.vivo.expose.model.ExposeItemInterface;
import com.vivo.expose.model.OnceExposeReporter;
import com.vivo.expose.model.PromptlyOption;
import com.vivo.expose.model.PromptlyOptionInterface;
import com.vivo.expose.model.ReportType;
import com.vivo.expose.root.ExposeRootViewInterface;
import com.vivo.expose.root.RootViewOption;
import com.vivo.expose.root.RootViewOptionInterface;
import com.vivo.expose.view.ExposableViewInterface;

import java.util.List;

/**
 * Created by 11070542 on 2017/11/28.
 * 曝光相关工具类
 */

public class HideExposeUtils {
    private static final String TAG = "HideExposeUtils";

    @NonNull
    private static Rect createRectFromRootView(View rootView, @Nullable RootViewOptionInterface option) {
        Rect rect = new Rect();
        if (option == null) {
            option = new RootViewOption();
        }
        rect.left = option.getExposeMarginLeft();
        rect.right = rootView.getWidth() - option.getExposeMarginRight();
        rect.top = option.getExposeMarginTop();
        rect.bottom = rootView.getHeight() - option.getExposeMarginBottom();
        return rect;
    }

    /**
     * 尝试性曝光最外部ListView当前显示的ItemView
     * <p>
     * 1.滚动停止时调用
     * <p>
     * 2.onStart时调用
     * <p>
     * 3.数据载入时调用
     *
     * @param rootViewInterface 尝试开始曝光的ListView
     */
    public static void attemptToExposeStart(ExposeRootViewInterface rootViewInterface) {
        if (rootViewInterface == null) {
            return;
        }
        if (rootViewInterface instanceof ListView) {
            ListView exposeListView = (ListView) rootViewInterface;
            HideVlog.d(TAG, "attemptToExposeStartRootListView");
            attemptToExposeStart(exposeListView, 0, 0, createRectFromRootView(exposeListView, rootViewInterface.getRootViewOption()));
        } else if (rootViewInterface instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) rootViewInterface;
            HideVlog.d(TAG, "attemptToExposeStartExposeScrollView");
            attemptToExposeStart(scrollView, 0, -scrollView.getScrollY(), createRectFromRootView(scrollView, rootViewInterface.getRootViewOption()));
        } else if (rootViewInterface instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) rootViewInterface;
            HideVlog.d(TAG, "attemptToExposeStartRootRecyclerView");
            attemptToExposeStart(recyclerView, 0, 0, createRectFromRootView(recyclerView, rootViewInterface.getRootViewOption()));
        }

    }

    /**
     * 尝试性曝光ListView中的一个ViewGroup
     *
     * @param subViewInListView 尝试开始曝光的曝光区
     */
    public static void attemptToExposeStartSubView(View subViewInListView) {
        boolean isExposeRootView = subViewInListView instanceof ExposeRootViewInterface;
        HideVlog.d(TAG, "attemptToExposeStartView|" + subViewInListView.getClass().getSimpleName() + "|" + isExposeRootView);
        if (isExposeRootView) {
            attemptToExposeStart((ExposeRootViewInterface) subViewInListView);
            return;
        }

        MySizeHolder holder = getRealRecInRootView(subViewInListView);
        if (holder == null) {
            attemptToExposeEnd(subViewInListView);
            return;
        }
        attemptToExposeStart(subViewInListView, holder.leftInListView, holder.topInListView, holder.container);
    }

    private static void attemptToExposeStart(View view, int left, int top, Rect container) {
        OnceExposeReporter onceExposeReporter = new OnceExposeReporter();
        attemptToExposeStart(view, left, top, container, onceExposeReporter);
        onceExposeReporter.tryToReportAll();
    }

    private static void attemptToExposeStart(View view, int left, int top, Rect container, OnceExposeReporter onceExposeReporter) {
        if (view == null || container == null) {
            return;
        }

        HideVlog.d(TAG, "attemptToExposeStart:" + view.getVisibility() + "|" + view.getClass().getSimpleName() + "|left:" + left + "|top:" + top + "|cLeft" + container.left + "|cRight" + container.right + "|cTop:" + container.top + "|cBottom:" + container.bottom);

        if (view.getVisibility() != View.VISIBLE) {
            return;
        }

        if (view instanceof ExposableViewInterface) {

            ExposableViewInterface exposableView = (ExposableViewInterface) view;

            PromptlyOptionInterface promptlyOption = exposableView.getPromptlyOption();
            if (promptlyOption == null) {
                promptlyOption = PromptlyReporterCenter.PROMPTLY_OPTION_DEFAULT;
            }

            int percentVisible = getValidVisiblePercentToDoExpose(promptlyOption);

            int comparedLeft = left + (PromptlyOption.PERCENT_FULL - percentVisible) * view.getWidth() / PromptlyOption.PERCENT_FULL;
            int comparedRight = left + percentVisible * view.getWidth() / PromptlyOption.PERCENT_FULL;
            int comparedTop = top + (PromptlyOption.PERCENT_FULL - percentVisible) * view.getHeight() / PromptlyOption.PERCENT_FULL;
            int comparedBottom = top + percentVisible * view.getHeight() / PromptlyOption.PERCENT_FULL;

            boolean hasBindItem;
            if (!container.isEmpty() && comparedLeft >= container.left && comparedRight <= container.right && comparedTop >= container.top && comparedBottom <= container.bottom) {
                hasBindItem = execExposableExposeStartOrEnd(exposableView, true, onceExposeReporter);
            } else {
                hasBindItem = execExposableExposeStartOrEnd(exposableView, false, onceExposeReporter);
            }

            if (hasBindItem && !exposableView.canDeepExpose()) {
                //一般情况都是不能继续递归的，会走到这里面，return出去
                return;
            }
        }

        if (!(view instanceof ViewGroup)) {
            return;
        }

        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child == null) {
                continue;
            }
            if (child.getWidth() == 0 || child.getHeight() == 0) {
                continue;
            }
            if (child instanceof ViewGroup || child instanceof ExposableViewInterface) {
                attemptToExposeStart(child, left + child.getLeft(), top + child.getTop(), container, onceExposeReporter);
            }
        }
    }

    public static void attemptToExposeStartAfterLayout(final View subViewInListView) {
        int childCount = (subViewInListView instanceof ViewGroup) ? ((ViewGroup) subViewInListView).getChildCount() : 0;
        HideVlog.d(TAG, "attemptToExposeStartAfterLayoutAttempt|" + subViewInListView.hashCode() + "|" + childCount);
        callAfterLayout(subViewInListView, new Runnable() {
            @Override
            public void run() {
                int childCount = (subViewInListView instanceof ViewGroup) ? ((ViewGroup) subViewInListView).getChildCount() : 0;
                boolean isExposeRootView = subViewInListView instanceof ExposeRootViewInterface;
                HideVlog.d(TAG, "attemptToExposeStartAfterLayout|" + subViewInListView.hashCode() + "|" + childCount + "|" + isExposeRootView);
                if (isExposeRootView) {
                    HideExposeUtils.attemptToExposeStart((ExposeRootViewInterface) subViewInListView);
                } else {
                    HideExposeUtils.attemptToExposeStartSubView(subViewInListView);
                }
            }
        });
    }

    public static void callAfterLayout(final View view, final Runnable runnable) {
        if (view == null || runnable == null) {
            return;
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                runnable.run();
            }
        });
    }

    /**
     * 获取露出百分之多少视为显示，非法值则返回50
     *
     * @param promptlyOption 曝光选项
     * @return 百分之多少算曝光
     */
    private static int getValidVisiblePercentToDoExpose(PromptlyOptionInterface promptlyOption) {
        int percent = promptlyOption.getVisiblePercentToDoExpose();
        if (percent <= 0 || percent > PromptlyOption.PERCENT_FULL) {
            return PromptlyOption.PERCENT_HALF;
        }
        return percent;
    }


    /**
     * 执行expose
     *
     * @param exposableView 要曝光的View
     * @param startOrEnd    exposeStart还是exposeEnd
     * @return exposableView是否有绑定item
     */
    private static boolean execExposableExposeStartOrEnd(ExposableViewInterface exposableView, boolean startOrEnd, OnceExposeReporter onceExposeReport) {
        ExposeItemInterface[] items = exposableView.getItemsToDoExpose();
        ReportType type = exposableView.getReportType();
        PromptlyOptionInterface promptlyOption = exposableView.getPromptlyOption();
        if (items != null && items.length > 0 && type != null) {
            for (ExposeItemInterface item : items) {
                if (startOrEnd) {
                    HidePromptlyReporterUtils.exposeStart(type, item, onceExposeReport);
                } else {
                    //对于有定制PromptlyOption为禁止结束曝光的，不结束曝光
                    if (promptlyOption == null || !promptlyOption.isDisableExposeEnd()) {
                        HidePromptlyReporterUtils.exposeEnd(type, item);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 尝试曝光结束
     * 1.View被recycle时调用
     * 2.onStop时调用
     *
     * @param view 尝试结束曝光的View
     */
    public static void attemptToExposeEnd(View view) {
        forceExposeStartOrEnd(view, false, null);
    }

    /**
     * 强制曝光一个ViewGroup
     *
     * @param view 强制开始曝光的View
     */
    public static void attemptToExposeStartForce(View view) {
        OnceExposeReporter onceExposeReporter = new OnceExposeReporter();
        forceExposeStartOrEnd(view, true, onceExposeReporter);
        onceExposeReporter.tryToReportAll();
    }

    private static void forceExposeStartOrEnd(View view, boolean startOrEnd, OnceExposeReporter onceExposeReporter) {
        if (view == null) {
            return;
        }
        if (view instanceof ExposableViewInterface) {
            ExposableViewInterface exposableViewInterface = (ExposableViewInterface) view;
            boolean hasBindItem = execExposableExposeStartOrEnd(exposableViewInterface, startOrEnd, onceExposeReporter);
            if (hasBindItem && !exposableViewInterface.canDeepExpose()) {
                //一般情况都是不能继续递归的，会走到这里面，return出去
                return;
            }
        }

        if (!(view instanceof ViewGroup)) {
            return;
        }

        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child == null) {
                continue;
            }

            if (child instanceof ViewGroup || child instanceof ExposableViewInterface) {
                forceExposeStartOrEnd(child, startOrEnd, onceExposeReporter);
            }
        }
    }

    public static void setItemsCanExposeStart(View view, boolean canExposeStart) {
        if (view == null) {
            return;
        }
        if (view instanceof ExposableViewInterface) {
            ExposableViewInterface exposableViewInterface = (ExposableViewInterface) view;

            ExposeItemInterface[] items = exposableViewInterface.getItemsToDoExpose();
            if (items != null && items.length > 0) {
                for (ExposeItemInterface item : items) {
                    item.getExposeAppData().setExpCanExposeStart(canExposeStart);
                }
            }
        }

        if (!(view instanceof ViewGroup)) {
            return;
        }

        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child == null) {
                continue;
            }

            if (child instanceof ViewGroup || child instanceof ExposableViewInterface) {
                setItemsCanExposeStart(child, canExposeStart);
            }
        }
    }


    /**
     * 获取View在ExposeRootViewInterface中的真实位置
     *
     * @param view 哪个View
     * @return 如果不在ListView中返回null
     */
    @Nullable
    private static MySizeHolder getRealRecInRootView(View view) {
        MySizeHolder rect = new MySizeHolder();

        View parent = view;
        int left = 0;
        int top = 0;
        Rect rectContainer = null;
        while (parent != null) {
            left += parent.getLeft();
            top += parent.getTop();

            ViewParent viewParent = parent.getParent();
            if (viewParent == null || !(viewParent instanceof View)) {
                break;
            }

            parent = (View) viewParent;
            if (parent instanceof ExposeRootViewInterface) {
                ExposeRootViewInterface rootViewInterface = (ExposeRootViewInterface) parent;
                if (!rootViewInterface.isEnable()) {
                    //不是Enable则不能开始曝光
                    return null;
                }
                if (parent instanceof ScrollView) {
                    top -= parent.getScrollY();
                }
                rectContainer = createRectFromRootView(parent, rootViewInterface.getRootViewOption());
                break;
            }

        }

        if (rectContainer == null || rectContainer.isEmpty()) {
            return null;
        }

        rect.leftInListView = left;
        rect.topInListView = top;
        rect.currentHeight = view.getHeight();
        rect.currentWidth = view.getWidth();
        rect.container = rectContainer;

        HideVlog.d(TAG, "rect:" + rect.toString());

        if (rect.currentHeight == 0 || rect.currentWidth == 0) {
            return null;
        }

        return rect;
    }

    private static class MySizeHolder {
        /**
         * 相对于ListView的真实Left
         */
        int leftInListView;
        /**
         * 相对于ListView的真实Top
         */
        int topInListView;
        /**
         * 该View的宽度
         */
        int currentWidth;
        /**
         * 该View的高度
         */
        int currentHeight;

        /**
         * 滚动容器
         */
        Rect container;

        @Override
        public String toString() {
            return "leftInListView:" + leftInListView + ",topInListView:" + topInListView + ",currentWidth:" + currentWidth + ",currentHeight:" + currentHeight + ",widthListView" + container.right + ",heightListView" + container.bottom;
        }
    }
}
