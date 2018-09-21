package com.example.data.tracker.mylibrary.controler;

import android.app.Activity;
import android.util.Log;


import com.example.data.tracker.mylibrary.editor.ViewCrawler;
import com.example.data.tracker.mylibrary.editor.connect.EditorConnection;

import java.net.URI;
import java.util.Timer;

/**
 * get snapshot form UI for every one seconds
 * get ViewTreeInfo from UI
 * */
public class DataTransmit {
    private static final String TAG = "DataTransmit";
    private Timer timer;
    private TimerTask timerTask;
    private ViewCrawler viewCrawler;
    private EditorConnection editorConnection;

    public DataTransmit() {
        timer = new Timer();
        timerTask = new TimerTask();
        viewCrawler = new ViewCrawler();
    }

    public static DataTransmit getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder{
        private static final DataTransmit INSTANCE = new DataTransmit();
    }

    public void init(URI uri) {
        editorConnection = new EditorConnection(uri,20);
    }

    public void startTransmit() {
        //aop 获取Activity 实例
        final Activity activity = null;

        if (activity == null) {
            return;
        }

        startTimer(activity);
    }

    private void startTimer(Activity activity){
        timer.cancel();
        timerTask.cancel();
        timerTask.setActivity(activity);
        timer.schedule(timerTask,0,1000);
    }

    private class TimerTask extends java.util.TimerTask {

        private Activity activity;

        private void setActivity(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            Log.e(TAG, "get snapshot and viewTreeInfo every 1s");
            viewCrawler.writeScreenInfo(editorConnection.getBufferedOutputStream(),activity);
        }
    }

}
