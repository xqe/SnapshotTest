package com.vivo.expose.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by bruce on 2017/8/8.
 *
 * 配合VThread，做到runnable运行完成之后，自动移除无用handler的引用
 */

class AutoFinishHandler extends Handler {
    private static final String TAG = "AutoFinishHandler";
    /**
     * 调试开关
     */
    private static final boolean DEBUG = true;
    /**
     * 10秒内没有新事务发送，则移除此handler的强引用
     */
    private static final int AUTO_REMOVE_TIME = 10 * 1000;
    /**
     * 特殊msg，用于移除handler的强引用
     */
    private static final int MSG_FINAL = -27;
    /**
     * 标识此handler的事务名称
     */
    private final String mType;
    /**
     * 标识此handler已被移除，一旦此标志位被置为true，则此handler不会再尝试自我删除
     */
    private boolean mIsRemoved = false;
    /**
     * 标识handler中承载的runnable的最后期限，用于处理postDelay等场景
     */
    private long mUptimeMills = -1;

    AutoFinishHandler(String type, Looper looper) {
        super(looper);
        mType = type;
        dump("create handler");
    }

    @Override
    public void dispatchMessage(Message msg) {
        dump("dispatchMessage msg:" + msg.what);
        super.dispatchMessage(msg);
        // 此时runnable主体已运行完毕，可以尝试进行handler的移除
        autoRemoveSelf();
    }

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        dump("sendMessageAtTime msg:" + msg.what);
        // 当新入栈的msg的目的时间比此handler的最后期限还要晚时，更新最后期限
        if (msg.what != MSG_FINAL && mUptimeMills < uptimeMillis) {
            mUptimeMills = uptimeMillis;
            // 最后期限更新时，移除自动删除的消息，等待delay的消息到达之后再删除
            removeMessages(MSG_FINAL);
        }
        return super.sendMessageAtTime(msg, uptimeMillis);
    }

    @Override
    public void handleMessage(Message msg) {
        dump("handleMessage msg:" + msg.what);
        if (msg.what == MSG_FINAL) {
            HideVThread.getInstance().removeType(mType);
            removeMessages(MSG_FINAL);
            mIsRemoved = true;
        } else {
            super.handleMessage(msg);
        }
    }

    /**
     * 移除自身的前提：所有事物都已处理完毕，包括postDelay的内容
     */
    private void autoRemoveSelf() {
        if (mIsRemoved) {
            return;
        }
        long timeDur = SystemClock.uptimeMillis() - mUptimeMills;
        dump("autoRemoveSelf " + timeDur + "ms");
        if (timeDur >= 0) {
            removeMessages(MSG_FINAL);
            sendEmptyMessageDelayed(MSG_FINAL, AUTO_REMOVE_TIME);
        }
    }

    private void dump(String s) {
        if (DEBUG) {
            Log.d(TAG, mType + " " + s);
        }
    }
}
