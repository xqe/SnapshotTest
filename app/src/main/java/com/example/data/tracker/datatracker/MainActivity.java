package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.data.tracker.mylibrary.test.DataTransmit;
import com.example.data.tracker.mylibrary.test.floatSelect.FocusView;
import com.example.data.tracker.mylibrary.AOP.AccessibilityDelegate;

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
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.test1)
    Button test1;
    @BindView(R.id.test2)
    Button test2;
    @BindView(R.id.test3)
    Button test3;

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

        button.setAccessibilityDelegate(new AccessibilityDelegate());

        button.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.e(TAG, "onSystemUiVisibilityChange: ===============");
            }
        });

    }

    @OnClick(R.id.button)
    public void click() {
        int[] location = new int[2];
        button4.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        Map<String, String> params = new HashMap<>();
        params.put("1", "dsdksjkdsj");
        button.setTag(R.string.view_tag, params);
        textView2.setText("turn x:" + viewX + "~" + (viewX + button4.getWidth()));
        textView.setText("turn y:" + viewY + "~" + (viewY + button4.getHeight()));
    }

    boolean isViewAdd = false;
    FocusView focusView;

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
        DataTransmit.getInstance().startTransmit(this);
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
