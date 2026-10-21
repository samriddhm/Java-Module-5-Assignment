package com.javaassign5.q50;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Q50: fast unit tests - always run by "mvn test". */
class CalculatorTest {

    private Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    @Test
    @DisplayName("2 + 3 = 5")
    void addsNumbers() {
        assertEquals(5, calc.add(2, 3));
    }

    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvSource({"10, 2, 5", "9, 3, 3", "-8, 2, -4"})
    void dividesNumbers(int a, int b, int expected) {
        assertEquals(expected, calc.divide(a, b));
    }

    @Test
    void divideByZeroThrows() {
        assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
    }

    @Test
    void discountUsesRateFromPom() {
        // value comes from <systemPropertyVariables> in pom.xml (falls back to 0.10 in an IDE)
        double rate = Double.parseDouble(System.getProperty("discount.rate", "0.10"));
        assertEquals(90.0, calc.applyDiscount(100.0, rate), 0.0001);
    }

    @Test
    void runningInTestEnvironment() {
        assertEquals("test", System.getProperty("app.environment", "test"));
    }

    @Test
    void primes() {
        assertTrue(calc.isPrime(13));
        assertFalse(calc.isPrime(15));
        assertFalse(calc.isPrime(1));
    }
}
