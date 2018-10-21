package com.example.data.tracker.mylibrary.configCenter;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigManager {
    private static final String CONFIG = "event_bind_config";

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    private void storeConfig(Context context,List<BindEvent> bindEvents) {
        //1、viewId - targetActivity
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < bindEvents.size(); i++) {
            BindEvent bindEvent = bindEvents.get(i);
            String bindViewID = bindEvent.createViewID();
            editor.putString(bindViewID,bindEvent.getTargetActivity());
        }
        editor.apply();

        //2、activity - set<ViewId>

        //事件测试，仅缓存解析当前编辑事件，不存入SharePreference
    }


    public void parseConfig(Context context,JSONObject jsonObject) {
        //1、解析成ViewID再存储  2、直接存储，ViewFinder使用时再解析

        JSONArray jsonArray = null;
        try {
            JSONObject payload = jsonObject.getJSONObject("payload");
            jsonArray = payload.getJSONArray("events");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray == null) {
            return;
        }
        Map<String,Set<String>> storeResult = new HashMap<>();
        List<BindEvent> bindEvents = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject event = jsonArray.getJSONObject(i);
                String triggerId = event.getString("trigger_id");
                String eventType = event.getString("event_type");
                String targetActivity = event.getString("target_activity");
                String eventName = event.getString("event_name");
                JSONArray pathArray = event.getJSONArray("path");
                List<PathElement> pathElements = new ArrayList<>();
                for (int j = 0; j < pathArray.length(); j++) {
                    JSONObject pathElementJson = pathArray.getJSONObject(i);
                    String className = pathElementJson.getString("class_name");
                    String index = pathElementJson.getString("index");
                    String id = pathElementJson.getString("id");
                    String idName = pathElementJson.getString("id_name");
                    pathElements.add(new PathElement.Builder()
                            .className(className)
                            .idName(idName)
                            .id(Integer.parseInt(id))
                            .index(Integer.parseInt(index))
                            .build());
                }
                BindEvent bindEvent = new BindEvent.Builder()
                        .triggerId(triggerId)
                        .eventName(eventName)
                        .eventType(eventType)
                        .targetActivity(targetActivity)
                        .path(pathElements)
                        .build();
                bindEvents.add(bindEvent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (bindEvents.isEmpty()) {
            return;
        }
        storeConfig(context,bindEvents);
    }

    public Set<String> readConfig(Context context, String activityName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(activityName,null);
    }

}
