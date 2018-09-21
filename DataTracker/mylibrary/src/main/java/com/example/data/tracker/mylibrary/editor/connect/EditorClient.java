package com.example.data.tracker.mylibrary.editor.connect;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

class EditorClient extends WebSocketClient {

    private boolean isClosed = false;

    EditorClient(URI serverURI,int socketTimeout) {
        super(serverURI,new Draft_17(),null,socketTimeout);
    }

    @Override
    public void onOpen(ServerHandshake handShakeData) {
        isClosed = false;
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        isClosed = true;
    }

    @Override
    public void onError(Exception ex) {
        isClosed = true;

    }

    public boolean isClosed(){
        return isClosed;
    }
}
