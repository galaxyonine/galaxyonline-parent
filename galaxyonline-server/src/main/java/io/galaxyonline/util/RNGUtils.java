package io.galaxyonline.util;

import java.util.Random;

public class RNGUtils {
    public static Double randomDouble(Double min, Double max) {
        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}