package com.javaassign5.q50;

/** Q50: the class under test. */
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("cannot divide by zero");
        return a / b;
    }

    public double applyDiscount(double price, double rate) {
        if (rate < 0 || rate > 1) throw new IllegalArgumentException("rate must be 0..1");
        return price * (1 - rate);
    }

    public boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
