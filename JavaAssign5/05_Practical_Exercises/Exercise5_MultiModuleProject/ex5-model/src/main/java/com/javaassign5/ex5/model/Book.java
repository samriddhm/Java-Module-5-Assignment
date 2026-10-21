package com.javaassign5.ex5.model;

import org.apache.commons.lang3.StringUtils;

/** Exercise 5 (ex5-model): a library book. Uses commons-lang3, inherited from the parent pom. */
public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final int year;
    private boolean available = true;

    public Book(String isbn, String title, String author, int year) {
        if (StringUtils.isAnyBlank(isbn, title, author)) {
            throw new IllegalArgumentException("isbn, title and author are required");
        }
        this.isbn = isbn.trim();
        this.title = StringUtils.normalizeSpace(title);
        this.author = StringUtils.normalizeSpace(author);
        this.year = year;
    }

    public String getIsbn()      { return isbn; }
    public String getTitle()     { return title; }
    public String getAuthor()    { return author; }
    public int getYear()         { return year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return StringUtils.rightPad(StringUtils.abbreviate(title, 34), 36)
                + StringUtils.rightPad(author, 20) + year + (available ? "  available" : "  ON LOAN");
    }
}
