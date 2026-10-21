package com.javaassign5.q47.core;

/** Q47 (core module): business logic shared with other modules. */
public class GradeCalculator {

    /** University of Sydney style grade bands. */
    public String grade(double mark) {
        if (mark < 0 || mark > 100) throw new IllegalArgumentException("mark must be 0-100");
        if (mark >= 85) return "HD";
        if (mark >= 75) return "DI";
        if (mark >= 65) return "CR";
        if (mark >= 50) return "PS";
        return "FA";
    }

    public double average(double... marks) {
        if (marks.length == 0) return 0;
        double sum = 0;
        for (double m : marks) sum += m;
        return sum / marks.length;
    }
}
