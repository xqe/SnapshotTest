package com.example.data.tracker.mylibrary.interaction.connect;

import android.support.annotation.NonNull;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class EditorConnection {
    private EditorClient editorClient;
    private BufferedOutputStream bufferedOutputStream;

    public EditorConnection(URI uri,int socketTimeout,IWebMessage event){
        editorClient = new EditorClient(uri,socketTimeout,event);
        try {
            editorClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startEdit() {
    }

    public BufferedOutputStream getBufferedOutputStream() {
        if (bufferedOutputStream == null) {
            bufferedOutputStream = new BufferedOutputStream(new EditorOutputStream(editorClient));
        }
        return bufferedOutputStream;
    }

    public boolean isDisconnected(){
        return editorClient.isClosed();
    }


    private class EditorClient extends WebSocketClient {

        private static final String TAG = "EditorClient";

        private boolean isClosed = false;
        private IWebMessage event;

        EditorClient(URI serverURI,int socketTimeout,IWebMessage event) {
            super(serverURI,new Draft_17(),null,socketTimeout);
            this.event = event;
        }

        @Override
        public void onOpen(ServerHandshake handShakeData) {
            isClosed = false;
        }

        @Override
        public void onMessage(String message) {
            try {
                final JSONObject messageJson = new JSONObject(message);
                final String type = messageJson.getString("type");
                switch (type) {
                    case "device_info_request":
                        event.sendDeviceInfo();
                        break;
                    case "snapshot_request":
                        event.sendSnapshot(messageJson);
                        break;
                    case "change_request":
                        event.performEdit(messageJson);
                        break;
                    case "event_binding_request":
                        event.bindEvents(messageJson);
                        break;
                    case "clear_request":
                        event.clearEdits(messageJson);
                        break;
                    case "tweak_request":
                        event.setTweaks(messageJson);
                        break;
                }
            } catch (final JSONException e) {
                Log.i(TAG, "onMessage: " + e.toString());
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            isClosed = true;
        }

        @Override
        public void onError(Exception ex) {
            isClosed = true;

        }

        boolean isClosed(){
            return isClosed;
        }
    }

    private class EditorOutputStream extends OutputStream {

        private EditorClient editorClient;

        EditorOutputStream(EditorClient editorClient) {
            this.editorClient = editorClient;
        }

        @Override
        public void write(int b) throws IOException {
            byte[] bytes = new byte[1];
            bytes[0] = (byte) b;
            write(bytes,0,bytes.length);
        }

        @Override
        public void write(@NonNull byte[] b) throws IOException {
            write(b,0,b.length);
        }

        @Override
        public void write(@NonNull byte[] b, int off, int len) throws IOException {
            editorClient.send(b);
        }
    }
}
