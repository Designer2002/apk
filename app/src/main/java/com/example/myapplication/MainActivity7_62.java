package com.example.myapplication;

import static com.example.myapplication.SeekerInterop.createSegment;
import static com.example.myapplication.StartActivity.selected;
import static com.example.myapplication.Utils.CheckFormat;
import static com.example.myapplication.Utils.CheckFormatY;
import static com.example.myapplication.Utils.DisplayError;
import static com.example.myapplication.Utils.FormatCoord;
import static com.example.myapplication.Utils.ShowSpinnerDialog;
import static com.example.myapplication.Utils.timeoutVal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import dev.vivvvek.seeker.Segment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity7_62 extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private static double lat;
    private static double lon;
    private static double att;
    private int position = Integer.MAX_VALUE / 2;
    private static String yStringFormatted, x2StringFormatted;
    private static String variable;
    private static String xStringFormatted, y2StringFormatted, endx, endy, startx, starty;
    private TextView f00, f01, f02, f03, f04, f05, f10, f11, f12, f13, f14, f15, f20, f21, f22, f23, f24, f25;
    private Button calcButton;
    private ImageButton gps2, GPSbutton;
    private ImageButton timeout;
    private final Context context = MainActivity7_62.this;

    private EditText viewX;
    private TableLayout table;
    private EditText viewY;
    private EditText viewX2, viewY2;
    private TextView viewA, viewA2, viewS;

    private void DisplayTimeout() {
        View view = findViewById(R.id.timeout_dialog);
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
        view.setFocusable(true);
        view.requestFocus();
        view.bringToFront();
        view.invalidate();
        ComposeView composeView = findViewById(R.id.slider);
        List<Segment> segments = Arrays.asList(
                createSegment("Очень хороший приём (4-6 секунд)", 4.0f),
                createSegment("Хороший приём (6-10 секунд)", 6.0f),
                createSegment("Обычный приём (10-15 секунд)", 10.0f),
                createSegment("Слабый приём (15-20 секунд)", 15.0f),
                createSegment("Очень слабый приём (20-30 секунд)", 20.0f)
        );
        AtomicReference<Float> selectedTimeout = new AtomicReference<>(7.0f); // стартовое значение
        SeekerInterop.setSeekerContent(
                MainActivity7_62.this,
                composeView,
                timeoutVal != -1 ? timeoutVal : 7f,
                30,
                segments,
                new Function1<Float, Unit>() {
                    @Override
                    public Unit invoke(Float newValue) {
                        selectedTimeout.set(newValue);
                        return Unit.INSTANCE;
                    }
                }
        );
        Log.d("TIMEOUT", String.valueOf(timeoutVal));
        Button okBtn = findViewById(R.id.savetimeout);
        okBtn.setOnClickListener(v -> {
            SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
            prefs.edit().putFloat("gps_timeout", selectedTimeout.get()).apply();
            view.setVisibility(View.GONE);
            view.setClickable(false);
            view.setFocusable(false);
            timeoutVal =  selectedTimeout.get();
        });

    }

    void CheckTimeout() {

        SharedPreferences prefs = context.getSharedPreferences("settings", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.remove("gps_timeout");
//        editor.apply();
        float timeoutF = prefs.getFloat("gps_timeout", -1.0f);
        if (timeoutF == -1.0f) {
            DisplayTimeout();
        } else timeoutVal = timeoutF;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7_62);
        TextView t = findViewById(R.id.title_text);
        t.setText(Utils.getSelectedText(context, selected));

        if (selected == -1){
            Intent intent = new Intent(MainActivity7_62.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this)); // this — это Activity context
        }
        ImageButton home = findViewById(R.id.home_button);
        home.setOnClickListener(v -> {
            ClearData();
            Intent intent = new Intent(MainActivity7_62.this, StartActivity.class);
            startActivity(intent);
            finish();
        });
        CheckTimeout();
        calcButton = findViewById(R.id.calx_btn);
        timeout = findViewById(R.id.timeout_button);
        timeout.setOnClickListener(v -> {
            DisplayTimeout();
        });
        gps2 = findViewById(R.id.gps2_button);
        GPSbutton = findViewById(R.id.gps_button);
        viewX = findViewById(R.id.x);
        viewY = findViewById(R.id.y);
        viewX2 = findViewById(R.id.x2);
        viewY2 = findViewById(R.id.y2);
        viewA = findViewById(R.id.angle);
        viewA2 = findViewById(R.id.angle2);
        viewS = findViewById(R.id.s);

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

        ImageButton cleanBtn = findViewById(R.id.clear_button);
        cleanBtn.setOnClickListener(v -> {
            ClearData();
        });

        GPSbutton.setOnClickListener(v -> {
            getGPS(0);
        });


        gps2.setOnClickListener(v -> {
            getGPS(1);
        });

        calcButton.setOnClickListener(v -> {

            //надо посчитать угол!
            endx = viewX2.getText().toString().trim();
            endy = viewY2.getText().toString().trim();
            startx = viewX.getText().toString().trim();
            starty = viewY.getText().toString().trim();

            if (endx == "" || endy == "" || startx == "" || starty == "") {
                DisplayError("Данных для расчёта не хватает! Введите данные корректно и попробуйте снова", context, this);
                return;
            }

            String[] resultTMP;
            try {
                Python py = Python.getInstance();

                //люто питоним снова!!
                PyObject module = py.getModule("geo");  // имя файла без .py
                PyObject pyresult = module.callAttr("inverse_geo", startx, starty, endx, endy);
                String resStr = pyresult.toString();
                if (resStr.equals("-1")){
                    DisplayError("Что-то введено не так!", context, this);
                    return;
                }
                resultTMP = resStr.replace("[", "").replace("]", "").split(",");
                variable = Utils.fillVrlms(resultTMP[1]);
                viewA.setText(resultTMP[1].split("\\.")[0]+"°");

                //п(р)итон опять!
                module = py.getModule("angle");
                pyresult = module.callAttr("uglomer", Float.valueOf(resultTMP[1].trim()));
                resStr = pyresult.toString();
                viewA2.setText(resStr);

                viewS.setText(resultTMP[0]);

            } catch (Exception e) {
                DisplayError(e.toString(), context, this);
                //DisplayError("Данных для расчёта не хватает! Введите данные корректно и попробуйте снова",context,this);
                return;
            }

            try {
                xStringFormatted = viewX.getText().toString().trim();
                yStringFormatted = viewY.getText().toString().trim();

                if (!CheckFormatY(yStringFormatted) || !CheckFormat(xStringFormatted)) {
                    DisplayError("Вы ввели числа в некоректном формате в поле ввода \"Позиция\", введите их правильно", context, this);
                    return;
                }
                //2.заполняем таблицу
                //но сначала надо убедиться что не ввели чушь


                if (xStringFormatted == null || yStringFormatted == null) {
                    DisplayError("Перед расчётом необходимо определить местоположение при помощи зеленой кнопки в углу или ввести координаты вручную", context, this);
                    return;
                }
                if (xStringFormatted.isEmpty() || yStringFormatted.isEmpty()) {
                    DisplayError("Что-то введено не так!", context, this);
                    return;
                }


                if (variable.isEmpty()) {
                    DisplayError("Перед расчётом необходимо ввести третью переменную", context, this);
                    return;
                }
                if ((!variable.contains("-") && variable.length() != 4) || (variable.contains("-") && variable.length() != 5)) {
                    DisplayError("Неверный формат третьей переменной", context, this);
                    return;
                }
            } catch (Exception e) {
                DisplayError(e.toString(), context, this);
                return;
            }


            //сделаем таблицу а потом фигачим в ту что на телефоне
            Integer[][] result = new Integer[3][6];
            try {
                result = Formula.calculate(xStringFormatted, yStringFormatted, variable, Utils.getNumber(StartActivity.selected));
            } catch (Exception e) {
                DisplayError(e.toString(), context, this);
                return;
            }

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
        Utils.RememberIn(this, context, table, this.findViewById(android.R.id.content), R.id.x, R.id.y, R.id.x2, R.id.y2, R.id.angle, R.id.angle2, R.id.s);
        super.onDestroy();

    }

    private void ClearData() {
        // Очистка координат и переменной
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
        endx = null;
        endy = null;
        viewA.setText("");
        viewA2.setText("");
        viewS.setText("");
    }
    private void getGPS(int i) {
        Log.d("GPS", "getGPS called");

        // Проверка разрешения
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "GPS не дали, работать не будет", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка, включен ли GPS
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Без GPS работать не будет. Включите его", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;
        }

        // Запрос с высокой точностью
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setMinUpdateIntervalMillis(500)
                .build();

        FusedLocationProviderClient fusedClient = LocationServices.getFusedLocationProviderClient(this);
        final boolean[] ready = {false};
        ShowSpinnerDialog(ready, context, MainActivity7_62.this);
        final Location[] location = {null};
        // Создаем колбэк
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Log.d("GPS", "onLocationResult called");

                location[0] = locationResult.getLastLocation();

                    if (location[0] != null) {
                        lat = location[0].getLatitude();
                        lon = location[0].getLongitude();
                        att = location[0].getAltitude();
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
                        DisplayError(e.toString(), context, MainActivity7_62.this);
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

                    // Остановить GPS при получении координат
                    fusedClient.removeLocationUpdates(this);
                }

        };

        // Таймер остановки через timeoutVal секунд
        Handler timeoutHandler = new Handler(Looper.getMainLooper());
        timeoutHandler.postDelayed(() -> {
            Log.d("GPS", "Timeout reached, stopping GPS");
            ready[0] = true;
            if(location[0] == null)DisplayError("За указанное время не удалось определить местоположение!", context, MainActivity7_62.this);
            fusedClient.removeLocationUpdates(locationCallback);
        }, (long)timeoutVal * 1000);

        // Запускаем получение координат
        fusedClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
    }


}