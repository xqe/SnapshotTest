package com.example.data.tracker.mylibrary.viewCrawler.floatSelect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

public class FocusView extends View {

    private Paint paint;
    private static final int RADIUS = 50;
    private int locationX;
    private int locationY;

    public FocusView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    public int getViewWidth(){
        return RADIUS * 2;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(50,50,RADIUS,paint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void dispatchVisibilityChanged(@NonNull View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
        Log.e("ContainerLayout FocusView", "dispatchVisibilityChanged: " + changedView.getClass().getName() + "," + visibility );
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.e("ContainerLayout FocusView", "onVisibilityChanged: " + changedView.getClass().getName() + "," + visibility );
    }
}
