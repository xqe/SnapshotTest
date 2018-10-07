package com.example.data.tracker.mylibrary.websocket.client;

import android.util.Log;

import com.example.data.tracker.mylibrary.controler.DataTransmit;
import com.example.data.tracker.mylibrary.editor.snapshot.SnapshotInfo;
import com.example.data.tracker.mylibrary.editor.viewTree.ViewTreeInfo;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

public class WebClient {

    private static final String TAG = "WebClient";
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
    private String hostName;
    private int port;

    public WebClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public void sendMessage(){
        Request request = new Request.Builder().url("ws://" + hostName + ":" + port + "/").build();
        WebSocketCall.create(client,request).enqueue(new WebSocketListener() {
            WebSocket webSocket = null;
            @Override
            public void onOpen(final WebSocket webSocket, Response response) {
                Log.e(TAG, "onOpen: ");
                this.webSocket = webSocket;
                writeExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        startTransmit(webSocket);
                    }
                });
            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.e(TAG, "onFailure: ");
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                Log.e(TAG, "onMessage: ====");
                String response = message.string();
                Log.e(TAG, "onMessage: " + response);
                if ("reply command 1".equals(response)) {
                    webSocket.sendMessage(RequestBody.create(WebSocket.TEXT,"command 2"));
                }
            }

            @Override
            public void onPong(Buffer payload) {
                Log.e(TAG, "onPong: " + payload.toString());
            }

            @Override
            public void onClose(int code, String reason) {
                Log.e(TAG, "onClose: " + code + "," + reason) ;
            }
        });
    }

    private void startTransmit(final WebSocket webSocket) {
        //startSnapShot


    }

}
