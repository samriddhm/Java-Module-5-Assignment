package com.javaassign5.q49;

/**
 * Q49: a tiny library class. After "mvn deploy", other projects can use it with:
 *
 *   <dependency>
 *       <groupId>com.javaassign5</groupId>
 *       <artifactId>q49-deploy-demo</artifactId>
 *       <version>1.0.0</version>
 *   </dependency>
 */
public final class TemperatureConverter {

    private TemperatureConverter() { }

    public static double celsiusToFahrenheit(double c) {
        return c * 9 / 5 + 32;
    }

    public static double fahrenheitToCelsius(double f) {
        return (f - 32) * 5 / 9;
    }
}
