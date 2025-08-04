package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;

import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class ZoomActivity extends AppCompatActivity {
    private ZoomView imageView;
    private ScaleGestureDetector scaleGestureDetector;

    private float scaleFactor = 1f;
    private final float MIN_SCALE = 1f;
    private final float MAX_SCALE = 4f;

    private int originalWidth;
    private int originalHeight;
    private boolean originalSizeCaptured = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        imageView = findViewById(R.id.img);
        ImageButton home = findViewById(R.id.back);
        home.setOnClickListener(v -> {
            Intent intent = new Intent(ZoomActivity.this, MainActivity.class);
            startActivity(intent);
        });
        Button reset = findViewById(R.id.btnReset);
        reset.setOnClickListener(v -> imageView.resetZoom());

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        imageView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            // Return false so ScrollView/HScrollView can also scroll
            return false;
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (!originalSizeCaptured) {
                originalWidth = imageView.getWidth();
                originalHeight = imageView.getHeight();
                originalSizeCaptured = true;
            }

            float scale = detector.getScaleFactor();
            scaleFactor *= scale;
            scaleFactor = Math.max(MIN_SCALE, Math.min(scaleFactor, MAX_SCALE));

            int newWidth = (int) (originalWidth * scaleFactor);
            int newHeight = (int) (originalHeight * scaleFactor);

            imageView.getLayoutParams().width = newWidth;
            imageView.getLayoutParams().height = newHeight;
            imageView.requestLayout();

            return true;
        }
    }
}