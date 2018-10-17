package com.example.data.tracker.fuseprotecter.state;

import com.example.data.tracker.fuseprotecter.sdkAPI.API;

public class CloseState implements State {

    private int errorCount;
    private API api;

    public CloseState(API api) {
        this.api = api;
    }

    @Override
    public Object execute(Object... args) {
        api.method1();
        //if () 记录接口请求次数
        {
            errorCount ++;
        }
        return null;
    }
}
