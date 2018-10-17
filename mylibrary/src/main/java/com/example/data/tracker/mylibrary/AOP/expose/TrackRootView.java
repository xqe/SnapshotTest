package com.example.data.tracker.mylibrary.AOP.expose;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class TrackRootView extends FrameLayout {

    private static final String TAG = "TrackRootView!";
    private GestureDetector gestureDetector;
    private static final int CLICK_LIMIT = 20;
    private boolean isSlide = false;
    private boolean isFling = false;
    private float touchX;
    private float touchY;
    private VelocityTracker velocityTracker;
    private ViewConfiguration configuration;
    private FlingUtils flingUtils;
    private FlingHandler flingHandler;
    private static final int MSG_ENDING_FLING = 100;
    private static final int MSG_ENDING_SLIDE = 101;
    private static int num = 0;
    private LayoutChangeListener layoutChangeListener;

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
                int durationX = flingUtils.getSplineFlingDuration((int)velocityX);
                int durationY = flingUtils.getSplineFlingDuration((int) velocityY);
                int realDuration = durationX >= durationY ? durationX : durationY;
                LogUtil.i(TAG,"send Fling message and realDuration= " + realDuration);
                flingHandler.sendFlingMessage(TrackRootView.this,realDuration);
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
                LogUtil.i(TAG, "onShowPress: ");
                if (isFling) {
                    LogUtil.i(TAG, "onShowPress: stopFling");
                    flingHandler.sendFlingMessage(TrackRootView.this,0);
                }
                isFling = false;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogUtil.i(TAG, "onLayout: ");
        if (layoutChangeListener != null) {
            layoutChangeListener.onLayoutChange();
        }
        flingHandler.sendFlingMessage(TrackRootView.this,100);
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
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isSlide) {
                    isSlide = false;
                    if (!isFling) {
                        LogUtil.i(TAG, "---check expose for sliding situation");
                        ExposeManager.getInstance().checkExpose(ExposeConstants.TYPE_VIEW_EXPOSE,this);
                    }
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

    public void setLayoutChangeListener(LayoutChangeListener layoutChangeListener) {
        this.layoutChangeListener = layoutChangeListener;
    }

    public void removeLayoutChangeListener() {
        this.layoutChangeListener = null;
    }

    private static class FlingHandler extends Handler {

        private void sendFlingMessage(TrackRootView trackRootView,long duration) {
            removeMessages(MSG_ENDING_FLING);
            Message message = obtainMessage(MSG_ENDING_FLING);
            message.arg1 = num;
            num ++;
            message.obj = trackRootView;
            sendMessageDelayed(message,duration);
            LogUtil.i(TAG, "sendFlingMessage: " + message.arg1 + ",duration:" + duration);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ENDING_FLING:
                case MSG_ENDING_SLIDE:
                    LogUtil.i(TAG, "handleMessage: endFling:" + msg.arg1);
                    TrackRootView trackRootView = (TrackRootView) msg.obj;
                    trackRootView.isFling = false;
                    ExposeManager.getInstance().checkExpose(ExposeConstants.TYPE_VIEW_EXPOSE,trackRootView);
                    break;
            }
        }
    }

    public interface LayoutChangeListener{
        void onLayoutChange();
    }
}
