package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);

        ImageButton button = findViewById(R.id.gps_button);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        button.setOnClickListener(v -> {
            // включить жпс
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Функционал приложения требует GPS", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                return;
            }

            // получить координаты
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GPS не дали, работать не будет", Toast.LENGTH_SHORT).show();
                return;
            }

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double x = location.getLatitude();
                    double y = location.getLongitude();

                    // движуха
                    Toast.makeText(MainActivity.this,
                            "X: " + x + "\nY: " + y,
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(@NonNull String provider) {}
                @Override
                public void onProviderDisabled(@NonNull String provider) {}
            }, null);
        });
    }
}
