package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.example.data.tracker.datatracker.fragment.Fragment1;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentActivity extends Activity {

    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        transact(new Fragment1());
    }

    public void transact(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment).commit();

    }
}
