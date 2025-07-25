package com.example.myapplication;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Array;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private static double lat;
    private static double lon;
    private static double att;
    private static String yString;
    private static  String xString;
    private static String FormatCoord(String coordinate){
        double number = Double.parseDouble(coordinate);
        int result = (int) Math.round(number * 1_000_000);
        return String.valueOf(result);
    }
    private void DisplayError(String text, TextView errorView){
        errorView.setText(text);
        errorView.setVisibility(View.VISIBLE);
        errorView.postDelayed(new Runnable() {
            public void run() {
                errorView.setVisibility(View.GONE);
            }
        }, 2000);
        return;
    }
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
        TextView errorView = findViewById(R.id.error_message);
        EditText edit = findViewById(R.id.edit_var);
        TextView viewX = findViewById(R.id.x);
        TextView viewY = findViewById(R.id.y);
        TableLayout table = findViewById(R.id.khaki_table);
        //поля таблицы
        TextView f00 = table.findViewById(R.id.f00);
        TextView f01 = table.findViewById(R.id.f01);
        TextView f02 = table.findViewById(R.id.f02);
        TextView f03 = table.findViewById(R.id.f03);
        TextView f04 = table.findViewById(R.id.f04);
        TextView f05 = table.findViewById(R.id.f05);
        TextView f10 = table.findViewById(R.id.f10);
        TextView f11 = table.findViewById(R.id.f11);
        TextView f12 = table.findViewById(R.id.f12);
        TextView f13 = table.findViewById(R.id.f13);
        TextView f14 = table.findViewById(R.id.f14);
        TextView f15 = table.findViewById(R.id.f15);
        TextView f20 = table.findViewById(R.id.f20);
        TextView f21 = table.findViewById(R.id.f21);
        TextView f22 = table.findViewById(R.id.f22);
        TextView f23 = table.findViewById(R.id.f23);
        TextView f24 = table.findViewById(R.id.f24);
        TextView f25 = table.findViewById(R.id.f25);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        errorView.setOnClickListener(view->{
            errorView.setVisibility(View.GONE);
        });
        button.setOnClickListener(v -> {
            // включить жпс
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Функционал приложения требует GPS", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                return;
            }

            // не дали доступ если
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GPS не дали, работать не будет", Toast.LENGTH_SHORT).show();
                return;
            }

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    att = location.getAltitude();
                    // движуха
                    //1.у нас есть широта долгота высота но они в системе координат WSG-84
                    //это пендосы враг народа и вообще кринж надо в СК-42 перевести первым делом
                    double x = TranslatorWSG84_SK42.WGS84_SK42_Lat(lat, lon, att);
                    double y = TranslatorWSG84_SK42.WGS84_SK42_Long(lat, lon, att);
                    xString = String.valueOf(x);
                    yString = String.valueOf(y);
                    viewX.setText(xString);
                    viewY.setText(yString);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(@NonNull String provider) {}
                @Override
                public void onProviderDisabled(@NonNull String provider) {}
            }, null);
        });
        Button calcButton = findViewById(R.id.calx_btn);
        calcButton.setOnClickListener(v -> {
            try{
                //2.заполняем таблицу
                //но сначала надо убедиться что третья переменная тоже введена
                if (edit.getText() == null){
                    DisplayError("Перед расчётом необходимо ввести третью переменную",errorView);
                    return;
                }
                if (yString==null||xString==null)
                {
                    DisplayError("Перед расчётом необходимо определить местоположение при помощи зеленой кнопки в углу",errorView);
                    return;
                }
                String variable = String.valueOf(edit.getText());
                if (variable.isEmpty()){
                    DisplayError("Перед расчётом необходимо ввести третью переменную",errorView);
                    return;
                }
                if (variable.length()!=4){
                    DisplayError("Неверный формат третьей переменной",errorView);
                    return;
                }
                String xStringFormatted = FormatCoord(xString);
                String yStringFormatted = FormatCoord(yString);
                //сделаем таблицу а потом фигачим в ту что на телефоне
                Integer[][] result = Formula.calculate(xStringFormatted, yStringFormatted, variable);
                f00.setText(String.valueOf(result[0][0]));
                f01.setText(String.valueOf(result[0][1]));
                f02.setText(String.valueOf(result[0][2]));
                f03.setText(String.valueOf(result[0][3]));
                f04.setText(String.valueOf(result[0][4]));
                f05.setText(String.valueOf(result[0][5]));
                f10.setText(String.valueOf(result[1][0]));
                f11.setText(String.valueOf(result[1][1]));
                f12.setText(String.valueOf(result[1][2]));
                f13.setText(String.valueOf(result[1][3]));
                f14.setText(String.valueOf(result[1][4]));
                f15.setText(String.valueOf(result[1][5]));
                f20.setText(String.valueOf(result[2][0]));
                f21.setText(String.valueOf(result[2][1]));
                f22.setText(String.valueOf(result[2][2]));
                f23.setText(String.valueOf(result[2][3]));
                f24.setText(String.valueOf(result[2][4]));
                f25.setText(String.valueOf(result[2][5]));
            }
            catch (Exception e) {
                DisplayError(e.toString(), errorView);
            }

        });
    }
}
