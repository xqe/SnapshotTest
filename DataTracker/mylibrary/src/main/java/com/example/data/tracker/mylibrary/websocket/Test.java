package com.example.data.tracker.mylibrary.websocket;

import android.util.Log;

import com.example.data.tracker.mylibrary.websocket.client.WebClient;
import com.example.data.tracker.mylibrary.websocket.server.WebServer;

public class Test {

    public void testWebSocket() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                final WebServer webServer = new WebServer();
                Log.e("--", "run: ");
                webServer.start();

                WebClient webClient = new WebClient(webServer.getHostName(),webServer.getPort());
                webClient.sendMessage();
            }
        }.start();
    }
}
