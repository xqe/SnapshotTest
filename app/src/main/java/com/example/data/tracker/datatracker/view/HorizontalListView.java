package com.example.data.tracker.datatracker.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.data.tracker.datatracker.adapter.HorizontalListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HorizontalListView extends RecyclerView {

    public HorizontalListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add("data bean " + i);
        }
        HorizontalListAdapter adapter = new HorizontalListAdapter(dataList);
        setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(linearLayoutManager);
    }


}
