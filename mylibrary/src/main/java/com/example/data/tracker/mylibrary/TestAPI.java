package com.example.data.tracker.mylibrary;

import android.content.Context;
import android.util.Log;

import com.example.data.tracker.mylibrary.interaction.WebEditor;
import com.example.data.tracker.mylibrary.interaction.connect.ServerTest;
import com.example.data.tracker.mylibrary.test.websocket.server.WebServer;

import java.net.URI;

public class TestAPI {

    private static TestAPI api;

    private TestAPI() {
    }

    public static TestAPI getInstance() {
        if (api == null) {
            api = new TestAPI();
        }
        return api;
    }

    public void startEdit(final Context context) {

        new Thread(){
            @Override
            public void run() {
                ServerTest serverTest = new ServerTest();
                serverTest.start();
                URI uri = URI.create("ws://localhost:8080/");
                new WebEditor(context,uri).start();
            }
        }.start();
    }
}
