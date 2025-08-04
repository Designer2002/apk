package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.Settings.Global.getString;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class Utils {
    public static String FormatCoord(String coordinate) {
        double number = Double.parseDouble(coordinate);
        return String.valueOf(Math.round(number)).substring(0, 7);
    }
    public static  String fillVrlms(String s) {
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

    public static boolean CheckFormat(String coord){
        if (coord.contains("-")){
            return coord.length()==8;
        }
        else{
            return coord.length()==7;
        }
    }
    public static void RememberIn(Activity a, Context c, TableLayout table, View view, int idX, int idY, int idVar){
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
        SharedPreferences prefs = c.getSharedPreferences("temp_data", MODE_PRIVATE);

        try{
            prefs.edit().putString("result", json).apply();
            TextView viewX = view.findViewById(idX);
            TextView viewY = view.findViewById(idY);
            EditText edit = view.findViewById(idVar);
            if(viewX.getText()!=null)prefs.edit().putString("x_value", viewX.getText().toString()).apply();
            if(viewY.getText()!=null)prefs.edit().putString("y_value", viewY.getText().toString()).apply();
            if(edit!=null&&edit.getText()!=null)prefs.edit().putString("variable", edit.getText().toString()).apply();
        }
        catch (Exception e){
            DisplayError("Записать прошлый расчёт не удалось", c, a);
        }

    }
    public static void RememberIn(Activity a, Context context, TableLayout table, View view, int idX, int idY, int idX2, int idY2, int idAngle, int idAngle2, int idS){
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
        SharedPreferences prefs = context.getSharedPreferences("temp_data", MODE_PRIVATE);

        try{
            prefs.edit().putString("result", json).apply();
            TextView viewX = view.findViewById(idX);
            TextView viewY = view.findViewById(idY);
            TextView viewX2 = view.findViewById(idX2);
            TextView viewY2 = view.findViewById(idY2);
            TextView viewA = view.findViewById(idAngle);
            TextView viewA2 = view.findViewById(idAngle2);
            TextView viewS = view.findViewById(idS);
            if(viewX.getText()!=null)prefs.edit().putString("x_value", viewX.getText().toString()).apply();
            if(viewY.getText()!=null)prefs.edit().putString("y_value", viewY.getText().toString()).apply();
            if(viewX2.getText()!=null)prefs.edit().putString("x2_value", viewX.getText().toString()).apply();
            if(viewY2.getText()!=null)prefs.edit().putString("y2_value", viewY.getText().toString()).apply();
            if(viewA.getText()!=null)prefs.edit().putString("a_value", viewX.getText().toString()).apply();
            if(viewA2.getText()!=null)prefs.edit().putString("a2_value", viewY.getText().toString()).apply();
            if(viewS.getText()!=null)prefs.edit().putString("s_value", viewX.getText().toString()).apply();

        }
        catch (Exception e){
            Utils.DisplayError("Записать прошлый расчёт не удалось", context, a);
        }

    }
    public static  void ShowSpinnerDialog(boolean[] ready, Context context,Activity a){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.spinner_layout, null);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        Utils.LockOrient(dialog, a);
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

    static  void  DisplayError(String string, Context context, Activity activity){

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.error_layout, null);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        Utils.LockOrient(dialog, activity);
        TextView error = view.findViewById(R.id.error_message);
        error.setText(string);
        Button close = view.findViewById(R.id.close);
        close.setOnClickListener(v -> dialog.dismiss());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();


    }
    public static void LockOrient(Dialog dialog,Activity activity){
        int originalOrientation = activity.getRequestedOrientation();

// Заблокируем в текущей ориентации
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        dialog.setOnDismissListener(d -> {
            // Вернем назад
            activity.setRequestedOrientation(originalOrientation);
        });

        dialog.show();

    }
    public static String getSelectedText(Context context, int sel){
        switch (sel){
            case 1:
                return context.getString(R.string.r7);
            case 2:
                return context.getString(R.string.r6);
            case 3:
                return context.getString(R.string.r62);
            case 4:
                return context.getString(R.string.r5);
            default:
                break;
        }
        return "";
    }
    public static int getNumber(int selected) {
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
    public static String ConvertToSystem(String angle){
        return angle.substring(0,2)+"-"+angle.substring(2,4);
    }


}
