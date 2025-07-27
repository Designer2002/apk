package com.example.myapplication;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.lang.reflect.Array;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private static double lat;
    private static double lon;
    private static double att;
    private static String yStringFormatted;
    private static  String xStringFormatted;
    private Button calcButton;
    private TextView viewX;
    private  TextView viewY;
    private TextView errorView;
    private static String FormatCoord(String coordinate) {
        double number = Double.parseDouble(coordinate);
        int multiplier = coordinate.indexOf('.') <= 2 ? 100000 : 10000;
        int result = (int) Math.round(number * multiplier);
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
    @SuppressLint("ResourceAsColor")
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
        errorView = findViewById(R.id.error_message);
        EditText edit = findViewById(R.id.edit_var);
        viewX = findViewById(R.id.x);
        viewY = findViewById(R.id.y);
        TableLayout table = findViewById(R.id.khaki_table);
        LinearLayout spinner = findViewById(R.id.spinner_layout);
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
        //временно запомнить расчеты
        ImageButton recent = findViewById(R.id.recent_button);


        calcButton = findViewById(R.id.calx_btn);
        ImageButton hand_input = findViewById(R.id.hand_button);
        hand_input.setOnClickListener(v -> {
            showCoordDialog();
        });

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        errorView.setOnClickListener(view->{
            errorView.setVisibility(View.GONE);
        });
        button.setOnClickListener(v -> {
                    calcButton.setEnabled(false);
                    calcButton.setBackgroundColor(R.color.grey);
                    // не дали доступ если
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "GPS не дали, работать не будет", Toast.LENGTH_SHORT).show();
                        return;
                    }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Без GPS работать не будет. Необходимо включить его", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                return;
            }
            spinner.setVisibility(View.VISIBLE);
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener(location -> {
                                if (location != null) {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                    att = location.getAltitude();
                                }

                                // движуха
                                //1.у нас есть широта долгота высота но они в системе координат WSG-84
                                //это пендосы враг народа и вообще кринж надо в СК-42 перевести первым делом
                                double x = TranslatorWSG84_SK42.WGS84_SK42_Lat(lat, lon, att);

                                double y = TranslatorWSG84_SK42.WGS84_SK42_Long(lat, lon, att);
                                String xString = String.valueOf(x);
                                String yString = String.valueOf(y);
                                xStringFormatted = FormatCoord(xString);
                                yStringFormatted = FormatCoord(yString);
                                viewX.setText(xStringFormatted);
                                viewY.setText(yStringFormatted);
                                spinner.setVisibility(View.GONE);
                                calcButton.setEnabled(true);
                                calcButton.setBackgroundColor(R.color.khaki_secondary);
                            });
                });

        calcButton.setOnClickListener(v -> {

            try{
                //2.заполняем таблицу
                //но сначала надо убедиться что третья переменная тоже введена
                if (edit.getText() == null){
                    DisplayError("Перед расчётом необходимо ввести третью переменную",errorView);
                    return;
                }
                if (xStringFormatted.isEmpty() || yStringFormatted.isEmpty()){
                    DisplayError("Что-то введено не так!",errorView);
                    return;
                }
                if (xStringFormatted==null||yStringFormatted==null)
                {
                    DisplayError("Перед расчётом необходимо определить местоположение при помощи зеленой кнопки в углу или ввести координаты вручную",errorView);
                    return;
                }
                String variable = String.valueOf(edit.getText());
                if (variable.isEmpty()){
                    DisplayError("Перед расчётом необходимо ввести третью переменную",errorView);
                    return;
                }
                if ((!variable.contains("-") && variable.length()!=4) || (variable.contains("-")&&variable.length()!=5)){
                    DisplayError("Неверный формат третьей переменной",errorView);
                    return;
                }

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
        //подгружаю предыдущий расчет если он есть
        SharedPreferences prefs = getSharedPreferences("calculation_tmp", MODE_PRIVATE);

        String xString = prefs.getString("x_value", null);
        String yString = prefs.getString("y_value", null);
        String variable = prefs.getString("variable", null);
        String resultArray = prefs.getString("array", null);
        if (xString!=null){
            viewX.setText(xString);
        }
        if (yString!=null){
            viewY.setText(yString);
        }
        if (variable!=null){
            edit.setText(variable);
        }
        if (resultArray!=null){

        }
    }
    private void showCoordDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.input_alert, null);

        EditText editX = view.findViewById(R.id.editX);
        EditText editY = view.findViewById(R.id.editY);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        saveBtn.setOnClickListener(v -> {
            try {

                String x = editX.getText().toString().trim();
                String y = editY.getText().toString().trim();
                if (!CheckFormat(x) || !CheckFormat(y)) {
                    DisplayError("Неверный формат введеных координат", errorView);
                }
                xStringFormatted = x;
                yStringFormatted = y;
                viewX.setText(xStringFormatted);
                viewY.setText(yStringFormatted);
                dialog.dismiss();
            }
            catch (Exception e){
                DisplayError("Данные введены неккоретно!", errorView);
            }
        });

        dialog.show();
    }
    private boolean CheckFormat(String coord){
        if (coord.contains("-")){
            return coord.length()==8;
        }
        else{
            return coord.length()==7;
        }
    }
}
