package com.example.data.tracker.datatracker.bean;

import java.io.Serializable;

public class DownloadInfo implements Serializable {

    private String downloadUrl;
    private int appId;
    private String appName;

    public DownloadInfo() {
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "downloadUrl='" + downloadUrl + '\'' +
                ", appId=" + appId +
                ", appName='" + appName + '\'' +
                '}';
    }
}
