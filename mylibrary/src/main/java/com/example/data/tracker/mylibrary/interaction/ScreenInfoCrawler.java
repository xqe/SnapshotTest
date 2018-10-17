package com.example.data.tracker.mylibrary.interaction;

import android.util.JsonWriter;
import android.view.View;

import com.example.data.tracker.mylibrary.interaction.snapshot.SnapshotCrawler;
import com.example.data.tracker.mylibrary.interaction.viewTree.ViewTreeCrawler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ScreenInfoCrawler {
    private SnapshotCrawler snapshotCrawler;
    private ViewTreeCrawler viewTreeCrawler;

    public ScreenInfoCrawler() {
        snapshotCrawler = new SnapshotCrawler();
        viewTreeCrawler = new ViewTreeCrawler();
    }

    void writeScreenInfo(OutputStream outputStream, View rootView) {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        JsonWriter jsonWriter = new JsonWriter(writer);
        try {
            viewTreeCrawler.writeViewTreeInfo(rootView,jsonWriter);
            snapshotCrawler.writeSnapshotData(rootView,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
