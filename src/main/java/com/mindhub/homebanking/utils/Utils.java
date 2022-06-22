package com.mindhub.homebanking.utils;

public class Utils {

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getCardNumber = String.valueOf(getRandomNumber(1000,9999) + " " + getRandomNumber(1000,9999) + " " + getRandomNumber(1000,9999) + " " + getRandomNumber(1000,9999));
}
