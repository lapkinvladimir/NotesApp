package com.example.NotesApp.extra;

public class MathOperations {
    public static int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    public static int fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static double power(double base, int exp) {
        return Math.pow(base, exp);
    }
}
