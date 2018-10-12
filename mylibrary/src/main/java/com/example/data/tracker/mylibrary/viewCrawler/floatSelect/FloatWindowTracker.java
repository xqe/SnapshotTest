package com.example.data.tracker.mylibrary.viewCrawler.floatSelect;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.data.tracker.mylibrary.R;

public class FloatWindowTracker {

    private WindowManager windowManager;
    private int screenW;
    private int screenH;
    private FocusView focusView;
    private WindowManager.LayoutParams layoutParams;
    private static final String TAG = "FloatWindowTracker";

    private FloatWindowTracker() {
    }

    public static FloatWindowTracker getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final FloatWindowTracker INSTANCE = new FloatWindowTracker();
    }

    public void bind(Activity activity) {
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;
        addCircleView(activity);
    }

    private void addCircleView(final Activity activity){
        focusView = new FocusView(activity);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        //添加View所占宽高
        layoutParams.width = focusView.getViewWidth();
        layoutParams.height = focusView.getViewWidth();
        //添加到屏幕的坐标
        layoutParams.x = screenW / 2 - focusView.getViewWidth() / 2;
        layoutParams.y = screenH / 2 - focusView.getViewWidth() / 2;
        focusView.setLocationX(screenW / 2);
        focusView.setLocationY(screenH / 2 + focusView.getViewWidth() / 2);
        windowManager.addView(focusView,layoutParams);

        focusView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.i(TAG, "onTouch: " + event.getRawX() + "," + event.getRawY());
                    dragFocusView(event.getRawX() - focusView.getWidth(),event.getRawY() - focusView.getHeight());
                }
                return false;
            }
        });

        focusView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e(TAG, "onLongClick: " + focusView.getLocationX() + "," + focusView.getLocationY());
                View targetView = ViewFinder.crawlClickView(activity,focusView.getLocationX(),focusView.getLocationY());
                String viewID = ViewIDMaker.getViewID(targetView);
                Object data = targetView.getTag(R.string.view_tag);
                Log.e(TAG, "dispatchTouchEvent: viewID = " + viewID);
                if (data != null) {

                    Log.e(TAG, "onLongClick: data:" + data.toString());
                }
                //new EditorDialog().show(activity.getFragmentManager(),"EditorDialog");
                return true;
            }
        });
    }

    private void dragFocusView(float x,float y) {
        layoutParams.alpha = 0.5f;
        layoutParams.x = (int) x;
        layoutParams.y = (int) y;
        //以浮点中心为location
        focusView.setLocationX(layoutParams.x + focusView.getWidth() / 2);
        focusView.setLocationY(layoutParams.y + focusView.getHeight());
        windowManager.updateViewLayout(focusView,layoutParams);
    }

    /**释放Context引用*/
    public void unBind(){
        windowManager.removeViewImmediate(focusView);
        windowManager = null;
        layoutParams = null;
        focusView = null;

    }
}
