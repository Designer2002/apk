package com.example.myapplication;
import android.Manifest;

import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import com.chaquo.python.Python;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Handler;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private static double lat;
    private static double lon;
    private static double att;
    private int position = Integer.MAX_VALUE / 2;
    private static String yStringFormatted;
    private static  String xStringFormatted;
    private  TextView f00,f01,f02,f03,f04,f05,f10,f11,f12,f13,f14,f15,f20,f21,f22,f23,f24,f25;
    private Button calcButton;
    private  EditText edit;
    private int selected = -1;
    private TextView viewX;
    private TableLayout table;
    private  TextView viewY;
    private TextView rapperVal;
    private Button rapper;
    private TextView errorView, errorView2;
    private static String FormatCoord(String coordinate) {
        double number = Double.parseDouble(coordinate);
        int multiplier = coordinate.indexOf('.') <= 2 ? 100000 : 10000;
        int result = (int) Math.round(number * multiplier);
        String resStr =String.valueOf(result);
        return resStr.length() > 7 ? resStr.substring(0, 6) : resStr;
    }
    private void DisplayError(String text, TextView errorView){
        errorView.setText(text);
        errorView.setVisibility(View.VISIBLE);
        errorView.postDelayed(new Runnable() {
            public void run() {
                errorView.setVisibility(View.GONE);
            }
        }, 6000);
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
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this)); // this — это Activity context
        }
        ShowChooseDialog();
        ImageButton button = findViewById(R.id.gps_button);
        errorView = findViewById(R.id.error_message);

        edit = findViewById(R.id.edit_var);
        viewX = findViewById(R.id.x);
        viewY = findViewById(R.id.y);
        table = findViewById(R.id.khaki_table);
        LinearLayout spinner = findViewById(R.id.spinner_layout);
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

        //на главный экран
        ImageButton home = findViewById(R.id.home_button);
         home.setOnClickListener(v -> {
            ShowChooseDialog();
         });

        calcButton = findViewById(R.id.calx_btn);
        ImageButton hand_input = findViewById(R.id.hand_button);
        hand_input.setOnClickListener(v -> {
            showCoordDialog();
        });

        //rapper btn
         rapper = findViewById(R.id.rapper_btn);
        rapperVal = findViewById(R.id.rapper_value);

        rapper.setOnClickListener(v -> {
            if (CheckFormat(viewX.getText().toString()) && CheckFormat(viewY.getText().toString()))
                showRapperDialog();
            else{
                DisplayError("Для расчёта реперной точки нужна стартовая позиция. Она либо введена некорректно, либо не введена вовсе. Работать не будет", errorView);
            }
        });


        //загрузить прошлое состояние и проверить включена ли запоминалка
        SharedPreferences prefs = getSharedPreferences("checkbox_prefs", MODE_PRIVATE);
        boolean isChecked = prefs.getBoolean("checkbox_state", false);

        CheckBox box = findViewById(R.id.load_checkbox);
        box.setChecked(isChecked);
        if (box.isChecked()){
            RememberOut(table);
        }
        box.setOnCheckedChangeListener((buttonView, isNowChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("checkbox_state", isNowChecked);
            editor.apply();
        });
        ImageButton cleanBtn = findViewById(R.id.clear_button);
        cleanBtn.setOnClickListener(v -> {
            ClearData();
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
                        calcButton.setEnabled(true);
                        calcButton.setBackgroundColor(R.color.khaki_secondary);

                        return;
                    }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Без GPS работать не будет. Необходимо включить его", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                calcButton.setEnabled(true);
                calcButton.setBackgroundColor(R.color.khaki_secondary);

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
String[] result = new String[2];
                                try {
                                    Python py = Python.getInstance();

                                    //люто питоним!!
                                    PyObject module = py.getModule("convert");  // имя файла без .py
                                    PyObject pyresult = module.callAttr("convert", lat, lon);
                                    String resStr = pyresult.toString();
                                    result = resStr.replace("[", "").replace("]", "").split(",");
                                } catch (Exception e) {
                                    DisplayError(e.toString(), errorView);
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
                if (xStringFormatted==null||yStringFormatted==null)
                {
                    DisplayError("Перед расчётом необходимо определить местоположение при помощи зеленой кнопки в углу или ввести координаты вручную",errorView);
                    return;
                }
                if (xStringFormatted.isEmpty() || yStringFormatted.isEmpty()){
                    DisplayError("Что-то введено не так!",errorView);
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


    }

    private void ClearData() {
        // Очистка координат и переменной
        edit.setText("");
        viewX.setText(R.string.x_coord);
        viewY.setText(R.string.y_coord);

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
    }


    private void RememberOut(TableLayout table){
        //подгружаю предыдущий расчет если он есть

        SharedPreferences prefs = getSharedPreferences("temp_data", MODE_PRIVATE);
try {


    String xString = prefs.getString("x_value", null);
    String yString = prefs.getString("y_value", null);
    String variable = prefs.getString("variable", null);
    String resultArray = prefs.getString("result", null);

    if (xString != null) {
        viewX.setText(xString);
    }
    if (yString != null) {
        viewY.setText(yString);
    }
    if (variable != null) {
        edit.setText(variable);
    }
    if (resultArray != null) {
        Gson gson = new Gson();
        String[] result = gson.fromJson(resultArray, String[].class);

        //поля таблицы

        f00.setText(result[0]);
        f01.setText(result[1]);
        f02.setText(result[2]);
        f03.setText(result[3]);
        f04.setText(result[4]);
        f05.setText(result[5]);
        f10.setText(result[6]);
        f11.setText(result[7]);
        f12.setText(result[8]);
        f13.setText(result[9]);
        f14.setText(result[10]);
        f15.setText(result[11]);
        f20.setText(result[12]);
        f21.setText(result[13]);
        f22.setText(result[14]);
        f23.setText(result[15]);
        f24.setText(result[16]);
        f25.setText(result[17]);
        xStringFormatted=xString;
        yStringFormatted =yString;



    }
}catch (Exception e){
    DisplayError("Не удалось подгрузить данные с последнего расчёта", errorView);
    //DisplayError(e.toString(), errorView);

}
    }


    private void ShowChooseDialog() {
        List<String> options = Arrays.asList(
                getString(R.string.r7),
                getString(R.string.r6),
                getString(R.string.r62),
                getString(R.string.r5)
        );

//        // Повторим данные чтобы было как будто бесконечно
//        List<String> infiniteOptions = new ArrayList<>();
//        for (int i = 0; i < 100; i++) infiniteOptions.addAll(options);
//        // Старт с середины
        //int initialPos = infiniteOptions.size() / 2;
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.start_window, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        Handler handler = new Handler();
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        Runnable scrollRunnable = new Runnable() {
//            int position = initialPos;
//
//            @Override
//            public void run() {
//                position++;
//                recyclerView.scrollBy(0, 2);  // скроллим на 1 пиксель вниз
//                handler.postDelayed(this, 8);
//            }
//        };
        ThemeAdapter adapter = new ThemeAdapter(recyclerView, options, index -> {
            selected = index % options.size(); // по mod вернуть настоящий индекс
            //handler.removeCallbacks(scrollRunnable);
        });
        recyclerView.setAdapter(adapter);
//
//
//        recyclerView.scrollToPosition(initialPos);
//
//

        //handler.postDelayed(scrollRunnable, 500); // подождать чуть перед стартом
        // Кнопка "Continue"
        Button continueBtn = view.findViewById(R.id.continue_button);
        continueBtn.setOnClickListener(v -> {
            if (selected!=-1){
                dialog.dismiss();
                if (selected == 2){
                    rapper.setVisibility(View.VISIBLE);
                    rapperVal.setVisibility(View.VISIBLE);
                }
                TextView t = findViewById(R.id.title_text);
                t.setText(getSelectedText(selected));
            }

        });
        // Запретить закрытие при нажатии вне диалога
        dialog.setCanceledOnTouchOutside(false);

// Запретить закрытие при нажатии кнопки Назад
        dialog.setCancelable(false);
        dialog.show();
    }


    private void showRapperDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.rapper_input, null);
        errorView2 = view.findViewById(R.id.error_message2);
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
                try{
                    Double test = Double.parseDouble(x);
                    test = Double.parseDouble(y);
                }
                catch (Exception e){
                    DisplayError("Неверный формат введеных координат", errorView2);
                    rapperVal.setText(getString(R.string.undefined));
                    return;
                }
                String[] result;
                try {
                    Python py = Python.getInstance();

                    //люто питоним снова!!
                    PyObject module = py.getModule("geo");  // имя файла без .py
                    PyObject pyresult = module.callAttr("inverse_geo", viewX.getText().toString(), viewY.getText().toString(), x, y);
                    String resStr = pyresult.toString();
                    result = resStr.replace("[", "").replace("]", "").split(",");
                    rapperVal.setText("УГОЛ: " +result[1] + "\nРАССТОЯНИЕ: " + result[0]);
                } catch (Exception e) {
                    DisplayError(e.toString(), errorView);
                    return;
                }
                dialog.dismiss();
            }
            catch (Exception e){
                DisplayError("Данные введены неккоретно!", errorView);
            }
        });

        dialog.show();
    }

    private void showCoordDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.input_alert, null);
        errorView2 = view.findViewById(R.id.error_message2);
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
                    DisplayError("Неверный формат введеных координат", errorView2);
                    viewX.setText(getString(R.string.x_coord));
                    viewY.setText(getString(R.string.y_coord));
                    return;
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



    @Override
    protected void onDestroy() {
        RememberIn(table);
        super.onDestroy();
    }

    private void RememberIn(TableLayout table){
        Gson gson = new Gson();
        String[] res = new String[18];
        res[0] = ((TextView) table.findViewById(R.id.f00)).getText().toString();
        res[1] = ((TextView) table.findViewById(R.id.f01)).getText().toString();
        res[2] = ((TextView) table.findViewById(R.id.f02)).getText().toString();
        res[3] = ((TextView) table.findViewById(R.id.f03)).getText().toString();
        res[4] = ((TextView) table.findViewById(R.id.f04)).getText().toString();
        res[5] = ((TextView) table.findViewById(R.id.f05)).getText().toString();
        res[6] = ((TextView) table.findViewById(R.id.f10)).getText().toString();
        res[7] = ((TextView) table.findViewById(R.id.f11)).getText().toString();
        res[8] = ((TextView) table.findViewById(R.id.f12)).getText().toString();
        res[9] = ((TextView) table.findViewById(R.id.f13)).getText().toString();
        res[10] = ((TextView) table.findViewById(R.id.f14)).getText().toString();
        res[11] = ((TextView) table.findViewById(R.id.f15)).getText().toString();
        res[12] = ((TextView) table.findViewById(R.id.f20)).getText().toString();
        res[13] = ((TextView) table.findViewById(R.id.f21)).getText().toString();
        res[14] = ((TextView) table.findViewById(R.id.f22)).getText().toString();
        res[15] = ((TextView) table.findViewById(R.id.f23)).getText().toString();
        res[16] = ((TextView) table.findViewById(R.id.f24)).getText().toString();
        res[17] = ((TextView) table.findViewById(R.id.f25)).getText().toString();



        String json = gson.toJson(res);
        //TextView test = findViewById(R.id.test);
        //test.setText(json);
        SharedPreferences prefs = getSharedPreferences("temp_data", MODE_PRIVATE);

        try{
            prefs.edit().putString("result", json).apply();

            if(viewX.getText()!=null)prefs.edit().putString("x_value", viewX.getText().toString()).apply();
            if(viewY.getText()!=null)prefs.edit().putString("y_value", viewY.getText().toString()).apply();
            if(edit.getText()!=null)prefs.edit().putString("variable", edit.getText().toString()).apply();
        }
        catch (Exception e){
            DisplayError("Записать прошлый расчёт не удалось", errorView);
        }

    }

    private String getSelectedText(int sel){
        switch (sel){
            case 0:
                return getString(R.string.r7);
            case 1:
                return getString(R.string.r6);
            case 2:
                return getString(R.string.r62);
            case 3:
                return getString(R.string.r5);
            default:
                break;
        }
        return "";
    }
}
