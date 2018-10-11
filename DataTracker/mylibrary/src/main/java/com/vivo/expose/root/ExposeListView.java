package com.vivo.expose.root;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.vivo.expose.model.ReportType;
import com.vivo.expose.utils.HideExposeUtils;
import com.vivo.expose.utils.HidePromptlyReporterUtils;
import com.vivo.expose.utils.HideVlog;
import com.vivo.expose.view.ExposableLinearLayout;
import com.vivo.expose.view.ExposableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11070542 on 2017/12/9.
 * 监听实时曝光的ListView
 * 曝光流程
 * 1.在要曝光的页面可见时调listView.onExposeResume();在不可见时调listView.onExposePause()
 * 2.使用支持曝光的组件，如:
 * {@link ExposableRelativeLayout}
 * {@link ExposableLinearLayout}
 */

public class ExposeListView extends ListView implements ExposeRootViewInterface {

    private static final String TAG = "ExposeListView";

    private boolean mIsExposeEnable = false;
    private boolean mIsDataHasChanged = false;

    @Nullable
    private RootViewOptionInterface mOption;

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mInternalScrollListener != null) {
                mInternalScrollListener.onScrollStateChanged(view, scrollState);
            }


            if (mIsExposeEnable) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    HideExposeUtils.attemptToExposeStart(ExposeListView.this);
                    HideVlog.d(TAG, "HeaderView size=" + mHeaderViews.size() + ",FooterView size=" + mFooterViews.size());
                    if (mHeaderViews.size() > 0) {
                        for (View v : mHeaderViews) {
                            if (v != null && v instanceof ViewGroup) {
                                HideExposeUtils.attemptToExposeStartSubView(v);
                            }
                        }
                    }
                    if (mFooterViews.size() > 0) {
                        for (View v : mFooterViews) {
                            if (v != null && v instanceof ViewGroup) {
                                HideExposeUtils.attemptToExposeStartSubView(v);
                            }
                        }
                    }
                }
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (mInternalScrollListener != null) {
                mInternalScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

        }
    };

    RecyclerListener mRecyclerListener = new RecyclerListener() {
        @Override
        public void onMovedToScrapHeap(View view) {

            if (mInternalRecyclerListener != null) {
                mInternalRecyclerListener.onMovedToScrapHeap(view);
            }

            if (mIsExposeEnable) {
                if (view != null && view instanceof ViewGroup) {
                    HideExposeUtils.attemptToExposeEnd((ViewGroup) view);
                }
            }

        }
    };


    private OnScrollListener mInternalScrollListener;
    private RecyclerListener mInternalRecyclerListener;

    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();

    public ExposeListView(Context context) {
        super(context);
        init();
    }

    public ExposeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExposeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.setOnScrollListener(mOnScrollListener);
        super.setRecyclerListener(mRecyclerListener);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mInternalScrollListener = l;
    }


    @Override
    public void setRecyclerListener(RecyclerListener listener) {
        mInternalRecyclerListener = listener;
    }


    @Override
    protected void handleDataChanged() {
        super.handleDataChanged();
        mIsDataHasChanged = true;
        HideVlog.d(TAG, "handleDataChangedAttempt|" + ExposeListView.this.hashCode() + "|" + getChildCount());
        if (mIsExposeEnable) {

            HideExposeUtils.callAfterLayout(this, new Runnable() {
                @Override
                public void run() {
                    HideVlog.d(TAG, "handleDataChanged|" + ExposeListView.this.hashCode() + "|" + ExposeListView.this.getChildCount());
                    HideExposeUtils.attemptToExposeStart(ExposeListView.this);
                }
            });

        }
    }

    @Override
    public void setVisibility(int visibility) {
        boolean lastVisible = super.getVisibility() == VISIBLE;
        boolean currentVisible = visibility == VISIBLE;
        super.setVisibility(visibility);
        if (lastVisible != currentVisible && mIsExposeEnable) {
            HideVlog.d(TAG, "setVisibility|" + currentVisible + "|" + ExposeListView.this.hashCode() + "|" + getChildCount());
            if (currentVisible) {
                HideExposeUtils.attemptToExposeStart(ExposeListView.this);
            } else {
                HideExposeUtils.attemptToExposeEnd(ExposeListView.this);
            }
        }
    }


    /**
     * ListView进入可见状态时调用，包括Activity的onResume与tab切换
     */
    @Override
    public void onExposeResume(@Nullable RootViewOptionInterface option) {
        mOption = option;
        mIsExposeEnable = true;
        HideVlog.d(TAG, "onExposeResume|" + ExposeListView.this.hashCode() + "|" + getChildCount() + "|" + mIsDataHasChanged);
        if (mIsDataHasChanged) {
            HideExposeUtils.attemptToExposeStart(this);
        }
    }

    @Override
    public void onExposeResume() {
        onExposeResume(null);
    }

    /**
     * ListView离开可见状态时调用，包括Activity的onPause与tab切换
     */
    @Override
    public void onExposePause(ReportType... reportType) {
        HideExposeUtils.attemptToExposeEnd(this);
        if (reportType != null && reportType.length > 0) {
            for (ReportType type : reportType) {
                HidePromptlyReporterUtils.reportItem(null, type, true);
            }
        }
        //onPause了就别曝光了
        mIsExposeEnable = false;
    }

    @Nullable
    @Override
    public RootViewOptionInterface getRootViewOption() {
        return mOption;
    }

    @Override
    public boolean isEnable() {
        return mIsExposeEnable;
    }

    @Override
    public void addHeaderView(View v) {
        this.addHeaderView(v, null, true);
    }

    @Override
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        super.addHeaderView(v, data, isSelectable);
        if (!mHeaderViews.contains(v)) {
            mHeaderViews.add(v);
        }
    }

    @Override
    public boolean removeHeaderView(View v) {
        if (mHeaderViews.contains(v)) {
            mHeaderViews.remove(v);
        }
        return super.removeHeaderView(v);
    }

    @Override
    public boolean removeFooterView(View v) {
        if (mFooterViews.contains(v)) {
            mFooterViews.remove(v);
        }
        return super.removeFooterView(v);
    }

    @Override
    public void addFooterView(View v) {
        this.addFooterView(v, null, true);
    }

    @Override
    public void addFooterView(View v, Object data, boolean isSelectable) {
        super.addFooterView(v, data, isSelectable);
        if (!mFooterViews.contains(v)) {
            mFooterViews.add(v);
        }
    }


}
