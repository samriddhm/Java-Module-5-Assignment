package com.javaassign5.ex5.service;

import com.javaassign5.ex5.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LibraryServiceTest {

    private LibraryService service;

    @BeforeEach
    void setUp() {
        service = new LibraryService();
        service.addBook(new Book("111", "Effective Java", "Joshua Bloch", 2018));
        service.addBook(new Book("222", "Java Concurrency in Practice", "Brian Goetz", 2006));
        service.addBook(new Book("333", "Clean Code", "Robert Martin", 2008));
    }

    @Test
    void searchIsCaseInsensitive() {
        assertEquals(2, service.search("JAVA").size());
        assertEquals(1, service.search("goetz").size());
    }

    @Test
    void borrowAndReturn() {
        service.borrow("111");
        assertFalse(service.all().get(0).isAvailable());
        assertEquals(2, service.availableCount());
        service.giveBack("111");
        assertEquals(3, service.availableCount());
    }

    @Test
    void cannotBorrowTwice() {
        service.borrow("333");
        assertThrows(IllegalStateException.class, () -> service.borrow("333"));
    }

    @Test
    void duplicateIsbnRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addBook(new Book("111", "Copy", "Someone", 2020)));
    }
}
