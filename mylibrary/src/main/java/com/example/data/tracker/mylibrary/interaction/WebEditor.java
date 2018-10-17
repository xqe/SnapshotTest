package com.example.data.tracker.mylibrary.interaction;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.data.tracker.mylibrary.AOP.expose.TrackRootView;
import com.example.data.tracker.mylibrary.interaction.connect.EditorConnection;
import com.example.data.tracker.mylibrary.interaction.connect.IWebMessage;
import com.example.data.tracker.mylibrary.interaction.connect.ShakeSensor;

import org.json.JSONObject;

import java.net.URI;

public class WebEditor implements ShakeSensor.ShakeListener {

    private static final String TAG = "WebEditor";
    private URI uri;
    private static final int SOCKET_TIMEOUT = 100;
    private ShakeSensor shakeSensor;
    private EditorConnection editorConnection;
    private ScreenInfoCrawler screenInfoCrawler;
    private MyLifecycleCallback myLifecycleCallback;
    private GlobalLayoutListener globalLayoutListener;
    private IWebMessage webMessage;
    private View currentRootView;
    private Context context;

    public WebEditor(Context context,URI uri) {
        this.uri = uri;
        this.context = context;
        screenInfoCrawler = new ScreenInfoCrawler();
        globalLayoutListener = new GlobalLayoutListener();
        myLifecycleCallback = new MyLifecycleCallback();
        webMessage = new WebMessageImpl();
        shakeSensor = new ShakeSensor(this);
    }

    //执行脚本，发广播启动Sensor or 发广播开启部署模式
    public void start(){
        shakeSensor.cancelListen();
        //start connect
        editorConnection = new EditorConnection(uri,SOCKET_TIMEOUT,webMessage);
        ((Application)context).registerActivityLifecycleCallbacks(myLifecycleCallback);
    }

    @Override
    public void onShake() {

    }

    private class WebMessageImpl implements IWebMessage {

        @Override
        public void sendSnapshot(JSONObject message) {

        }

        @Override
        public void performEdit(JSONObject message) {

        }

        @Override
        public void clearEdits(JSONObject message) {

        }

        @Override
        public void bindEvents(JSONObject message) {

        }

        @Override
        public void setTweaks(JSONObject message) {

        }

        @Override
        public void sendDeviceInfo() {

        }

        @Override
        public void cleanup() {

        }
    }

    //ViewTree change
    private class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            Log.i(TAG, "onGlobalLayout: ");
            screenInfoCrawler.writeScreenInfo(editorConnection.getBufferedOutputStream(),currentRootView);
        }
    }

    //Activity Lifecycle
    private class MyLifecycleCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.i(TAG, "onActivityResumed: " + activity.getClass().getSimpleName());
            currentRootView = activity.getWindow().getDecorView().getRootView();
            //currentRootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
            screenInfoCrawler.writeScreenInfo(editorConnection.getBufferedOutputStream(),currentRootView);
            TrackRootView trackRootView = findTrackRootView(activity);
            if (trackRootView != null) {
                trackRootView.setLayoutChangeListener(new TrackRootView.LayoutChangeListener() {
                    @Override
                    public void onLayoutChange() {
                        Log.i(TAG, "onLayoutChange: ");
                        screenInfoCrawler.writeScreenInfo(editorConnection.getBufferedOutputStream(),currentRootView);
                    }
                });
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.i(TAG, "onActivityStopped: " + activity.getClass().getSimpleName());
            //currentRootView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            TrackRootView trackRootView = findTrackRootView(activity);
            if (trackRootView != null) {
                trackRootView.removeLayoutChangeListener();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        private TrackRootView findTrackRootView(Activity activity) {
            ViewGroup viewGroup = activity.findViewById(android.R.id.content);
            if (viewGroup.getChildAt(0) instanceof TrackRootView) {
                return (TrackRootView) viewGroup.getChildAt(0);
            }
            return null;
        }
    }
}
