import java.util.List;

/**
 * Q17 demo: validate a good and a bad UserAccount.
 *
 * Run:  javac *.java && java LengthValidationDemo
 */
public class LengthValidationDemo {

    public static void main(String[] args) {
        UserAccount good = new UserAccount("shrey", "superSecret1", "Software engineering student", null);
        UserAccount bad = new UserAccount("ab", "123", "x".repeat(60), "anything goes");

        check(good);
        check(bad);
    }

    private static void check(UserAccount acc) {
        List<String> errors = LengthValidator.validate(acc);
        System.out.println("Validating " + acc);
        if (errors.isEmpty()) {
            System.out.println("   VALID");
        } else {
            System.out.println("   INVALID - " + errors.size() + " problem(s):");
            errors.forEach(e -> System.out.println("   - " + e));
        }
        System.out.println();
    }
}
