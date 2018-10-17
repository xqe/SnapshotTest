package com.example.data.tracker.mylibrary.test;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.example.data.tracker.mylibrary.interaction.ScreenInfoCrawler;
import com.example.data.tracker.mylibrary.interaction.connect.EditorConnection;
import com.example.data.tracker.mylibrary.interaction.connect.IWebMessage;

import org.json.JSONObject;

import java.net.URI;
import java.util.Timer;

/**
 * get snapshot form UI for every one seconds
 * get ViewTreeInfo from UI
 * */
public class DataTransmit implements IWebMessage {
    private static final String TAG = "DataTransmit";
    private Timer timer;
    private TimerTask timerTask;
    private ScreenInfoCrawler screenInfoCrawler;
    private EditorConnection editorConnection;

    private DataTransmit() {
        timer = new Timer();
        timerTask = new TimerTask();
        screenInfoCrawler = new ScreenInfoCrawler();
    }

    public static DataTransmit getInstance() {
        return Holder.INSTANCE;
    }


    private static class Holder{
        private static final DataTransmit INSTANCE = new DataTransmit();
    }

    public void connect(URI uri) {
        editorConnection = new EditorConnection(uri,20,this);
        editorConnection.startEdit();
    }

    public void startTransmit(Context context) {
        //editState.startListen(context);
    }

    private void startTimer(Activity activity){
        timer.cancel();
        timerTask.cancel();
        timerTask.setActivity(activity);
        timer.schedule(timerTask,0,1000);
    }


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

    private class TimerTask extends java.util.TimerTask {

        private Activity activity;

        private void setActivity(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            Log.e(TAG, "get snapshot and viewTreeInfo every 1s");
            //screenInfoCrawler.writeScreenInfo(editorConnection.getBufferedOutputStream(),activity);
        }
    }

}
