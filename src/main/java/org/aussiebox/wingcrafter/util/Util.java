package org.aussiebox.wingcrafter.util;

public class Util {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
