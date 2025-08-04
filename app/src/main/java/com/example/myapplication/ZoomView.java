package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomView extends AppCompatImageView {

    private Matrix matrix = new Matrix();
    private float[] matrixValues = new float[9];

    private float saveScale = 1f;
    private float minScale = 1f;
    private float maxScale = 6f;

    private ScaleGestureDetector scaleDetector;
    private GestureDetector tapDetector;

    private PointF lastTouch = new PointF();
    private int mode = NONE;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    public ZoomView(Context context) {
        super(context);
        init(context);
    }

    public ZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setScaleType(ScaleType.MATRIX);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        tapDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                performClick();
                return true;
            }
        });

        post(() -> {
            if (getDrawable() == null) return;

            float viewWidth = getWidth();
            float viewHeight = getHeight();
            float imageWidth = getDrawable().getIntrinsicWidth();
            float imageHeight = getDrawable().getIntrinsicHeight();

            float scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);
            minScale = scale;
            saveScale = scale;

            float dx = (viewWidth - imageWidth * scale) / 2f;
            float dy = (viewHeight - imageHeight * scale) / 2f;

            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            setImageMatrix(matrix);
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        tapDetector.onTouchEvent(event);

        PointF current = new PointF(event.getX(), event.getY());

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastTouch.set(current);
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG && saveScale > minScale) {
                    float dx = current.x - lastTouch.x;
                    float dy = current.y - lastTouch.y;

                    matrix.postTranslate(dx, dy);
                    fixTranslation();
                    setImageMatrix(matrix);

                    lastTouch.set(current);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= scaleFactor;

            if (saveScale > maxScale) {
                saveScale = maxScale;
                scaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                scaleFactor = minScale / origScale;
            }

            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            fixTranslation();
            setImageMatrix(matrix);
            return true;
        }
    }
    public void resetZoom() {
        saveScale = minScale;
        matrix.reset();
        setImageMatrix(matrix);
    }
    private void fixTranslation() {
        matrix.getValues(matrixValues);

        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];

        float scale = matrixValues[Matrix.MSCALE_X];
        float imageWidth = getDrawable().getIntrinsicWidth() * scale;
        float imageHeight = getDrawable().getIntrinsicHeight() * scale;

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float maxTransX = 0;
        float maxTransY = 0;

        float minTransX = viewWidth - imageWidth;
        float minTransY = viewHeight - imageHeight;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
//        // Clamp within bounds only if image is larger than view
//        float clampedX = imageWidth > viewWidth ? clamp(transX, minTransX, maxTransX) : (viewWidth - imageWidth) / 2f;
//        float clampedY = imageHeight > viewHeight ? clamp(transY, minTransY, maxTransY) : (viewHeight - imageHeight) / 2f;
//
//        matrixValues[Matrix.MTRANS_X] = clampedX;
//        matrixValues[Matrix.MTRANS_Y] = clampedY;
        if(imageWidth>viewWidth || imageWidth>screenWidth){
            matrixValues[Matrix.MTRANS_X]=clamp(transX, minTransX, maxTransX);
        }
        if(imageHeight>viewHeight){
            matrixValues[Matrix.MTRANS_Y]=clamp(transY, minTransY, maxTransY);
        }

        matrix.setValues(matrixValues);
    }

    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
