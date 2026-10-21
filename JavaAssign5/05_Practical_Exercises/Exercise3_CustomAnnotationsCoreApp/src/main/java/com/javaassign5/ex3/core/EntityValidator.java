package com.javaassign5.ex3.core;

import com.javaassign5.ex3.annotations.MaxLength;
import com.javaassign5.ex3.annotations.Required;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** Checks @Required and @MaxLength on any object using reflection. */
public final class EntityValidator {

    private EntityValidator() { }

    public static List<String> validate(Object entity) {
        List<String> errors = new ArrayList<>();
        for (Field f : entity.getClass().getDeclaredFields()) {
            Object value;
            try {
                f.setAccessible(true);
                value = f.get(entity);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }

            Required required = f.getAnnotation(Required.class);
            boolean missing = value == null || (value instanceof String && ((String) value).isBlank());
            if (required != null && missing) {
                errors.add(required.message());
                continue;
            }

            MaxLength max = f.getAnnotation(MaxLength.class);
            if (max != null && value instanceof String && ((String) value).length() > max.value()) {
                errors.add(f.getName() + " must be at most " + max.value() + " characters (got "
                        + ((String) value).length() + ")");
            }
        }
        return errors;
    }
}
