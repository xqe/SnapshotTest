package com.vivo.expose.model;

/**
 * Created by 11070542 on 2018/1/17.
 * 在里面实现上报的网络请求
 */

public interface ReporterConnectCallback {

    /**
     * 实时曝光上报的网络请求由业务实现
     *
     * @param reportType
     * @param appexpo
     */
    void reportExposeData(ReportType reportType, String appexpo);

    /**
     * 展示曝光上报的网络请求由业务实现,注意：是主线程调用，请勿耗时操作
     *
     * @param page
     * @param appexpo
     */
    void reportOnceExposeData(String page, String appexpo);

    /**
     * 非聚合上报实时曝光，一次开始曝光一条请求 注意：1.是主线程调用，请勿耗时操作 2.可能存在一结束曝光，马上又开始曝光的风险
     *
     * @param exposeAppData
     * @param revertExpose  是否撤销了上一次结束曝光，不算新的曝光次数
     */
    void reportSingleExposeData(ExposeAppData exposeAppData, boolean revertExpose);

}
