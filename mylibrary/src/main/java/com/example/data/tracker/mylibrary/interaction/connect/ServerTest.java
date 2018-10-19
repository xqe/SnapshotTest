package com.example.data.tracker.mylibrary.interaction.connect;


import com.example.data.tracker.mylibrary.AOP.expose.LogUtil;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ServerTest {
    
    private static final String TAG = "ServerTest";
    
    public void start() {
        WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress("localhost",8080)) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                LogUtil.i(TAG, "onOpen: " + getAddress() + "," + getPort());
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                LogUtil.i(TAG, "onClose: ");
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                LogUtil.i(TAG, "onMessage: " + message);
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                LogUtil.i(TAG, "onError: ");
            }
        };
        webSocketServer.start();
    }
}
