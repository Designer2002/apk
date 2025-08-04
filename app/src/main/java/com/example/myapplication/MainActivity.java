package com.example.myapplication;
import android.Manifest;

import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.google.gson.Gson;
import android.annotation.SuppressLint;
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


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private static double lat;
    private static double lon;
private  final  String msg = "Для расчёта реперной точки нужна стартовая позиция. Она либо введена некорректно, либо не введена вовсе. Работать не будет";
    private static double att;
    private int position = Integer.MAX_VALUE / 2;
    private static String yStringFormatted, x2StringFormatted;
    private static String variable;
    private static  String xStringFormatted, y2StringFormatted, endx, endy;

    private View rapper, geo, geo2;
    private  TextView f00,f01,f02,f03,f04,f05,f10,f11,f12,f13,f14,f15,f20,f21,f22,f23,f24,f25;
    private Button calcButton;
    private  EditText edit;
    private int selected = -1;
    private  ImageButton gps2;

    private EditText viewX;
    private TableLayout table;
    private  EditText viewY;
    private EditText viewX2, viewY2;
    private  TextView angleView, sView;

    private static String FormatCoord(String coordinate) {
        double number = Double.parseDouble(coordinate);
        return String.valueOf(Math.round(number)).substring(0, 7);
    }








    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        //switchTheme(2);

        setContentView(R.layout.activity_main);
        ShowChooseDialog();


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this)); // this — это Activity context
        }
        //ShowChooseDialog();
        ImageButton GPSbutton = findViewById(R.id.gps_button);


        edit = findViewById(R.id.edit_var);
        viewX = findViewById(R.id.x);
        viewY = findViewById(R.id.y);
        viewX2 = findViewById(R.id.x2);
        viewY2=findViewById(R.id.y2);




        table = findViewById(R.id.khaki_table);
        geo2 = findViewById(R.id.geo_ext_2);

        rapper = findViewById(R.id.rapper_ext);
        geo = findViewById(R.id.geo_ext);
        angleView = findViewById(R.id.angle);
        sView = findViewById(R.id.s);

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
        viewX2.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                 endx = viewX2.getText().toString().trim();
            }
        });
        viewY2.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                endy = viewY2.getText().toString().trim();
            }
        });


        //на главный экран
        ImageButton home = findViewById(R.id.home_button);
         home.setOnClickListener(v -> {
             ClearData();
            ShowChooseDialog();
         });

        calcButton = findViewById(R.id.calx_btn);







