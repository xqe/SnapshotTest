package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.data.tracker.mylibrary.viewCrawler.viewTree.FloatWindowTracker;
import com.example.data.tracker.mylibrary.websocket.Test;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {


    private static final String TAG = "MainActivity";
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.textView2)
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FloatWindowTracker.getInstance().bind(this);
    }

    @OnClick(R.id.button)
    public void click() {
        int[] location = new int[2];
        button4.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        Map<String,String> params = new HashMap<>();
        params.put("1","dsdksjkdsj");
        button.setTag(R.string.view_tag,params);
        textView2.setText("turn x:" + viewX + "~" + (viewX + button4.getWidth()));
        textView.setText("turn y:" + viewY + "~" + (viewY + button4.getHeight()));
    }

    @OnClick(R.id.button2)
    public void test() {
        new Test().testWebSocket();
    }

    @OnClick(R.id.button3)
    public void turn() {
        startActivity(new Intent(this, ListViewActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatWindowTracker.getInstance().unBind();
    }
}
