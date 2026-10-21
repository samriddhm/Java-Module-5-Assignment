package com.javaassign5.ex5.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaassign5.ex5.model.Book;
import com.javaassign5.ex5.service.LibraryService;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Exercise 5 (ex5-app): uses classes from ex5-model AND ex5-service,
 * Gson (managed version) and commons-lang3 (shared from the parent).
 *
 * Run from the parent folder:  mvn install  then  mvn -q -pl ex5-app exec:java
 */
public class LibraryApp {

    public static void main(String[] args) {
        LibraryService library = new LibraryService();
        library.addBook(new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018));
        library.addBook(new Book("978-0321349606", "Java Concurrency in Practice", "Brian Goetz", 2006));
        library.addBook(new Book("978-0132350884", "Clean Code", "Robert Martin", 2008));
        library.addBook(new Book("978-1617294945", "Spring in Action", "Craig Walls", 2018));

        line("Catalogue");
        library.all().forEach(b -> System.out.println("  " + b));

        line("Borrowing");
        System.out.println("  Borrowed: " + library.borrow("978-0132350884").getTitle());
        try {
            library.borrow("978-0132350884");
        } catch (IllegalStateException e) {
            System.out.println("  Refused : " + e.getMessage());
        }

        line("Search 'java'");
        library.search("java").forEach(b -> System.out.println("  " + b));

        line("JSON report (Gson)");
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("totalBooks", library.all().size());
        report.put("available", library.availableCount());
        List<String> onLoan = library.all().stream().filter(b -> !b.isAvailable()).map(Book::getTitle).toList();
        report.put("onLoan", onLoan);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(report));
    }

    private static void line(String title) {
        System.out.println();
        System.out.println(StringUtils.center(" " + title + " ", 70, '='));
    }
}
