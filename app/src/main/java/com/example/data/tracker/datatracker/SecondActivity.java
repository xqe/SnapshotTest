package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import com.example.data.tracker.mylibrary.test.floatSelect.ViewIDMaker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondActivity extends Activity {
    @BindView(R.id.button2)
    Button button2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        Log.e("SecondActivity", "onCreate: " + ViewIDMaker.getViewID(button2));
    }
}
