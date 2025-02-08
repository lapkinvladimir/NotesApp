package com.example.NotesApp.extra;

import java.util.Random;

public class DataGenerator {
    private static final String[] NAMES = {"Alice", "Bob", "Charlie", "David", "Eve"};

    public static String getRandomName() {
        Random random = new Random();
        return NAMES[random.nextInt(NAMES.length)];
    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
