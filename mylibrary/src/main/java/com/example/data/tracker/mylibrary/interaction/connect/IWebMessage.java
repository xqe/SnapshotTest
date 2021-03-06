package com.example.data.tracker.mylibrary.interaction.connect;

import org.json.JSONObject;

public interface IWebMessage {
    public void sendSnapshot(JSONObject message);
    public void performEdit(JSONObject message);
    public void clearEdits(JSONObject message);
    public void bindEvents(JSONObject message);
    public void setTweaks(JSONObject message);
    public void sendDeviceInfo();
    public void cleanup();
}
