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

import com.example.data.tracker.mylibrary.controler.DataTransmit;
import com.example.data.tracker.mylibrary.viewCrawler.floatSelect.FloatWindowTracker;
import com.example.data.tracker.mylibrary.viewCrawler.floatSelect.FocusView;
import com.example.data.tracker.mylibrary.visitor.AccessibilityDelegate;
import com.example.data.tracker.mylibrary.visitor.ContainerReplace;
import com.example.data.tracker.mylibrary.visitor.ExposeManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FloatWindowTracker.getInstance().bind(this);
        focusView = new FocusView(this);
        Log.e(TAG, "onCreate: " + this.getClass().getCanonicalName() );
        View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "onGlobalLayout: =================");
            }
        });

        ContainerReplace.replaceContainer(this);

        button.setAccessibilityDelegate(new AccessibilityDelegate());

        button.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.e(TAG, "onSystemUiVisibilityChange: ===============" );
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
        if (!isViewAdd) {
            isViewAdd = true;
            container.addView(focusView);
        } else {
            isViewAdd = false;
            container.removeView(focusView);
        }

        if (button.getVisibility() == View.VISIBLE) {
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean visibility = ExposeManager.checkExposureViewDimension(focusView);
                Log.e(TAG, "ContainerLayout test VIEW REMOVE: " + visibility + "," + focusView.getVisibility());
                boolean visibility1 = ExposeManager.checkExposureViewDimension(button);
                Log.e(TAG, "ContainerLayout test VIEW VISIBLE: " + visibility1 + "," + button.getVisibility());
            }
        },5000);
    }

    @OnClick(R.id.button3)
    public void turn() {
        startActivity(new Intent(this, SecondActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataTransmit.getInstance().startTransmit(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatWindowTracker.getInstance().unBind();
    }
}
