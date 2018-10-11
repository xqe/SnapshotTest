package com.vivo.expose.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.vivo.expose.utils.HidePromptlyReporterUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OnceExposeReporter {

    private HashMap<String, JSONArray> mWaitToReportData = new HashMap<>();

    public void attemptToOnceExpose(@Nullable String page, @Nullable ExposeAppData exposeAppData) {
        if (exposeAppData == null) {
            return;
        }
        if (exposeAppData.isHasOnceExpose()) {
            return;
        }
        exposeAppData.setHasOnceExpose(true);

        if (TextUtils.isEmpty(page)) {
            return;
        }
        JSONArray jsonArray = mWaitToReportData.get(page);
        if (jsonArray == null) {
            jsonArray = new JSONArray();
            mWaitToReportData.put(page, jsonArray);
        }

        jsonArray.put(exposeAppData.toJsonObject(false));
    }

    public void tryToReportAll() {

        if (mWaitToReportData.isEmpty()) {
            return;
        }

        ReporterConnectCallback connectCallback = ReportType.getReporterConnectCallback();
        if (connectCallback == null) {
            return;
        }

        Set<Map.Entry<String, JSONArray>> entrySet = mWaitToReportData.entrySet();
        String date = HidePromptlyReporterUtils.getSpecialDate(System.currentTimeMillis());
        for (Map.Entry<String, JSONArray> entry : entrySet) {
            String page = entry.getKey();
            JSONArray value = entry.getValue();
            if (TextUtils.isEmpty(page) || value == null) {
                continue;
            }

            JSONObject json = new JSONObject();
            try {
                json.put(date, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            connectCallback.reportOnceExposeData(page, json.toString());
        }

    }
}
