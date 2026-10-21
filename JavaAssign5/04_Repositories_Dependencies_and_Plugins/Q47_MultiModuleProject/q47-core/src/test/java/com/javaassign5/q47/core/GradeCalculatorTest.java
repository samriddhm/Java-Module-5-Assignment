package com.javaassign5.q47.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Q47: unit test inside the core module (JUnit inherited from the parent pom). */
class GradeCalculatorTest {

    private final GradeCalculator calc = new GradeCalculator();

    @Test
    void gradeBands() {
        assertEquals("HD", calc.grade(90));
        assertEquals("DI", calc.grade(75));
        assertEquals("CR", calc.grade(70));
        assertEquals("PS", calc.grade(50));
        assertEquals("FA", calc.grade(49.9));
    }

    @Test
    void averageOfMarks() {
        assertEquals(70.0, calc.average(60, 70, 80), 0.0001);
    }

    @Test
    void invalidMarkRejected() {
        assertThrows(IllegalArgumentException.class, () -> calc.grade(101));
    }
}
