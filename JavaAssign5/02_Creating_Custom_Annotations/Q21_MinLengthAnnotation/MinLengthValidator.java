import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** Q21: checks every @MinLength field of an object using reflection. */
public final class MinLengthValidator {

    private MinLengthValidator() { }

    /** Returns a list of error messages; an empty list means the object is valid. */
    public static List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
        Class<?> c = target.getClass();

        for (Field field : c.getDeclaredFields()) {
            MinLength rule = field.getAnnotation(MinLength.class);
            if (rule == null) continue;

            if (!CharSequence.class.isAssignableFrom(field.getType())) {
                throw new IllegalStateException("@MinLength used on non-text field: " + field.getName());
            }
            if (rule.value() < 0) {
                throw new IllegalStateException("@MinLength value cannot be negative on " + field.getName());
            }

            try {
                field.setAccessible(true);
                Object raw = field.get(target);
                String text = (raw == null) ? "" : raw.toString();
                if (rule.trim()) text = text.trim();

                if (text.length() < rule.value()) {
                    errors.add(field.getName() + " " + rule.message()
                            .replace("{min}", String.valueOf(rule.value()))
                            .replace("{actual}", String.valueOf(text.length())));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return errors;
    }

    /** Convenience method: throws an exception instead of returning errors. */
    public static void validateOrThrow(Object target) {
        List<String> errors = validate(target);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }
}
