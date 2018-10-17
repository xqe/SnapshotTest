package com.example.data.tracker.datatracker;

import android.app.Application;

import com.example.data.tracker.mylibrary.AOP.expose.TrackerManager;
import com.example.data.tracker.mylibrary.TestAPI;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TrackerManager.getInstance().init(this);
        TestAPI.getInstance().startEdit(this);
    }
}
