package com.vivo.expose.utils;

import android.util.Log;

/**
 * Created by 11070542 on 2018/1/17.
 */

public class HideVlog {

    public static boolean sDebugSDK = false;

    /*
    PromptlyReporterCenter.enableDebug()开启打log统计曝光缺失率
     */
    public static boolean sDebugExpose = false;

    /**
     * 普通打log，仅SDK开发者使用，一般DEBUG_SDK设置为false
     *
     * @param tag
     * @param content
     */
    public static void d(String tag, String content) {
        if (sDebugSDK) {
            Log.d("AppStore." + tag, content);
        }
    }

    /**
     * 打log统计曝光缺失率，及时发现漏曝光的情况
     * 目前打log的时机有开始曝光、结束曝光、上报count!=0的曝光
     *
     * @param tag
     * @param content
     */
    public static void debugExpose(String tag, String content) {
        if (sDebugExpose) {
            Log.d("AppStore." + tag, content);
        }
    }

}
