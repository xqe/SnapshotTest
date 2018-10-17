package com.example.data.tracker.mylibrary.interaction.snapshot;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SnapshotCrawler {
    private static final String TAG = "SnapshotCrawler";
    private ExecutorService executorService;

    public SnapshotCrawler() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void writeSnapshotData(View rootView,OutputStream outputStream) throws IOException{
        SnapshotCall snapshotCall = new SnapshotCall(rootView);
        FutureTask<Bitmap> futureTask = new FutureTask<>(snapshotCall);
        executorService.submit(futureTask);
        Bitmap snapshot = null;
        try {
            snapshot = futureTask.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        if (snapshot != null) {
            outputStream.write('"');
            final Base64OutputStream imageOut = new Base64OutputStream(outputStream, Base64.NO_WRAP);
            snapshot.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
            imageOut.flush();
            outputStream.write('"');
        } else {
            outputStream.write("null".getBytes());
        }
    }
}
