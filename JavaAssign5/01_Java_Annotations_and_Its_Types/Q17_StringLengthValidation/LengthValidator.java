import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Q17: reads @Length on every field of an object and checks the value.
 * Returns a list of error messages (empty list = valid).
 */
public final class LengthValidator {

    private LengthValidator() { }

    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            Length rule = field.getAnnotation(Length.class);
            if (rule == null) continue;                           // field not annotated

            if (field.getType() != String.class) {
                errors.add(field.getName() + ": @Length can only be used on String fields");
                continue;
            }
            if (rule.min() < 0 || rule.min() > rule.max()) {
                errors.add(field.getName() + ": invalid @Length(min=" + rule.min() + ", max=" + rule.max() + ")");
                continue;
            }

            try {
                field.setAccessible(true);
                String value = (String) field.get(obj);
                int len = (value == null) ? 0 : value.length();   // treat null as length 0
                if (len < rule.min() || len > rule.max()) {
                    String msg = rule.message()
                            .replace("{min}", String.valueOf(rule.min()))
                            .replace("{max}", String.valueOf(rule.max()));
                    String shown = (value != null && value.length() > 20) ? value.substring(0, 20) + "..." : value;
                    errors.add(field.getName() + ": " + msg + " (was " + len + ": \"" + shown + "\")");
                }
            } catch (IllegalAccessException e) {
                errors.add(field.getName() + ": could not be read (" + e.getMessage() + ")");
            }
        }
        return errors;
    }
}
