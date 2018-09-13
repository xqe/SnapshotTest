package com.example.data.tracker.mylibrary.protocol;

import java.nio.ByteBuffer;

public class SnapshotPackage implements Package {
    private byte[] head = new byte[2];
    private byte[] snapshotData;

    @Override
    public byte[] toByteArray(){
        int totalLength = head.length + snapshotData.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(totalLength);
        byteBuffer.put(head);
        byteBuffer.put(snapshotData);
        return byteBuffer.array();
    }

    @Override
    public void addData(byte[] data) {
        snapshotData = data;
    }
}
