package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class StartWindow extends AppCompatActivity {

    private int selectedThemeIndex = 1; // Default: khaki original

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load default theme first if needed
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_content);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<String> themeOptions = Arrays.asList("1", "2", "3");

        ThemeAdapter adapter = new ThemeAdapter(themeOptions, index -> selectedThemeIndex = index);
        recyclerView.setAdapter(adapter);

        Button continueBtn = findViewById(R.id.continue_button);
        continueBtn.setOnClickListener(v -> {
            // Save theme index in shared preferences
            getSharedPreferences("app_theme", MODE_PRIVATE)
                    .edit().putInt("theme_index", selectedThemeIndex).apply();

            // Restart app or go to main
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
