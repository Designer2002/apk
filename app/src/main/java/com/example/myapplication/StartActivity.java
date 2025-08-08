package com.example.myapplication;
import android.Manifest;

import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import com.chaquo.python.Python;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Arrays;
import java.util.List;


public class StartActivity extends AppCompatActivity {
    public static int selected = -1;
    private Button con;
    private RecyclerView recyclerView;
    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        //switchTheme(2);
        setContentView(R.layout.start_window);
        con = findViewById(R.id.continue_button);



        recyclerView = findViewById(R.id.recycler);
        ShowChooseDialog();

        //ShowChooseDialog();


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void ShowChooseDialog() {
        List<String> options = Arrays.asList(
                getString(R.string.default_rap),
                getString(R.string.r7),
                getString(R.string.r6),
                getString(R.string.r62),
                getString(R.string.r5)
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        RecycleViewAdapter adapter = new RecycleViewAdapter(recyclerView, options, index -> {
            selected = index % options.size(); // по mod вернуть настоящий индекс
            //handler.removeCallbacks(scrollRunnable);
        });
        recyclerView.setAdapter(adapter);
        con.setOnClickListener(v -> {
            if (selected != -1) {
                if (selected == 0) {
                    Intent intent = new Intent(StartActivity.this, ZoomActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                if (selected == 3 || selected == 1) {
                    Intent intent = new Intent(StartActivity.this, MainActivity7_62.class);
                    startActivity(intent);
                    finish();
                    return;
                } else if (selected == 4) {
                    Intent intent = new Intent(StartActivity.this, MainActivity5.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            else if (selected == 2) {
                Intent intent = new Intent(StartActivity.this, MainActivity6.class);
                startActivity(intent);
                finish();
                return;
            }
            }

        });
    }


}
