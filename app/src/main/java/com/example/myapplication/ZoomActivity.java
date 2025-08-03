package com.example.myapplication;


import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class ZoomActivity extends AppCompatActivity {
    private TextView scaleLabel;
private ImageView imageView;

    private float scaleFactor = 1.0f;
    private Matrix matrix = new Matrix();

    boolean isDragging = false;final float[] last = new float[2];
    private RectF bounds = new RectF();
    private ScaleGestureDetector scaleDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_zoom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

            setContentView(R.layout.image_layout);

ImageButton home = findViewById(R.id.home_button2);
home.setOnClickListener(v->{
    Intent intent = new Intent(ZoomActivity.this, MainActivity.class);

    startActivity(intent);
});

             imageView = findViewById(R.id.zoomable_image);

            // далее — масштаб и прокрутка, как уже настроено
        scaleLabel = findViewById(R.id.scale_label);


        scaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float prevScale = scaleFactor;
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(2.2f, Math.min(scaleFactor, 6.0f));

                float scaleChange = scaleFactor / prevScale;

                // Применяем относительный масштаб, но с ограничением
                matrix.postScale(scaleChange, scaleChange, detector.getFocusX(), detector.getFocusY());

                fixTranslation();

                imageView.setImageMatrix(matrix);
                updateScaleText();
                return true;
            }
        });


        imageView.post(() -> {
            Drawable drawable = imageView.getDrawable();
            if (drawable == null) return;

            float imageWidth = drawable.getIntrinsicWidth();
            float imageHeight = drawable.getIntrinsicHeight();
            float viewWidth = imageView.getWidth();
            float viewHeight = imageView.getHeight();

            float scaleX = viewWidth / imageWidth;
            float scaleY = viewHeight / imageHeight;
            float scale = Math.min(scaleX, scaleY);

            float dx = (viewWidth - imageWidth * scale) / 2;
            float dy = (viewHeight - imageHeight * scale) / 2;

            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);

            imageView.setImageMatrix(matrix);
            scaleFactor = scale;

            updateScaleText();
        });



        imageView.setOnTouchListener((v, event) -> {
            scaleDetector.onTouchEvent(event);

            if (event.getPointerCount() == 1 && !scaleDetector.isInProgress()) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        last[0] = event.getX();
                        last[1] = event.getY();
                        isDragging = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float dx = event.getX() - last[0];
                            float dy = event.getY() - last[1];
                            matrix.postTranslate(dx, dy);
                            fixTranslation();
                            imageView.setImageMatrix(matrix);
                            last[0] = event.getX();
                            last[1] = event.getY();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        break;
                }
            }

            return true;
        });
        updateScaleText();
    }
    void fixTranslation() {
        if (imageView.getDrawable() == null) return;

        matrix.getValues(new float[9]);
        Matrix temp = new Matrix(matrix);

        // Получаем границы изображения после трансформации
        Drawable d = imageView.getDrawable();
        bounds.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        temp.mapRect(bounds);

        float viewWidth = imageView.getWidth();
        float viewHeight = imageView.getHeight();

        float dx = 0, dy = 0;

        // Горизонтальные границы
        if (bounds.width() > viewWidth) {
            if (bounds.left > 0) dx = -bounds.left;
            else if (bounds.right < viewWidth) dx = viewWidth - bounds.right;
        } else {
            dx = (viewWidth - bounds.width()) / 2 - bounds.left;
        }

        // Вертикальные границы
        if (bounds.height() > viewHeight) {
            if (bounds.top > 0) dy = -bounds.top;
            else if (bounds.bottom < viewHeight) dy = viewHeight - bounds.bottom;
        } else {
            dy = (viewHeight - bounds.height()) / 2 - bounds.top;
        }

        matrix.postTranslate(dx, dy);
    }
    private void updateScaleText() {
        scaleLabel.setText(String.format(Locale.getDefault(), "Масштаб: %.1fx", scaleFactor/2));
    }
}