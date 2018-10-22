package com.example.data.tracker.mylibrary.AOP;

import android.app.Activity;
import android.content.Context;


public class AOPMachine implements ScreenState.OnScreenChangeListener {

    private ScreenState screenState;

    public void init(Context context) {
        screenState = new ScreenState(context,this);
    }

    public void exit() {
        screenState.close();
    }

    @Override
    public void onActivityChange(Activity activity) {
        ViewFinder.getInstance().cacheCurrentConfig(activity);
        ViewFinder.getInstance().setDataDelegate();

    }

    @Override
    public void onLayoutChange(Activity activity) {
        ViewFinder.getInstance().setDataDelegate();
    }
}
