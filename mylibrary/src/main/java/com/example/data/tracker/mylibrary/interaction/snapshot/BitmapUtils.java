package com.example.data.tracker.mylibrary.interaction.snapshot;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
