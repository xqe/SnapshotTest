package com.example.data.tracker.mylibrary.interaction;

import android.app.Activity;
import android.content.Context;

import com.example.data.tracker.mylibrary.AOP.ScreenState;
import com.example.data.tracker.mylibrary.interaction.connect.EditorConnection;
import com.example.data.tracker.mylibrary.interaction.connect.IWebMessage;
import com.example.data.tracker.mylibrary.interaction.connect.ShakeSensor;

import java.net.URI;

public class WebEditor implements ShakeSensor.ShakeListener,ScreenState.OnScreenChangeListener {

    private static final String TAG = "WebEditor";
    private URI uri;
    private static final int SOCKET_TIMEOUT = 100;
    private ShakeSensor shakeSensor;
    private EditorConnection editorConnection;
    private ScreenInfoCrawler screenInfoCrawler;
    private ScreenState screenState;
    private IWebMessage webMessage;

    public WebEditor(Context context,URI uri) {
        this.uri = uri;
        screenInfoCrawler = new ScreenInfoCrawler();
        screenState = new ScreenState(context,this);
        webMessage = new WebMessageImpl();
        shakeSensor = new ShakeSensor(this);
    }

    //执行脚本，发广播启动Sensor or 发广播开启部署模式
    public void start(){
        //start connect
        shakeSensor.cancelListen();
        editorConnection = new EditorConnection(uri,SOCKET_TIMEOUT,webMessage);

    }

    @Override
    public void onShake() {

    }
    /**
     * exit from editor mode
     * */
    public void exit() {
        screenState.close();
    }

    @Override
    public void onActivityChange(Activity activity) {
        if (editorConnection.isDisconnected()) {
            return;
        }
        screenInfoCrawler.writeScreenInfo(editorConnection.getBufferedOutputStream(),activity);
    }

    @Override
    public void onLayoutChange(Activity activity) {
        if (editorConnection.isDisconnected()) {
            return;
        }
        screenInfoCrawler.writeScreenInfo(editorConnection.getBufferedOutputStream(),activity);
    }
}
