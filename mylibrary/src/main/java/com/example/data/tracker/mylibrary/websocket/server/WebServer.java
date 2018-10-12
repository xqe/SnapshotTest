package com.example.data.tracker.mylibrary.websocket.server;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

public class WebServer {

    private static final String TAG = "WebServer";
    private final MockWebServer mockWebServer = new MockWebServer();
    private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor();

    public void start() {
        mockWebServer.enqueue(new MockResponse().withWebSocketUpgrade(new WebSocketListener() {
            WebSocket webSocket = null;
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                this.webSocket = webSocket;
                Log.e(TAG, "onOpen: ");
                Log.e(TAG, "onOpen: " + response.request().toString());
                Log.e(TAG, "onOpen: " + response.headers());
                Log.e(TAG, "onOpen: " + response);
            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.e(TAG, "onFailure: " + response);
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                String request = message.string();
                Log.e(TAG, "onMessage: -----");
                Log.e(TAG, "onMessage: " +  request);
                if ("command 1".equals(request)) {
                    //replay client
                    writeExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                webSocket.sendMessage(RequestBody.create(WebSocket.TEXT,"reply command 1"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if ("command 2".equals(request)) {
                    writeExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Buffer buffer = new Buffer();
                            buffer.read("ping from server".getBytes());
                            try {
                                webSocket.sendPing(buffer);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void onPong(Buffer payload) {
                Log.e(TAG, "onPong: " );
                //客户端收到ping帧后回复pong帧，回调到这，收到pong帧后关闭连接
                //关闭连接
                writeExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            webSocket.close(1000,"normal close");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onClose(int code, String reason) {
                Log.e(TAG, "onClose: " + code + "," + reason);
            }
        }));
    }

    public String getHostName(){
        return mockWebServer.getHostName();
    }

    public int getPort(){
        return mockWebServer.getPort();
    }

    public interface OnInitializeListener{
        void onInitialized();
    }
}
