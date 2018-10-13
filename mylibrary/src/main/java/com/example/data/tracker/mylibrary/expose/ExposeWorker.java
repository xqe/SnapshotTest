package com.example.data.tracker.mylibrary.expose;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ExposeWorker {

    private static final String TAG = "ExposeWorker";

    private ExposeHandler exposeHandler;
    public static final int MSG_COMMIT_EXPOSE = 101;

    ExposeWorker() {
        HandlerThread handlerThread = new HandlerThread("");
        handlerThread.start();
        exposeHandler = new ExposeHandler(handlerThread.getLooper());
    }

    public void sendMessage(Message message) {
        exposeHandler.sendMessage(message);
    }

    private static class ExposeHandler extends Handler {

        private ExposeHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COMMIT_EXPOSE:
                    ExposeMessageObj obj = (ExposeMessageObj) msg.obj;
                    if (obj.getExposeType() == ExposeConstants.TYPE_VIEW_EXPOSE) {
                        for (String viewId : obj.getLastExposeList().keySet()) {
                            //find the invisible target and commit
                            if (!obj.getCurrentExposeList().containsKey(viewId)) {
                                ExposeModel exposeModel = obj.getLastExposeList().get(viewId);
                                doCommit(viewId,exposeModel);
                            }
                        }
                    } else if (obj.getExposeType() == ExposeConstants.TYPE_WINDOW_EXPOSE) {
                        for (String viewId : obj.getLastExposeList().keySet()) {
                            ExposeModel exposeModel = obj.getLastExposeList().get(viewId);
                            doCommit(viewId,exposeModel);
                        }
                    } else {
                        LogUtil.e(TAG, "handleMessage: wrong expose type");
                    }
                    break;
                    default:
            }
        }

        private void doCommit(String viewId,ExposeModel exposeModel) {
            exposeModel.setEndTime(System.currentTimeMillis());
            LogUtil.e(TAG, "doCommit ExposeList: " + viewId);
        }
    }

}
