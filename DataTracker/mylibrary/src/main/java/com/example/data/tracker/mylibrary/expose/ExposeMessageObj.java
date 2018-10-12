package com.example.data.tracker.mylibrary.expose;

import java.util.HashMap;
import java.util.Map;

/**
 * class for expose message send
 */
public class ExposeMessageObj {
    private int exposeType;
    private Map<String, ExposeModel> currentExposeList = new HashMap<>();
    private Map<String, ExposeModel> lastExposeList = new HashMap<>();

    public int getExposeType() {
        return exposeType;
    }

    public void setExposeType(int exposeType) {
        this.exposeType = exposeType;
    }

    public void setCurrentExposeList(Map<String, ExposeModel> currentExposeList) {
        this.currentExposeList = currentExposeList;
    }

    public void setLastExposeList(Map<String, ExposeModel> lastExposeList) {
        this.lastExposeList = lastExposeList;
    }

    public Map<String, ExposeModel> getCurrentExposeList() {
        return currentExposeList;
    }

    public Map<String, ExposeModel> getLastExposeList() {
        return lastExposeList;
    }
}