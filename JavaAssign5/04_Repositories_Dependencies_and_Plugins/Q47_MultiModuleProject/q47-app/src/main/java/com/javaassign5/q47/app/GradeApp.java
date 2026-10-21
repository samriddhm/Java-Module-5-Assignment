package com.javaassign5.q47.app;

import com.javaassign5.q47.core.GradeCalculator;

/**
 * Q47 (app module): uses a class from the OTHER module (q47-core).
 *
 * Run from the parent folder:  mvn install  then  mvn -pl q47-app exec:java
 */
public class GradeApp {

    public static void main(String[] args) {
        GradeCalculator calc = new GradeCalculator();
        double[] marks = {92, 78, 66, 55, 41};

        System.out.println("Mark  Grade   (GradeCalculator comes from module q47-core)");
        for (double m : marks) {
            System.out.printf("%4.0f  %s%n", m, calc.grade(m));
        }
        double avg = calc.average(marks);
        System.out.printf("Average %.1f -> %s%n", avg, calc.grade(avg));
    }
}
