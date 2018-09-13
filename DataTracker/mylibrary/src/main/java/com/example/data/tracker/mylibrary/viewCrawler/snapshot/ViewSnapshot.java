package com.example.data.tracker.mylibrary.viewCrawler.snapshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.data.tracker.mylibrary.cache.TransferDataQueue;
import com.example.data.tracker.mylibrary.protocol.SnapshotPackage;
import com.example.data.tracker.mylibrary.utils.BitmapUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ViewSnapshot {
    private static final String TAG = "ViewSnapshot";
    private ExecutorService executorService;

    public ViewSnapshot() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getSnapshot(Activity activity) {
        RootViewFinder rootViewFinder = new RootViewFinder(activity);
        FutureTask<RootViewInfo> futureTask = new FutureTask<>(rootViewFinder);
        executorService.submit(futureTask);
        //List<RootViewInfo> rootViewInfoList = Collections.emptyList();
        try {
            RootViewInfo rootViewInfo = futureTask.get(1, TimeUnit.SECONDS);
            if (rootViewInfo == null) {
                Log.e(TAG, "getSnapshot: rootViewInfo null" );
                return;
            }
            Bitmap snapShoat = rootViewInfo.getSnapshot();
            SnapshotPackage snapshotPackage = new SnapshotPackage();
            snapshotPackage.addData(BitmapUtils.Bitmap2Bytes(snapShoat));
            TransferDataQueue.getInstance().put(snapshotPackage);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
