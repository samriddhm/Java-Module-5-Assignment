import java.util.List;

/**
 * Q21 demo: validate three registrations with @MinLength.
 *
 * Run:  javac *.java && java MinLengthDemo
 */
public class MinLengthDemo {

    public static void main(String[] args) {
        Registration[] forms = {
                new Registration("shrey", "correcthorse", "12 George St, Sydney"),
                new Registration("  al  ", "abc", "Sydney"),
                new Registration(null, null, null),
        };

        for (Registration r : forms) {
            System.out.println(r);
            List<String> errors = MinLengthValidator.validate(r);
            if (errors.isEmpty()) {
                System.out.println("   OK - all @MinLength rules passed");
            } else {
                for (String e : errors) System.out.println("   ERROR: " + e);
            }
        }

        System.out.println();
        System.out.println("validateOrThrow() on an invalid form:");
        try {
            MinLengthValidator.validateOrThrow(new Registration("bo", "password123", "1 Long Road, Town"));
        } catch (IllegalArgumentException ex) {
            System.out.println("   Caught exception -> " + ex.getMessage());
        }
    }
}
