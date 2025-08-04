package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ortiz.touchview.TouchImageView;

public class OverrideTouch extends TouchImageView {

    private final GestureDetector gestureDetector;

    public OverrideTouch(Context context) {
        super(context);
        gestureDetector = createCustomGestureDetector(context);
    }

    public OverrideTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = createCustomGestureDetector(context);
    }

    private GestureDetector createCustomGestureDetector(Context context) {
        return new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setZoomToFit(true);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event); // обрабатываем свои жесты
        return super.onTouchEvent(event);    // остальное пусть делает TouchImageView
    }

    public void setZoomToFit(boolean useWidth) {
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if (imageWidth <= 0 || imageHeight <= 0 || viewWidth <= 0 || viewHeight <= 0) return;

        float zoom;
        if (useWidth) {
            zoom = (float) viewWidth / (float) imageWidth;
        } else {
            zoom = (float) viewHeight / (float) imageHeight;
        }

        // центрируем по экрану
        float centerX = viewWidth / 2f;
        float centerY = viewHeight / 2f;

        setZoom(zoom, centerX, centerY);
    }

}
