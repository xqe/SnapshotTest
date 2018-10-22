package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.data.tracker.mylibrary.configCenter.BindEvent;
import com.example.data.tracker.mylibrary.test.floatSelect.FocusView;
import com.example.data.tracker.mylibrary.test.websocket.Test;

import java.util.HashSet;
import java.util.Set;

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
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.test1)
    Button test1;
    @BindView(R.id.test2)
    Button test2;
    @BindView(R.id.test3)
    Button test3;


    boolean isViewAdd = false;
    FocusView focusView;
    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        focusView = new FocusView(this);
        Log.e(TAG, "onCreate: " + this.getClass().getCanonicalName());
        View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "onGlobalLayout: =================");
            }
        });

        button.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.e(TAG, "onSystemUiVisibilityChange: ===============");
            }
        });


    }

    @OnClick(R.id.button)
    public void click() {
        /*int[] location = new int[2];
        button4.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        Map<String, String> params = new HashMap<>();
        params.put("1", "dsdksjkdsj");
        button.setTag(R.string.view_tag, params);
        textView2.setText("turn x:" + viewX + "~" + (viewX + button4.getWidth()));
        textView.setText("turn y:" + viewY + "~" + (viewY + button4.getHeight()));*/

        /*if (!isViewAdd) {
            container.addView(focusView);
        } else {
            container.removeView(focusView);
        }
        isViewAdd = !isViewAdd;*/

        /*test = new Test();
        test.testWebSocket();*/

        Set<BindEvent> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            set.add(new BindEvent.Builder()
                    .eventName("name" +i)
                    .eventType("type" + i)
                    .path(null)
                    .targetActivity("targetActivity" + i)
                    .build());
        }

        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);

    }


    @OnClick(R.id.button2)
    public void test() {
        //SnapshotTimer.getInstance().startShot(this);
    }

    @OnClick(R.id.button3)
    public void turn() {
        startActivity(new Intent(this, FragmentActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
