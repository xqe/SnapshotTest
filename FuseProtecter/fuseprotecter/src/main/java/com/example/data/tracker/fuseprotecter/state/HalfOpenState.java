package com.example.data.tracker.fuseprotecter.state;

import com.example.data.tracker.fuseprotecter.sdkAPI.API;

public class HalfOpenState implements State {

    private int successCount;
    private API api;

    public HalfOpenState(API api) {
        this.api = api;
    }

    @Override
    public Object execute(Object... args) {
        api.method1();
        //if () 成功计数
        {
            successCount ++;
        }
        return null;
    }
}
