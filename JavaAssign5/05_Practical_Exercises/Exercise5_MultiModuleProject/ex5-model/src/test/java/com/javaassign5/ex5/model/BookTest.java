package com.javaassign5.ex5.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** JUnit is inherited from the parent pom - this module's pom does not mention it. */
class BookTest {

    @Test
    void cleansUpSpaces() {
        Book b = new Book(" 978-0 ", "  Clean   Code ", "Robert  Martin", 2008);
        assertEquals("Clean Code", b.getTitle());
        assertEquals("Robert Martin", b.getAuthor());
        assertTrue(b.isAvailable());
    }

    @Test
    void requiresTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Book("1", " ", "A", 2000));
    }
}
