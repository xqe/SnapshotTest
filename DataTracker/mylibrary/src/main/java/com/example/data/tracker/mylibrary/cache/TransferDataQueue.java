package com.example.data.tracker.mylibrary.cache;

import com.example.data.tracker.mylibrary.protocol.Package;

import java.util.concurrent.LinkedBlockingDeque;

/**数据传送队列*/
public class TransferDataQueue {

    private static final int MAX_CAPACITY = 10;

    private LinkedBlockingDeque<Package> dataQueue;

    public static TransferDataQueue getInstance(){
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final TransferDataQueue INSTANCE = new TransferDataQueue();
    }

    private TransferDataQueue() {
        dataQueue = new LinkedBlockingDeque<>(MAX_CAPACITY);
    }

    public void put(Package data) {
        try {
            dataQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Package take() {
        try {
            return dataQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
