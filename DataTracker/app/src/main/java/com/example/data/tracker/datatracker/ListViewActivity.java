package com.example.data.tracker.datatracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.example.data.tracker.datatracker.adapter.RecyclerAdapter;
import com.example.data.tracker.datatracker.bean.DownloadInfo;
import com.example.data.tracker.mylibrary.viewCrawler.floatSelect.FloatWindowTracker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListViewActivity extends BaseActivity implements RecyclerAdapter.OnItemClickListener,RecyclerAdapter.OnDownloadClickListener{

    private static final String TAG = "ListViewActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ButterKnife.bind(this);
        List<DownloadInfo> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DownloadInfo downloadInfo = new DownloadInfo();
            downloadInfo.setAppId(i);
            downloadInfo.setAppName("appName" + i);
            downloadInfo.setDownloadUrl("http://download" + i);
            dataList.add(downloadInfo);
        }
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(dataList);
        recyclerAdapter.setOnDownloadClickListener(this);
        recyclerAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //FloatWindowTracker.getInstance().bind(this);

        View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "onGlobalLayout: =================");
            }
        });
    }

    @OnClick(R.id.button)
    public void onClick() {

    }

    @Override
    public void onDownload(DownloadInfo downloadInfo) {
        Log.e(TAG, "onDownload: " + downloadInfo.toString());
    }

    @Override
    public void onItemClick(DownloadInfo downloadInfo) {
        Log.e(TAG, "onItemClick: " + downloadInfo.toString());
        Intent intent = new Intent(this,DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailActivity.DATA_KEY,downloadInfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatWindowTracker.getInstance().unBind();
    }
}
