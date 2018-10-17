package com.example.data.tracker.mylibrary;

import android.content.Context;
import android.util.Log;

import com.example.data.tracker.mylibrary.interaction.WebEditor;
import com.example.data.tracker.mylibrary.test.websocket.server.WebServer;

import java.net.URI;
import java.net.URISyntaxException;

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
                WebServer webServer = new WebServer();
                webServer.start();
                String hostName = webServer.getHostName();
                int port = webServer.getPort();
                String uriStr = "ws://" + hostName + ":" + port + "/";
                Log.i("API", "run: " + uriStr);
                try {
                    URI uri = new URI(uriStr);
                    new WebEditor(context,uri).start();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
