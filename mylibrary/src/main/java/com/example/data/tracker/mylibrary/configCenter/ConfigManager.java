package com.example.data.tracker.mylibrary.configCenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigManager {
    private static final String TAG = "ConfigManager";
    private static final String CONFIG = "event_bind_config";

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    private void storeVisualConfig(Context context, Map<String, Set<String>> eventMap) {
        //1、viewId - targetActivity
        //2、activity - set<ViewId>
        //事件测试，仅缓存解析当前编辑事件，不存入SharePreference
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String targetActivity : eventMap.keySet()) {
            editor.putStringSet(targetActivity, eventMap.get(targetActivity));
        }
        editor.apply();
    }

    /**
     * parse config json message from web
     */
    public void parseConfigMessage(Context context, JSONObject jsonObject) {
        //1、解析成ViewID再存储 --eventId等信息无法存储 2、直接存储，ViewFinder使用时再解析
        JSONArray eventArray = null;
        try {
            JSONObject payload = jsonObject.getJSONObject("payload");
            eventArray = payload.getJSONArray("events");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (eventArray == null) {
            return;
        }
        Map<String, Set<String>> storeResult = new HashMap<>();
        for (int i = 0; i < eventArray.length(); i++) {
            try {
                JSONObject event = eventArray.getJSONObject(i);
                String targetActivity = event.getString("target_activity");
               /*
                String triggerId = event.getString("trigger_id");
                String eventType = event.getString("event_type");
                String eventName = event.getString("event_name");
                JSONArray pathArray = event.getJSONArray("path");
                List<PathElement> pathElements = new ArrayList<>();
                for (int j = 0; j < pathArray.length(); j++) {
                    JSONObject pathElementJson = pathArray.getJSONObject(j);
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
                        .build();*/


                Set<String> bindEvents = storeResult.get(targetActivity);
                if (bindEvents == null) {
                    bindEvents = new HashSet<>();
                }
                bindEvents.add(event.toString());
                storeResult.put(targetActivity, bindEvents);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (storeResult.isEmpty()) {
            Log.i(TAG, "parseConfig: ");
            return;
        }
        storeVisualConfig(context, storeResult);
    }

    public BindEvent parseSingleConfigMessage(JSONObject jsonObject) {
        return null;
    }

    /**
     * get config from sp
     * @return Set<ViewID></ViewID>
     */
    public Set<BindEvent> getVisualConfig(Context context, String activityName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Set<String> eventJsonList = sharedPreferences.getStringSet(activityName, null);
        Set<BindEvent> result = new HashSet<>();
        if (eventJsonList != null) {
            for (String eventJson : eventJsonList) {
                JSONObject event = null;
                try {
                    event = new JSONObject(eventJson);
                    String targetActivity = event.getString("target_activity");
                    String triggerId = event.getString("trigger_id");
                    String eventType = event.getString("event_type");
                    String eventName = event.getString("event_name");
                    JSONArray pathArray = event.getJSONArray("path");
                    List<PathElement> pathElements = new ArrayList<>();
                    for (int j = 0; j < pathArray.length(); j++) {
                        JSONObject pathElementJson = pathArray.getJSONObject(j);
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
                    result.add(bindEvent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
