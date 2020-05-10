package com.centime.Utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Utils {
    public static String getRandomNumber(int capacity) {

        final String AB = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(capacity);
        for (int i = 0; i < capacity; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String getRandomString(int length) {
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < sb.capacity(); i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static void pause(int seconds) throws NumberFormatException, InterruptedException {
        Thread.sleep(seconds * 1000L);

    }
    public static String getOlddate(int years){
        LocalDate ld = LocalDate.now();
        ld = ld.minusYears(years).minusDays(1);
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ld);

    }
}
