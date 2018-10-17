package com.example.data.tracker.mylibrary.AOP.expose;

import java.util.Map;

public class ExposeModel {
    private long startTime;
    private long endTime;
    private Map<String,String> params;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ExposeModel{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", params=" + params +
                '}';
    }
}
