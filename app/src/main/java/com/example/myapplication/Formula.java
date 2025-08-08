package com.example.myapplication;

import java.util.Arrays;
import java.util.Scanner;

public class Formula {
    public static Boolean checkIfDataIsCorrect(String x, String y, String c){
        return x.length() == 7 && y.length() == 7 && c.length() == 4;
    }
    private static Boolean isXPositive(String x){
        return Integer.parseInt(x) > 0;
    }
    private static Boolean areTwoFirstDigitsOfYAnEvenNumber(String y){
        if(y.length()==8) return Integer.parseInt(y.substring(0, 2)) % 2 == 0;
        else return Integer.parseInt(y.substring(0, 1)) % 2 == 0;
    }
    private static String processNumber(String number){
        number = number.replace("-", "");
        //отбросить первые 2 цифры числа  и работать с оставшимися 5
        int i = number.length() == 8 ? 3:2;
        String last_five_digits = number.substring(i);
        String last_two = last_five_digits.substring(3);
        Integer rounded_last_two_as_number = Math.round(Integer.parseInt(last_two) / 10.0f);
        //возращаемое значение - первые ТРИ цифры оригинальные + ОКРУГЛЕННАЯ цифра минус НОЛЬ (оставшийся от откругления)
        String result = last_five_digits.substring(0, last_five_digits.length()-2)+rounded_last_two_as_number.toString();
        return result;
    }
    private static void makeRow(String processedNumber, Integer[][] res, Integer row){
        Integer num1 = Integer.parseInt(String.valueOf(processedNumber.charAt(0)));
        Integer num2 = Integer.parseInt(String.valueOf(processedNumber.charAt(1)));
        Integer num3 = Integer.parseInt(String.valueOf(processedNumber.charAt(2)));
        Integer num4 = Integer.parseInt(String.valueOf(processedNumber.charAt(3)));
        res[row][1] = num1;
        res[row][2] = num2;
        res[row][3] = num3;
        res[row][4] = num4;
        res[row][5] = (res[row][0]+num1+num2+num3+num4)%10;
    }
    public static Integer[][] calculate(String x, String y, String c, int NUMBER){
        Integer[][] res = new Integer[3][6];
        //если Х положительный то первое число первой строки НОЛЬ если нет ЕДИНИЦА
        res[0][0] = isXPositive(x) ? 0 : 1;
        //работа с числом Х
        String processedX = processNumber(x);
        //первый ряд таблицы
        makeRow(processedX, res, 0);
        res[1][0] = areTwoFirstDigitsOfYAnEvenNumber(y) ? 0 : 1;
        //работы с числом Y
        String processedY = processNumber(y);
        //второй ряд таблицы
        makeRow(processedY, res, 1);
        makeThirdRow(c, res, NUMBER);
        return res;
    }
    private static void makeThirdRow(String c, Integer[][] res, Integer NUMBER) {
        res[2][0] = NUMBER; //просто потому что
        res[2][1] = Integer.parseInt(String.valueOf(c.charAt(0)));
        res[2][2] = Integer.parseInt(String.valueOf(c.charAt(1)));
        res[2][3] = Integer.parseInt(String.valueOf(c.charAt(2)));
        res[2][4] = Integer.parseInt(String.valueOf(c.charAt(3)));
        res[2][5] = (res[2][0] + res[2][1] + res[2][2] + res[2][3] + res[2][4]) % 10;
        //остаток от деления всех цифр 3й строчки на 10
    }
//    public static void main(String[] args) {
//        String x;
//        String y;
//        String c;
//        try (Scanner scanner = new Scanner(System.in) // Create a Scanner object
//        ) {
//            System.out.println("Введите СЕМИЗНАЧНУЮ координату x:");
//            x = scanner.nextLine(); // Read X
//            System.out.println("Введите СЕМИЗНАЧНУЮ координату y:");
//            y = scanner.nextLine(); // Read Y
//            System.out.println("Введите ЧЕТЫРЕХЗНАЧНУЮ переменную c:");
//            c = scanner.nextLine(); // Read C
//        }
//        //проверка не введена ли чепуха
//        if (!checkIfDataIsCorrect(x, y, c)){
//            System.err.println("ВВОД НЕКОРРЕКТНЫЙ! ПОСЧИТАТЬ НЕ ПОЛУЧИТСЯ :(");
//            return;
//        }
//        Integer[][] result = calculate(x, y, c);
//        System.out.println(Arrays.deepToString(result));
//    }
}
