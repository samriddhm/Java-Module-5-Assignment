package com.javaassign5.ex1.validation;

import com.javaassign5.ex1.annotations.Email;
import com.javaassign5.ex1.annotations.Length;
import com.javaassign5.ex1.annotations.NotBlank;
import com.javaassign5.ex1.annotations.Pattern;
import com.javaassign5.ex1.annotations.Range;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the validation annotations with REFLECTION and checks values against them.
 *
 *   validate(object)            -> checks every field of a finished object
 *   validateValue(field, value) -> checks ONE candidate value before it is stored
 *                                  (used by the interactive form to re-ask straight away)
 */
public final class Validator {

    private static final java.util.regex.Pattern EMAIL =
            java.util.regex.Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$");

    private Validator() { }

    /** Validates all annotated fields of an object. */
    public static List<Violation> validate(Object target) {
        List<Violation> all = new ArrayList<>();
        for (Field f : target.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                all.addAll(validateValue(f, f.get(target)));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot read field " + f.getName(), e);
            }
        }
        return all;
    }

    /** Validates a single value against the annotations on the given field. */
    public static List<Violation> validateValue(Field field, Object value) {
        List<Violation> out = new ArrayList<>();
        String name = field.getName();
        String text = (value == null) ? null : value.toString();
        boolean empty = (text == null) || text.trim().isEmpty();

        NotBlank notBlank = field.getAnnotation(NotBlank.class);
        if (notBlank != null && empty) {
            out.add(new Violation(name, "NotBlank", notBlank.message()));
            return out;                       // no point checking further rules
        }
        if (empty && !(value instanceof Number)) {
            return out;                       // optional field left empty -> OK
        }

        Length length = field.getAnnotation(Length.class);
        if (length != null) {
            int len = text.length();
            if (len < length.min() || len > length.max()) {
                out.add(new Violation(name, "Length", fill(length.message(), length.min(), length.max())
                        + " (got " + len + ")"));
            }
        }

        Pattern pattern = field.getAnnotation(Pattern.class);
        if (pattern != null && !text.matches(pattern.regex())) {
            out.add(new Violation(name, "Pattern", pattern.message()));
        }

        Email email = field.getAnnotation(Email.class);
        if (email != null && !EMAIL.matcher(text).matches()) {
            out.add(new Violation(name, "Email", email.message()));
        }

        Range range = field.getAnnotation(Range.class);
        if (range != null) {
            if (!(value instanceof Number)) {
                out.add(new Violation(name, "Range", "must be a whole number"));
            } else {
                long n = ((Number) value).longValue();
                if (n < range.min() || n > range.max()) {
                    out.add(new Violation(name, "Range", fill(range.message(), range.min(), range.max())
                            + " (got " + n + ")"));
                }
            }
        }
        return out;
    }

    private static String fill(String msg, long min, long max) {
        return msg.replace("{min}", String.valueOf(min)).replace("{max}", String.valueOf(max));
    }
}
