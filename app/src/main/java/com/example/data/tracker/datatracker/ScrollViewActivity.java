package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollViewActivity extends Activity {

    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        ButterKnife.bind(this);
        addItems();
    }

    private void addItems() {
        for (int i = 0; i < container.getChildCount(); i++) {
            TextView textView = (TextView) container.getChildAt(i);
            textView.setText("item" + i);
        }
    }
}
