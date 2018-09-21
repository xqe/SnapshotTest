package com.example.data.tracker.mylibrary.data;


import android.util.Log;

import java.nio.ByteBuffer;

public class SnapshotPackage implements Package {
    private static final String TAG = "SnapshotPackage";
    private byte[] head = new byte[2];
    private byte[] snapshotData;

    public SnapshotPackage(byte[] snapshotData) {
        this.snapshotData = snapshotData;
    }

    @Override
    public byte[] toByteArray(){
        int length = head.length + snapshotData.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byteBuffer.put(head);
        byteBuffer.put(snapshotData);
        Log.i(TAG, "toByteArray: " + byteBuffer.array().length);
        return byteBuffer.array();
    }

    @Override
    public void addData(byte[] data) {
        snapshotData = data;
    }

}
