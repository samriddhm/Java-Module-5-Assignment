package com.javaassign5.ex4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Unit tests for the web app's logic (no server needed). Run with: mvn test */
class StudentStoreTest {

    @Test
    void startsWithTwoStudents() {
        assertEquals(2, new StudentStore().all().size());
    }

    @Test
    void addValidStudent() {
        StudentStore store = new StudentStore();
        assertNull(store.add("  Alex  ", "Cyber Security"));
        assertEquals("Alex", store.all().get(2).getName());
    }

    @Test
    void rejectsInvalidInput() {
        StudentStore store = new StudentStore();
        assertEquals("Name is required", store.add(" ", "IT"));
        assertEquals("Course is required", store.add("Sam", ""));
        assertEquals("Name must be at most 50 characters", store.add("x".repeat(51), "IT"));
        assertEquals(2, store.all().size());
    }
}
