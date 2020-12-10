package com.example.easystock.utils;

public class NumberHelper {

    public static double getPlusOperation(String firstNumber, String secondNumber) {
        double returnValue = Double.parseDouble(firstNumber) + Double.parseDouble(secondNumber);
        return (double) Math.round(returnValue * 100) / 100;
    }

    public static double getPlusOperation(double firstNumber, double secondNumber) {
        double returnValue = firstNumber + secondNumber;
        return (double) Math.round(returnValue * 100) / 100;
    }

    public static double getMinusOperation(String firstNumber, String secondNumber) {
        double returnValue = Double.parseDouble(firstNumber) - Double.parseDouble(secondNumber);
        return (double) Math.round(returnValue * 100) / 100;
    }

    public static double getMinusOperation(double firstNumber, double secondNumber) {
        double returnValue = firstNumber - secondNumber;
        return (double) Math.round(returnValue * 100) / 100;
    }

    public static double getRoundingNumber(String numberToRound) {
        return (double) Math.round(Double.parseDouble(numberToRound) * 100) / 100;
    }

    public static double getRoundingNumber(double numberToRound) {
        return (double) Math.round(numberToRound * 100) / 100;
    }


}
