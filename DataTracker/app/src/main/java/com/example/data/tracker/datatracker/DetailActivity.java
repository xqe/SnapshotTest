package com.example.data.tracker.datatracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.data.tracker.aop.DataBind;
import com.example.data.tracker.datatracker.bean.DownloadInfo;
import com.example.data.tracker.mylibrary.viewCrawler.floatSelect.FloatWindowTracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends Activity {

    private static final String TAG = "DetailActivity";
    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.download_url)
    TextView downloadUrl;
    @BindView(R.id.download)
    Button download;
    @BindView(R.id.detail)
    Button detail;
    private DownloadInfo downloadInfo;
    public static final String DATA_KEY = "detail";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        downloadInfo = (DownloadInfo) getIntent().getSerializableExtra(DATA_KEY);
        ButterKnife.bind(this);
        downloadUrl.setText(downloadInfo.getDownloadUrl());
        appName.setText(downloadInfo.getAppName());
        FloatWindowTracker.getInstance().bind(this);
    }

    @OnClick({R.id.download,R.id.detail})
    public void onDownload(View view) {
        switch (view.getId()) {
            case R.id.download:
                @DataBind
                String data = downloadInfo.getAppId() + "";
                break;
            case R.id.detail:
                @DataBind
                String detailData = downloadInfo.getAppName();
                break;
             default:
                 break;
        }
        Log.e(TAG, "onDownload: " + downloadInfo.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatWindowTracker.getInstance().unBind();
    }
}
