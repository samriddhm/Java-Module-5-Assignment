import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Q9 - STEP 5: process (read) the annotation, here with reflection.
 *
 * Run:  javac *.java && java CustomAnnotationDemo
 */
public class CustomAnnotationDemo {

    public static void main(String[] args) {
        Class<ReportGenerator> clazz = ReportGenerator.class;

        Review classReview = clazz.getAnnotation(Review.class);
        System.out.println("Class " + clazz.getSimpleName() + " -> " + describe(classReview));

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName)); // stable output order

        for (Method m : methods) {
            Review r = m.getAnnotation(Review.class);
            if (r == null) {
                System.out.println("Method " + m.getName() + "() -> not reviewed");
            } else {
                System.out.println("Method " + m.getName() + "() -> " + describe(r));
                if (r.status() == Review.Status.REJECTED) {
                    System.out.println("    WARNING: " + m.getName() + " was rejected!");
                }
            }
        }
    }

    private static String describe(Review r) {
        return "reviewer=" + r.reviewer()
                + ", status=" + r.status()
                + ", comment=\"" + r.comment() + "\"";
    }
}
