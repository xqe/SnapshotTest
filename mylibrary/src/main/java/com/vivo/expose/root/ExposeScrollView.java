package com.vivo.expose.root;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vivo.expose.model.ReportType;
import com.vivo.expose.utils.HideExposeUtils;
import com.vivo.expose.utils.HidePromptlyReporterUtils;
import com.vivo.expose.utils.HideVlog;

/**
 * Created by 11070542 on 2017/12/19.
 * 支持自动实时曝光的scrollView
 */

public class ExposeScrollView extends ScrollViewX implements ExposeRootViewInterface {

    private static final String TAG = "ExposeScrollView";

    private boolean mIsExposeEnable = false;

    @Nullable
    private RootViewOptionInterface mOption;

    OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollChanged(int x, int y, int oldX, int oldY) {
            if (mInnerScrollListener != null) {
                mInnerScrollListener.onScrollChanged(x, y, oldX, oldY);
            }

        }

        @Override
        public void onScrollStopped() {
            if (mInnerScrollListener != null) {
                mInnerScrollListener.onScrollStopped();
            }

            HideVlog.d(TAG, "onScrollStopped");
            if (mIsExposeEnable) {
                HideExposeUtils.attemptToExposeStart(ExposeScrollView.this);
            }
        }

        @Override
        public void onScrolling() {

            if (mInnerScrollListener != null) {
                mInnerScrollListener.onScrolling();
            }

        }
    };

    private OnScrollListener mInnerScrollListener;

    public ExposeScrollView(Context context) {
        super(context);
        init();
    }

    public ExposeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExposeScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        mInnerScrollListener = mOnScrollListener;
    }

    private void init() {
        super.setOnScrollListener(mScrollListener);
    }

    @Override
    public void onExposeResume(@Nullable RootViewOptionInterface option) {
        HideVlog.d(TAG, "onExposeResume");
        mOption = option;
        mIsExposeEnable = true;
        HideExposeUtils.attemptToExposeStart(this);
    }

    @Override
    public void onExposeResume() {
        onExposeResume(null);
    }

    @Override
    public void onExposePause(ReportType... reportType) {
        HideVlog.d(TAG, "onExposePause");
        HideExposeUtils.attemptToExposeEnd(this);
        if (reportType != null && reportType.length > 0) {
            for (ReportType type : reportType) {
                HidePromptlyReporterUtils.reportItem(null, type, true);
            }
        }
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
