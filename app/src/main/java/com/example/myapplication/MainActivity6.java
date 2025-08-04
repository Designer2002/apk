package com.example.myapplication;

import static com.example.myapplication.StartActivity.selected;
import static com.example.myapplication.Utils.CheckFormat;
import static com.example.myapplication.Utils.DisplayError;
import static com.example.myapplication.Utils.FormatCoord;
import static com.example.myapplication.Utils.ShowSpinnerDialog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class MainActivity6 extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private static double lat;
    private static double lon;
    private static double att;
    private final Context context = MainActivity6.this;
    private int position = Integer.MAX_VALUE / 2;
    private static String yStringFormatted, x2StringFormatted;
    private static String variable;
    private static  String xStringFormatted, y2StringFormatted, endx, endy;
    private TextView f00,f01,f02,f03,f04,f05,f10,f11,f12,f13,f14,f15,f20,f21,f22,f23,f24,f25;
    private Button calcButton;
    private ImageButton GPSbutton;
    private EditText edit;
    private EditText viewX;
    private TableLayout table;
    private  EditText viewY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        TextView t = findViewById(R.id.title_text);
        t.setText(Utils.getSelectedText(context, selected));
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this)); // this — это Activity context
        }
        calcButton = findViewById(R.id.calx_btn);
        GPSbutton = findViewById(R.id.gps_button);
        edit = findViewById(R.id.edit_var);
        viewX = findViewById(R.id.x);
        viewY = findViewById(R.id.y);

        table = findViewById(R.id.khaki_table);

        //поля таблицы
        f00 = table.findViewById(R.id.f00);
        f01 = table.findViewById(R.id.f01);
        f02 = table.findViewById(R.id.f02);
        f03 = table.findViewById(R.id.f03);
        f04 = table.findViewById(R.id.f04);
        f05 = table.findViewById(R.id.f05);
        f10 = table.findViewById(R.id.f10);
        f11 = table.findViewById(R.id.f11);
        f12 = table.findViewById(R.id.f12);
        f13 = table.findViewById(R.id.f13);
        f14 = table.findViewById(R.id.f14);
        f15 = table.findViewById(R.id.f15);
        f20 = table.findViewById(R.id.f20);
        f21 = table.findViewById(R.id.f21);
        f22 = table.findViewById(R.id.f22);
        f23 = table.findViewById(R.id.f23);
        f24 = table.findViewById(R.id.f24);
        f25 = table.findViewById(R.id.f25);

        viewX.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                xStringFormatted = viewX.getText().toString().trim();
            }
        });
        viewY.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                yStringFormatted = viewY.getText().toString().trim();
            }
        });


        //на главный экран
        ImageButton home = findViewById(R.id.home_button);
        home.setOnClickListener(v -> {
            ClearData();
            Intent intent = new Intent(MainActivity6.this, StartActivity.class);
            startActivity(intent);
        });

        GPSbutton.setOnClickListener(v -> getGPS());

        calcButton.setOnClickListener(v -> {


                try{
                    xStringFormatted = viewX.getText().toString().trim();
                    yStringFormatted = viewY.getText().toString().trim();

                    if(!CheckFormat(yStringFormatted) || !CheckFormat(xStringFormatted)){
                        DisplayError("Вы ввели не семизначные числа в поле ввода \"Позиция\", введите их корректно",context, this);
                        return;
                    }
                    //2.заполняем таблицу
                    //но сначала надо убедиться что третья переменная тоже введена
                    if (variable == null||variable.isEmpty()) variable = String.valueOf(edit.getText());
                    if (edit.getText() == null){
                        DisplayError("Перед расчётом необходимо ввести третью переменную", context, this);
                        return;
                    }
                    variable=edit.getText().toString();
                    if (variable == null || variable.isEmpty()){
                        DisplayError("Перед расчётом необходимо ввести третью переменную", context, this);
                    return;
                    }


                    if (xStringFormatted==null||yStringFormatted==null)
                    {
                        DisplayError("Перед расчётом необходимо определить местоположение при помощи зеленой кнопки в углу или ввести координаты вручную",context, this);
                        return;
                    }
                    if (xStringFormatted.isEmpty() || yStringFormatted.isEmpty()){
                        DisplayError("Что-то введено не так!",context, this);
                        return;
                    }


                    if (variable.isEmpty()){
                        DisplayError("Перед расчётом необходимо ввести третью переменную",context,this);
                        return;
                    }
                    if ((!variable.contains("-") && variable.length()!=4) || (variable.contains("-")&&variable.length()!=5)){
                        DisplayError("Неверный формат третьей переменной",context,this);
                        return;
                    }
                }
                catch (Exception e) {
                    DisplayError(e.toString(),context,this);
                    return;
                }



            //сделаем таблицу а потом фигачим в ту что на телефоне
            Integer[][] result = new Integer[3][6];
            try {
                result = Formula.calculate(xStringFormatted, yStringFormatted, variable, Utils.getNumber(StartActivity.selected));
            }
            catch (Exception e) {
                DisplayError(e.toString(),context,this);
            }
            result = Formula.calculate(xStringFormatted, yStringFormatted, variable, Utils.getNumber(selected));
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



        });

    }
    @Override
    protected void onDestroy() {
        Utils.RememberIn(this, context, table, this.findViewById(android.R.id.content), R.id.x,R.id.y, R.id.edit_var);
        super.onDestroy();

    }
    private void ClearData() {
        // Очистка координат и переменной
        edit.setText("");
        viewX.setText("");
        viewY.setText("");

        // Очистка таблицы
        f00.setText("");
        f01.setText("");
        f02.setText("");
        f03.setText("");
        f04.setText("");
        f05.setText("");

        f10.setText("");
        f11.setText("");
        f12.setText("");
        f13.setText("");
        f14.setText("");
        f15.setText("");

        f20.setText("");
        f21.setText("");
        f22.setText("");
        f23.setText("");
        f24.setText("");
        f25.setText("");

        variable = null;
        xStringFormatted = null;
        yStringFormatted = null;
        endx=null;
        endy=null;
    }
    private void getGPS() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final boolean[] ready = new boolean[]{false};
        ShowSpinnerDialog(ready,context, this);
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
                    String[] result = new String[2];
                    try {
                        Python py = Python.getInstance();

                        //люто питоним!!
                        PyObject module = py.getModule("convert");  // имя файла без .py
                        //мы включили жпс но он вставил то что мы захрадкодили а не то что реальное
                        PyObject pyresult = module.callAttr("convert", lat, lon);
                        String resStr = pyresult.toString();
                        result = resStr.replace("[", "").replace("]", "").split(",");
                    } catch (Exception e) {
                        DisplayError(e.toString(),context,this);
                        return;
                    }



                    double x = Double.parseDouble(result[0]);
                    double y = Double.parseDouble(result[1]);
                    String xString = String.valueOf(x);
                    String yString = String.valueOf(y);



                        xStringFormatted = FormatCoord(xString);
                        yStringFormatted = FormatCoord(yString);
                        viewX.setText(xStringFormatted);
                        viewY.setText(yStringFormatted);



                    ready[0] = true;
                });

    }



}