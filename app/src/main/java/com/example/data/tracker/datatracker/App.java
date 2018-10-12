package com.example.data.tracker.datatracker;

import android.app.Application;

import com.example.data.tracker.mylibrary.expose.TrackerManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TrackerManager.getInstance().init(this);
    }
}
