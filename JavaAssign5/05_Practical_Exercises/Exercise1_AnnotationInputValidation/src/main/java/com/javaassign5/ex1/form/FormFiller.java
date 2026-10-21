package com.javaassign5.ex1.form;

import com.javaassign5.ex1.annotations.Prompt;
import com.javaassign5.ex1.validation.Validator;
import com.javaassign5.ex1.validation.Violation;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Builds a console form AUTOMATICALLY from any class whose fields carry @Prompt.
 * Every answer is checked with Validator; on an error the same question is asked again.
 */
public class FormFiller {

    private final Scanner in;
    private final PrintStream out;
    private final boolean echoInput;   // true in demo mode, so the "typed" answers are visible

    public FormFiller(Scanner in, PrintStream out, boolean echoInput) {
        this.in = in;
        this.out = out;
        this.echoInput = echoInput;
    }

    public <T> T fill(Class<T> formClass) throws ReflectiveOperationException {
        T form = formClass.getDeclaredConstructor().newInstance();

        List<Field> questions = new ArrayList<>();
        for (Field f : formClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Prompt.class)) questions.add(f);
        }
        questions.sort(Comparator.comparingInt(f -> f.getAnnotation(Prompt.class).order()));

        for (Field field : questions) {
            field.setAccessible(true);
            Prompt prompt = field.getAnnotation(Prompt.class);
            while (true) {
                out.print(prompt.value() + ": ");
                if (!in.hasNextLine()) throw new IllegalStateException("input ended before the form was complete");
                String raw = in.nextLine();
                if (echoInput) out.println(raw);

                Object value = convert(raw, field.getType());
                List<Violation> problems = (value == null && isNumeric(field.getType()))
                        ? List.of(new Violation(field.getName(), "Type", "must be a whole number"))
                        : Validator.validateValue(field, value);

                if (problems.isEmpty()) {
                    if (value != null) field.set(form, value);
                    break;
                }
                for (Violation v : problems) out.println("   x " + v.getField() + " " + v.getMessage());
            }
        }
        return form;
    }

    private static boolean isNumeric(Class<?> type) {
        return type == int.class || type == Integer.class || type == long.class || type == Long.class;
    }

    /** Converts the typed text to the field's type; returns null if a number cannot be parsed. */
    private static Object convert(String raw, Class<?> type) {
        String t = raw.trim();
        try {
            if (type == int.class || type == Integer.class) return Integer.parseInt(t);
            if (type == long.class || type == Long.class) return Long.parseLong(t);
        } catch (NumberFormatException e) {
            return null;
        }
        return t;
    }
}
