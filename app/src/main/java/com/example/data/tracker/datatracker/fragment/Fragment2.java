package com.example.data.tracker.datatracker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.data.tracker.datatracker.FragmentActivity;
import com.example.data.tracker.datatracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class Fragment2 extends Fragment {

    private static final String TAG = "!!!!Fragment2";
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.button)
    Button button;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement2, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.button)
    public void onClick() {
        ((FragmentActivity) getActivity()).transact(new Fragment1());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
