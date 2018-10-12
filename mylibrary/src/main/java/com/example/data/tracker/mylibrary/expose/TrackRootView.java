package com.example.data.tracker.mylibrary.expose;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

public class TrackRootView extends FrameLayout {

    private static final String TAG = "TrackRootView!";
    private GestureDetector gestureDetector;
    private static final int CLICK_LIMIT = 30;
    private boolean isSlide = false;
    private boolean isFling = false;
    private float touchX;
    private float touchY;
    private VelocityTracker velocityTracker;
    private ViewConfiguration configuration;
    private FlingUtils flingUtils;
    private FlingHandler flingHandler;
    private static final int MSG_ENDING_FLING = 100;

    public TrackRootView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        velocityTracker = VelocityTracker.obtain();
        configuration = ViewConfiguration.get(getContext());
        flingUtils = new FlingUtils(getContext());
        flingHandler = new FlingHandler();
        gestureDetector = new GestureDetector(getContext(),new OnGestureListenerAdapter(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                LogUtil.i(TAG,"onFling");
                isFling = true;
                int durationX = flingUtils.getSplineFlingDuration((int) velocityTracker.getXVelocity());
                int durationY = flingUtils.getSplineFlingDuration((int) velocityTracker.getYVelocity());
                if (durationX > 0) {
                    LogUtil.i(TAG,"onFlingX:" + durationX);
                    flingHandler.sendFlingMessage(TrackRootView.this,durationX);
                }
                if (durationY > 0) {
                    LogUtil.i(TAG,"onFlingY:" + durationY);
                    flingHandler.sendFlingMessage(TrackRootView.this,durationY);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
                LogUtil.i(TAG, "onShowPress: ");
                if (isFling) {
                    LogUtil.i(TAG, "onShowPress: stopFling");
                    flingHandler.removeMessages(MSG_ENDING_FLING);
                    ExposeManager.getInstance().checkExpose(ExposeConstants.TYPE_VIEW_EXPOSE,TrackRootView.this);
                }
                isFling = false;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogUtil.i(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSlide = false;
                touchX = ev.getX();
                touchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(ev);
                velocityTracker.computeCurrentVelocity(1000, configuration.getScaledMaximumFlingVelocity());
                if (Math.abs(ev.getX() - touchX) > CLICK_LIMIT || Math.abs(ev.getY() - touchY) > CLICK_LIMIT) {
                    //slide
                    LogUtil.i(TAG, "dispatchTouchEvent: slide");
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isSlide) {
                    isSlide = false;
                    LogUtil.i(TAG, "dispatchTouchEvent: slide mode for checking expose");
                    flingHandler.sendFlingMessage(this,100);
                }
                break;
                default:
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        LogUtil.i(TAG, "dispatchWindowFocusChanged: " + hasFocus);
        super.dispatchWindowFocusChanged(hasFocus);
        ExposeManager.getInstance().checkExpose(ExposeConstants.TYPE_WINDOW_EXPOSE,this);
    }

    @Override
    protected void dispatchVisibilityChanged(View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
        if (visibility == GONE) {
            LogUtil.i(TAG, "dispatchVisibilityChanged: ");
            ExposeManager.getInstance().checkExpose(ExposeConstants.TYPE_WINDOW_EXPOSE,this);
        }
    }

    private static class FlingHandler extends Handler {

        public void sendFlingMessage(TrackRootView trackRootView,long duration) {
            removeMessages(MSG_ENDING_FLING);
            Message message = obtainMessage(MSG_ENDING_FLING);
            message.obj = trackRootView;
            sendMessageDelayed(message,duration);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ENDING_FLING:
                    LogUtil.i(TAG, "handleMessage: endFling");
                    TrackRootView trackRootView = (TrackRootView) msg.obj;
                    trackRootView.isFling = false;
                    ExposeManager.getInstance().checkExpose(ExposeConstants.TYPE_VIEW_EXPOSE,trackRootView);
                    break;
            }
        }
    }
}
