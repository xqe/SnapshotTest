package com.vivo.expose.root;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.vivo.expose.model.ReportType;
import com.vivo.expose.utils.HideExposeUtils;
import com.vivo.expose.utils.HidePromptlyReporterUtils;
import com.vivo.expose.utils.HideVlog;
import com.vivo.expose.view.ExposableLinearLayout;
import com.vivo.expose.view.ExposableRelativeLayout;

/**
 * Created by 11070542 on 2018/2/9.
 * 监听实时曝光的RecyclerView
 * 曝光流程
 * 1.在要曝光的页面可见时调recyclerView.onExposeResume();在不可见时调recyclerView.onExposePause()
 * 2.使用支持曝光的组件，如:
 * {@link ExposableRelativeLayout}
 * {@link ExposableLinearLayout}
 */

public class ExposeRecyclerView extends RecyclerView implements ExposeRootViewInterface {

    private static final String TAG = "ExposeRecyclerView";

    private boolean mIsExposeEnable = false;

    @Nullable
    private RootViewOptionInterface mOption;

    private OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            HideVlog.d(TAG, "onScrollStateChanged|" + scrollState + "|" + mIsExposeEnable + "|" + ExposeRecyclerView.this.hashCode());
            if (mIsExposeEnable) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    HideExposeUtils.attemptToExposeStart(ExposeRecyclerView.this);
                }
            }
        }

    };


    public ExposeRecyclerView(Context context) {
        super(context);
        init();
    }

    public ExposeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExposeRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.addOnScrollListener(mOnScrollListener);
        super.addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (mIsExposeEnable) {
                    HideVlog.d(TAG, "onChildViewAttachedToWindow|" + view.getClass().getSimpleName() + "|" + view.hashCode());
                    HideExposeUtils.attemptToExposeStartAfterLayout(view);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (mIsExposeEnable) {
                    HideVlog.d(TAG, "onChildViewDetachedFromWindow|" + view.getClass().getSimpleName() + "|" + view.hashCode());
                    HideExposeUtils.attemptToExposeEnd(view);
                }
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        super.setOnScrollListener(listener);
    }

    @Override
    public void removeOnScrollListener(OnScrollListener listener) {
        super.removeOnScrollListener(listener);
        if (listener == null) {
            super.addOnScrollListener(mOnScrollListener);
        }
    }

    @Override
    public void clearOnScrollListeners() {
        super.clearOnScrollListeners();
        super.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void setVisibility(int visibility) {
        boolean lastVisible = super.getVisibility() == VISIBLE;
        boolean currentVisible = visibility == VISIBLE;
        super.setVisibility(visibility);
        if (lastVisible != currentVisible && mIsExposeEnable) {
            HideVlog.d(TAG, "setVisibility|" + currentVisible + "|" + ExposeRecyclerView.this.hashCode() + "|" + getChildCount());
            if (currentVisible) {
                HideExposeUtils.attemptToExposeStart(ExposeRecyclerView.this);
            } else {
                HideExposeUtils.attemptToExposeEnd(ExposeRecyclerView.this);
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
        HideVlog.d(TAG, "onExposeResume|" + ExposeRecyclerView.this.hashCode() + "|" + getChildCount() + "|");
        HideExposeUtils.attemptToExposeStart(ExposeRecyclerView.this);
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


}
