/**
 * Q22 demo: prints an "About" report of each class using @ClassInfo,
 * and checks that the version follows the MAJOR.MINOR.PATCH format.
 *
 * Run:  javac *.java && java ClassInfoDemo
 */
public class ClassInfoDemo {

    public static void main(String[] args) {
        Class<?>[] classes = {InventoryManager.class, PaymentProcessor.class, LegacyHelper.class};

        for (Class<?> c : classes) {
            System.out.println("=== " + c.getSimpleName() + " ===");
            ClassInfo info = c.getAnnotation(ClassInfo.class);
            if (info == null) {
                System.out.println("   (no @ClassInfo - author and version unknown)");
                continue;
            }
            System.out.println("   Author      : " + info.author());
            System.out.println("   Version     : " + info.version()
                    + (info.version().matches("\\d+\\.\\d+\\.\\d+") ? "  (valid semantic version)" : "  (INVALID format)"));
            System.out.println("   Date        : " + info.date());
            System.out.println("   Reviewers   : " + (info.reviewers().length == 0
                    ? "none" : String.join(", ", info.reviewers())));
            System.out.println("   Description : " + (info.description().isEmpty() ? "-" : info.description()));
            System.out.println("   Major version number = " + info.version().split("\\.")[0]);
        }
    }
}
