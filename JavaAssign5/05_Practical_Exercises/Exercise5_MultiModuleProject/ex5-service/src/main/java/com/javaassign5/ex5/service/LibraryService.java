package com.javaassign5.ex5.service;

import com.javaassign5.ex5.model.Book;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Exercise 5 (ex5-service): library operations, built on the model module. */
public class LibraryService {

    private final Map<String, Book> books = new LinkedHashMap<>();

    public void addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("duplicate ISBN " + book.getIsbn());
        }
        books.put(book.getIsbn(), book);
    }

    public Book borrow(String isbn) {
        Book b = get(isbn);
        if (!b.isAvailable()) throw new IllegalStateException("'" + b.getTitle() + "' is already on loan");
        b.setAvailable(false);
        return b;
    }

    public Book giveBack(String isbn) {
        Book b = get(isbn);
        b.setAvailable(true);
        return b;
    }

    /** Case-insensitive search in title and author (StringUtils from the shared dependency). */
    public List<Book> search(String text) {
        return books.values().stream()
                .filter(b -> StringUtils.containsIgnoreCase(b.getTitle(), text)
                        || StringUtils.containsIgnoreCase(b.getAuthor(), text))
                .collect(Collectors.toList());
    }

    public List<Book> all() {
        return Collections.unmodifiableList(new ArrayList<>(books.values()));
    }

    public long availableCount() {
        return books.values().stream().filter(Book::isAvailable).count();
    }

    private Book get(String isbn) {
        Book b = books.get(isbn);
        if (b == null) throw new IllegalArgumentException("no book with ISBN " + isbn);
        return b;
    }
}
