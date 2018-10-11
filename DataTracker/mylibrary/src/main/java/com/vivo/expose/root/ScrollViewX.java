/**
 * Copyright (c) 2013-2014, Rinc Liu (http://rincliu.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vivo.expose.root;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.vivo.expose.utils.HideVlog;


class ScrollViewX extends ScrollView {
    private static final String TAG = "ScrollViewX";
    private static final long DELAY = 100;

    private int mCurrentScroll;

    private Runnable mScrollCheckTask;

    private OnScrollListener mOnScrollListener;

    /**
     * @param context
     */
    public ScrollViewX(Context context) {
        super(context);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public ScrollViewX(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ScrollViewX(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mScrollCheckTask = new Runnable() {
            @Override
            public void run() {
                int newScroll = getScrollY();
                if (mCurrentScroll == newScroll) {
                    if (mOnScrollListener != null) {
                        mOnScrollListener.onScrollStopped();
                    }
                } else {
                    if (mOnScrollListener != null) {
                        mOnScrollListener.onScrolling();
                    }
                    mCurrentScroll = getScrollY();
                    postDelayed(mScrollCheckTask, DELAY);
                }
            }
        };
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideVlog.d(TAG, "onTouch:" + event.getAction());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mCurrentScroll = getScrollY();
                    postDelayed(mScrollCheckTask, DELAY);
                }
                return false;
            }
        });
    }

    public interface OnScrollListener {
        void onScrollChanged(int x, int y, int oldX, int oldY);

        void onScrollStopped();

        void onScrolling();
    }

    /**
     * @param mOnScrollListener
     */
    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(x, y, oldX, oldY);
        }
    }

    /**
     * @param child
     * @return
     */
    public boolean isChildVisible(View child) {
        if (child == null) {
            return false;
        }
        Rect scrollBounds = new Rect();
        getHitRect(scrollBounds);
        return child.getLocalVisibleRect(scrollBounds);
    }

    /**
     * @return
     */
    public boolean isAtTop() {
        return getScrollY() <= 0;
    }

    /**
     * @return
     */
    public boolean isAtBottom() {
        return getChildAt(getChildCount() - 1).getBottom() + getPaddingBottom() == getHeight() + getScrollY();
    }
}