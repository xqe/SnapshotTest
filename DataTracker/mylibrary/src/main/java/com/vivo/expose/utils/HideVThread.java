package com.vivo.expose.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bruce on 2017/7/26.
 *
 * 统一对整个商店的线程进行管理，最终状态，所有在AS中能查到的thread都必须是vthread_开头
 */


public class HideVThread {


    private static final String TAG = "HideVThread";

    public static final int MSG_CHECK_NUM = 1;
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    private static final int KEEP_ALIVE_SECONDS = 10;

    private static HideVThread sInstance;

    /*
     * 不做上限的线程池，最大可同时运行Integer.MAX_VALUE个线程，执行完runnable之后，
     * 超过10秒即被回收。
     */
    private final Executor mExcuter;
    /**
     * 有序的handler池，执行完最后一个runnable之后，超过10秒即被回收。
     */
    private final ConcurrentHashMap<String, AutoFinishHandler> mWorkHandlers;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "appstore_task #" + mCount.getAndIncrement());
        }
    };

    private HideVThread() {
        mWorkHandlers = new ConcurrentHashMap<String, AutoFinishHandler>();

        BlockingQueue<Runnable> sPoolWorkQueue = new SynchronousQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        mExcuter = threadPoolExecutor;
    }

    @NonNull
    private synchronized Handler ensureHandler(String type) {
        Handler handlerExist = mWorkHandlers.get(type);
        if (handlerExist == null) {
            HideVlog.d(TAG, "create handler of " + type);
            HandlerThread thread = new HandlerThread(type);
            thread.start();
            AutoFinishHandler handler = new AutoFinishHandler(type, thread.getLooper());
            mWorkHandlers.put(type, handler);
            return handler;
        }
        return handlerExist;
    }

    public static synchronized HideVThread getInstance() {
        if (sInstance == null) {
            sInstance = new HideVThread();
        }
        return sInstance;
    }

    public void run(Runnable runnable, String type) {
        ensureHandler(type).post(runnable);
    }

    public void run(Runnable runnable, String type, long delayMillis) {
        ensureHandler(type).postDelayed(runnable, delayMillis);
    }

    public void runAlone(Runnable runnable, String type, int msgId) {
        Handler checkHandler = ensureHandler(type);
        checkHandler.removeMessages(msgId);
        Message message = Message.obtain(checkHandler, runnable);
        message.what = msgId;
        message.sendToTarget();
    }

    /**
     * 通用线程池来运行runnable，包含多个线程，非单线程排队执行。
     */
    public void runOnNormal(Runnable runnable) {
        mExcuter.execute(runnable);
    }

    synchronized void removeType(String type) {
        HideVlog.d(TAG, "auto remove handler type " + type);
        Handler target = mWorkHandlers.remove(type);
        if (target != null && target.getLooper() != null) {
            target.getLooper().quit();
        }
    }
}
