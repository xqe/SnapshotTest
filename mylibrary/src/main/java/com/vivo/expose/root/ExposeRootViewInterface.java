package com.vivo.expose.root;

import android.support.annotation.Nullable;

import com.vivo.expose.model.ReportType;

/**
 * Created by 11070542 on 2017/12/19.
 * 滚动容器
 */
public interface ExposeRootViewInterface {

    void onExposeResume(@Nullable RootViewOptionInterface option);

    void onExposeResume();

    void onExposePause(ReportType... reportType);

    @Nullable
    RootViewOptionInterface getRootViewOption();

    boolean isEnable();

}
