package com.vivo.expose.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by 11070542 on 2018/1/17.
 * 上报埋点page
 */

public class ReportType {

    private static ReporterConnectCallback sReporterConnectCallback = null;

    public static void setDefaultConnectCallback(ReporterConnectCallback reporterConnectCallback) {
        sReporterConnectCallback = reporterConnectCallback;
    }

    public static ReporterConnectCallback getReporterConnectCallback() {
        return sReporterConnectCallback;
    }

    @NonNull
    private String mPage;

    @Nullable
    private HashMap<String, String> mExtras;

    //是否过滤小于50毫秒的曝光，默认不过滤
    private boolean mIsFilterShortExpose = false;

    private String mOnceExposePage;

    public ReportType(@NonNull String page) {
        this(page, null);
    }

    public ReportType(@NonNull String page, HashMap<String, String> extras) {
        mPage = page;
        mExtras = extras;
    }

    public ReportType setOnceExposePage(String onceExposePage) {
        this.mOnceExposePage = onceExposePage;
        return this;
    }

    public String getOnceExposePage() {
        return mOnceExposePage;
    }

    public boolean isFilterShortExpose() {
        return mIsFilterShortExpose;
    }

    public ReportType filterShortExpose(boolean isFilterShortExpose) {
        this.mIsFilterShortExpose = isFilterShortExpose;
        return this;
    }

    public String getPage() {
        return mPage;
    }

    @Nullable
    public HashMap<String, String> getExtras() {
        return mExtras;
    }

    @Nullable
    public ReporterConnectCallback getReporter() {
        return sReporterConnectCallback;
    }

    /**
     * page同级的埋点不会变来变去时才能使用这个
     *
     * @param key
     * @param value
     */
    public void putExtra(String key, String value) {
        if (mExtras == null) {
            mExtras = new HashMap<>();
        }
        mExtras.put(key, value);
    }
}
