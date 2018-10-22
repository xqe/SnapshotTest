package com.example.data.tracker.mylibrary.configCenter;

import com.example.data.tracker.mylibrary.test.floatSelect.ViewIDMaker;

import java.util.List;

public class BindEvent {

    private String triggerId;
    private String eventType;
    private String eventName;
    private String targetActivity;
    private List<PathElement> path;

    public BindEvent(Builder builder) {
        this.triggerId = builder.triggerId;
        this.eventType = builder.eventType;
        this.eventName = builder.eventName;
        this.targetActivity = builder.targetActivity;
        this.path = builder.path;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTargetActivity() {
        return targetActivity;
    }

    public List<PathElement> getPath() {
        return path;
    }

    public String createViewID() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            PathElement pathElement = path.get(i);
            sb.append(pathElement.parseToViewID());
            sb.append(ViewIDMaker.CONNECTOR);
        }
        sb.substring(sb.length()-1,sb.length());
        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static class Builder{
        private String triggerId;
        private String eventType;
        private String eventName;
        private String targetActivity;
        private List<PathElement> path;

        public Builder triggerId(String triggerId) {
            this.triggerId = triggerId;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder eventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public Builder targetActivity(String targetActivity) {
            this.targetActivity = targetActivity;
            return this;
        }

        public Builder path(List<PathElement> path) {
            this.path = path;
            return this;
        }

        public BindEvent build() {
            return new BindEvent(this);
        }
    }
}
