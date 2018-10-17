package com.example.data.tracker.mylibrary.configCenter;

import android.text.TextUtils;

public class PathElement {
    private String className;
    private int index;
    private int id = -1;
    private String idName;
    private static final String RESOURCE_TAG = "#";

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String parseToViewID() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(className)
                .append("[")
                .append(index)
                .append("]");
        if (TextUtils.isEmpty(idName)) {
            stringBuilder .append(RESOURCE_TAG).append(idName);
        }
        return stringBuilder.toString();
    }
}
