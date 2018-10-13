package com.example.data.tracker.mylibrary.expose;

public class ExposeConfig {

    private float areaRatio = 0.5f;
    private long miniTime = 100;

    public float getAreaRatio() {
        return areaRatio;
    }

    public void setAreaRatio(float areaRatio) {
        this.areaRatio = areaRatio;
    }

    public long getMiniTime() {
        return miniTime;
    }

    public void setMiniTime(long miniTime) {
        this.miniTime = miniTime;
    }
}
