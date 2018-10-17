package com.example.data.tracker.fuseprotecter.state;

import android.os.CountDownTimer;
import android.util.Log;

import com.example.data.tracker.fuseprotecter.sdkAPI.API;

public class OpenState implements State {
    private static final String TAG = "OpenState";
    private CountDownTimer countDownTimer; //开启熔断后，打开计时器，计时模式不定
    private API api;

    public OpenState(API api) {
        this.api = api;
    }

    @Override
    public Object execute(Object... args) {
        Log.e(TAG, "execute: fuse Open,request refused!!");
        return null;
    }
}
