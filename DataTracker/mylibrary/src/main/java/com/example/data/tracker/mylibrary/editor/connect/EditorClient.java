package com.example.data.tracker.mylibrary.editor.connect;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

class EditorClient extends WebSocketClient {

    private static final String TAG = "EditorClient";

    private boolean isClosed = false;
    private IEvent event;

    EditorClient(URI serverURI,int socketTimeout,IEvent event) {
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

    public boolean isClosed(){
        return isClosed;
    }
}
