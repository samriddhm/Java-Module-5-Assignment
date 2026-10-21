package com.javaassign5.q50;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Q50: tagged "slow" -> SKIPPED by default (pom: excludedGroups = slow).
 * Runs only with:  mvn test -Pall-tests   (or  mvn test -Dexcluded.test.groups=)
 */
@Tag("slow")
class SlowCalculatorTest {

    @Test
    void countPrimesBelowTwoMillion() {
        Calculator calc = new Calculator();
        int count = 0;
        for (int i = 0; i < 2_000_000; i++) {
            if (calc.isPrime(i)) count++;
        }
        assertEquals(148_933, count);
    }
}
