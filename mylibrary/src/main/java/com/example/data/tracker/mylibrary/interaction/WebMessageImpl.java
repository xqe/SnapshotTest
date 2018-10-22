package com.example.data.tracker.mylibrary.interaction;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.example.data.tracker.mylibrary.AOP.ClickDelegate;
import com.example.data.tracker.mylibrary.AOP.ViewFinder;
import com.example.data.tracker.mylibrary.configCenter.BindEvent;
import com.example.data.tracker.mylibrary.configCenter.ConfigManager;
import com.example.data.tracker.mylibrary.interaction.connect.IWebMessage;

import org.json.JSONObject;

public class WebMessageImpl implements IWebMessage {

    private static final int MSG_BIND_EVENT = 101;
    private static final int MSG_SEND_SNAPSHOT = 102;

    private WorkHandler workHandler;

    public WebMessageImpl() {
        HandlerThread handlerThread = new HandlerThread("");
        handlerThread.start();
        workHandler = new WorkHandler(handlerThread.getLooper());
    }

    @Override
    public void sendSnapshot(JSONObject message) {
        workHandler.sendMessage(MSG_SEND_SNAPSHOT,message);
    }

    @Override
    public void performEdit(JSONObject message) {

    }

    @Override
    public void clearEdits(JSONObject message) {

    }

    @Override
    public void bindEvents(JSONObject message) {
        workHandler.sendMessage(MSG_BIND_EVENT,message);
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

    private class WorkHandler extends Handler {

        private WorkHandler(Looper looper) {
            super(looper);
        }

        private void sendMessage(int what,JSONObject jsonObject){
            Message message = obtainMessage();
            message.what = what;
            message.obj = jsonObject;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_BIND_EVENT:
                    BindEvent bindEvent = ConfigManager.getInstance().parseSingleConfigMessage((JSONObject) msg.obj);
                    if (!bindEvent.getPath().isEmpty()) {
                        View targetView = ViewFinder.getInstance().findTargetView(bindEvent.getPath());
                        if (targetView != null) {
                            ClickDelegate.setClickDelegate(targetView,bindEvent);
                        }
                    }
                    break;
                    default:
            }
        }
    }


}