//        //загрузить прошлое состояние и проверить включена ли запоминалка
//        SharedPreferences prefs = getSharedPreferences("checkbox_prefs", MODE_PRIVATE);
//        boolean isChecked = prefs.getBoolean("checkbox_state", false);
//
//        CheckBox box = findViewById(R.id.load_checkbox);
//        box.setChecked(isChecked);
//        if (box.isChecked()){
//            RememberOut(table);
//        }
//        box.setOnCheckedChangeListener((buttonView, isNowChecked) -> {
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("checkbox_state", isNowChecked);
//            editor.apply();
//        });
        gps2 = findViewById(R.id.gps2_button);
        gps2.setOnClickListener(v->getGPS(1));

        ImageButton cleanBtn = findViewById(R.id.clear_button);
        cleanBtn.setOnClickListener(v -> {
            ClearData();
        });

        GPSbutton.setOnClickListener(v -> {
            getGPS(0);
                });

        calcButton.setOnClickListener(v -> {
//если есть реперная точка!
            if (selected == 1 || selected == 3) {
                String startx, starty;

                    startx = viewX.getText().toString().trim();
                    starty = viewY.getText().toString().trim();
                    endx = viewX2.getText().toString().trim();
                    endy = viewY2.getText().toString().trim();

                if(endx == "" || endy == "" || startx == "" || starty == ""){
                    DisplayError("Данных для расчёта не хватает! Введите данные корректно и попробуйте снова");
                    return;
                }

                String[] result;
                try {
                    Python py = Python.getInstance();

                    //люто питоним снова!!
                    PyObject module = py.getModule("geo");  // имя файла без .py
                    PyObject pyresult = module.callAttr("inverse_geo", startx, starty, endx, endy);
                    String resStr = pyresult.toString();
                    result = resStr.replace("[", "").replace("]", "").split(",");
                    variable = fillVrlms(result[1]);
                    edit.setText(variable);
                    angleView.setText(result[1]);
                    sView.setText(result[0]);

                } catch (Exception e) {
                    DisplayError(e.toString());
                    return;
                }
            }



            else{
                try{
                    xStringFormatted = viewX.getText().toString().trim();
                    yStringFormatted = viewY.getText().toString().trim();

                    if(!CheckFormat(yStringFormatted) || !CheckFormat(xStringFormatted)){
                        DisplayError("Вы ввели не семизначные числа в поле ввода \"Позиция\", введите их корректно");
                    }
                    //2.заполняем таблицу
                    //но сначала надо убедиться что третья переменная тоже введена
                    if (variable == null||variable.isEmpty()) variable = String.valueOf(edit.getText());
                    if (edit.getText() == null){
                        DisplayError("Перед расчётом необходимо ввести третью переменную");
                        return;
                    }
                    variable=edit.getText().toString();
                    if (variable == null || variable.isEmpty()){
                        DisplayError("Перед расчётом необходимо ввести третью переменную");
                    }


                    if (xStringFormatted==null||yStringFormatted==null)
                    {
                        DisplayError("Перед расчётом необходимо определить местоположение при помощи зеленой кнопки в углу или ввести координаты вручную");
                        return;
                    }
                    if (xStringFormatted.isEmpty() || yStringFormatted.isEmpty()){
                        DisplayError("Что-то введено не так!");
                        return;
                    }


                    if (variable.isEmpty()){
                        DisplayError("Перед расчётом необходимо ввести третью переменную");
                        return;
                    }
                    if ((!variable.contains("-") && variable.length()!=4) || (variable.contains("-")&&variable.length()!=5)){
                        DisplayError("Неверный формат третьей переменной");
                        return;
                    }
                }
                    catch (Exception e) {
                                            DisplayError(e.toString());
                                        }
                }


                //сделаем таблицу а потом фигачим в ту что на телефоне
                Integer[][] result = Formula.calculate(xStringFormatted, yStringFormatted, variable, getNumber(selected));
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

    private void getGPS(int i) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final boolean[] ready = new boolean[]{false};
        ShowSpinnerDialog(ready);
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
                        DisplayError(e.toString());
                        return;
                    }



                    double x = Double.parseDouble(result[0]);
                    double y = Double.parseDouble(result[1]);
                    String xString = String.valueOf(x);
                    String yString = String.valueOf(y);

                    if(i == 0){

                        xStringFormatted = FormatCoord(xString);
                        yStringFormatted = FormatCoord(yString);
                        viewX.setText(xStringFormatted);
                        viewY.setText(yStringFormatted);

                    }
                    else{
                        x2StringFormatted = FormatCoord(xString);
                        y2StringFormatted = FormatCoord(yString);
                        viewX2.setText(x2StringFormatted);
                        viewY2.setText(y2StringFormatted);
                    }

                    ready[0] = true;
                });

    }


    private int getNumber(int selected) {
        switch (selected){
            case 1:
                return 7;
            case 2:
                return 6;
            case 3:
                return 6;
            case 4:
                return 5;
            default:
                break;
        }
        return -1;
    }

    private void ClearData() {
        // Очистка координат и переменной
        edit.setText("");
        viewX.setText("");
        viewY.setText("");
        viewX2.setText("");
        viewY2.setText("");

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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void ShowChooseDialog() {
        List<String> options = Arrays.asList(
                getString(R.string.default_rap),
                getString(R.string.r7),
                getString(R.string.r6),
                getString(R.string.r62),
                getString(R.string.r5)
        );

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

        RecycleViewAdapter adapter = new RecycleViewAdapter(recyclerView, options, index -> {
            selected = index % options.size(); // по mod вернуть настоящий индекс
            //handler.removeCallbacks(scrollRunnable);
        });
        recyclerView.setAdapter(adapter);

        Button continueBtn = view.findViewById(R.id.continue_button);
        continueBtn.setOnClickListener(v -> {
            if (selected!=-1){
                dialog.dismiss();
                TextView t = findViewById(R.id.title_text);
                t.setText(getSelectedText(selected));
                if(selected == 0){
                    Intent intent = new Intent(MainActivity.this, ZoomActivity.class);
                    startActivity(intent);
                }
                View gps2l = findViewById(R.id.gps2l);
                if (selected == 3 || selected == 1) {
                    gps2l.setVisibility(View.VISIBLE);
                    edit.setFocusable(false);
                    edit.setFocusableInTouchMode(false);
                    edit.clearFocus();
                    edit.setBackground(getDrawable(R.drawable.input_button_locked));
                    geo.setVisibility(View.VISIBLE);
                    geo2.setVisibility(View.VISIBLE);
                    rapper.setVisibility(View.VISIBLE);
                } else {
                    gps2l.setVisibility(View.GONE);
                    edit.setFocusable(true);
                    edit.setFocusableInTouchMode(true);
                    edit.setBackground(getDrawable(R.drawable.input_button));
                    geo.setVisibility(View.GONE);
                    geo2.setVisibility(View.GONE);
                    rapper.setVisibility(View.GONE);
                }
                if(selected==0){

                }
                //switchTheme(selected);
            }

        });
        // Запретить закрытие при нажатии вне диалога
        dialog.setCanceledOnTouchOutside(false);

// Запретить закрытие при нажатии кнопки Назад
        dialog.setCancelable(false);
        dialog.show();
    }

    private String fillVrlms(String s) {
        Double d = Double.parseDouble(s);
        long l = Math.round(d);
        l *= 10;
        StringBuilder sBuilder = new StringBuilder(String.valueOf(l));
        while(sBuilder.length()<4){
        sBuilder.insert(0, "0");
        }
        s = sBuilder.toString();
        return  s.replace(" ", "");
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
            DisplayError("Записать прошлый расчёт не удалось");
        }

    }

    private String getSelectedText(int sel){
        switch (sel){
            case 1:
                return getString(R.string.r7);
            case 2:
                return getString(R.string.r6);
            case 3:
                return getString(R.string.r62);
            case 4:
                return getString(R.string.r5);
            default:
                break;
        }
        return "";
    }
    private void ShowSpinnerDialog(boolean[] ready){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.spinner_layout, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        LockOrient(dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        // Проверка состояния переменной
        Handler handler = new Handler();
        Runnable checkFlag = new Runnable() {
            @Override
            public void run() {
                if (ready[0]) {
                    dialog.dismiss();
                } else {
                    handler.postDelayed(this, 300); // Проверять каждые 300 мс
                }
            }
        };

        handler.post(checkFlag);

    }

    private void  DisplayError(String string){

            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.error_layout, null);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();
        LockOrient(dialog);
        TextView error = view.findViewById(R.id.error_message);
        error.setText(string);
Button close = view.findViewById(R.id.close);
close.setOnClickListener(v -> dialog.dismiss());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();


    }
    private void LockOrient(Dialog dialog){
        int originalOrientation = getRequestedOrientation();

// Заблокируем в текущей ориентации
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        dialog.setOnDismissListener(d -> {
            // Вернем назад
            setRequestedOrientation(originalOrientation);
        });

        dialog.show();

    }

}
