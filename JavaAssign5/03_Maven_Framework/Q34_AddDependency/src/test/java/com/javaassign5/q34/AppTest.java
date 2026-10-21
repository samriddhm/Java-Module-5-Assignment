package com.javaassign5.q34;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Q34: uses the test-scoped JUnit dependency. Run with: mvn test */
class AppTest {

    @Test
    void tidyNormalizesSpacesAndCapitalizes() {
        assertEquals("Hello world", App.tidy("  hello    world "));
    }
}
