package com.javaassign5.ex4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple thread-safe in-memory store shared by all requests.
 * (Plain Java - no servlet classes - so it is easy to unit test.)
 */
public class StudentStore {

    public static final class Student {
        private final String name;
        private final String course;

        public Student(String name, String course) {
            this.name = name;
            this.course = course;
        }

        public String getName()   { return name; }
        public String getCourse() { return course; }
    }

    private static final StudentStore INSTANCE = new StudentStore();

    private final List<Student> students = Collections.synchronizedList(new ArrayList<>());

    public static StudentStore getInstance() {
        return INSTANCE;
    }

    public StudentStore() {
        students.add(new Student("Shreyas Murgai", "Software Engineering"));
        students.add(new Student("Priya Sharma", "Data Science"));
    }

    /** Validates and adds a student; returns an error message or null on success. */
    public String add(String name, String course) {
        if (name == null || name.isBlank()) return "Name is required";
        if (name.trim().length() > 50) return "Name must be at most 50 characters";
        if (course == null || course.isBlank()) return "Course is required";
        students.add(new Student(name.trim(), course.trim()));
        return null;
    }

    public List<Student> all() {
        synchronized (students) {
            return new ArrayList<>(students);
        }
    }
}
