import java.lang.annotation.Retention;

/**
 * Q30 demo: what happens if a custom annotation is not annotated with @Retention?
 *
 * Answer shown here: it silently defaults to CLASS retention, so reflection returns
 * null / false - a very common "why is my annotation not working?" bug.
 * No compiler error or warning is produced.
 *
 * Run:  javac *.java && java MissingRetentionDemo
 *       javap -v MissingRetentionDemo\$Sample.class | grep -A2 Runtime     (bytecode check)
 */
public class MissingRetentionDemo {

    @NoRetention("hello")
    @WithRuntimeRetention("hello")
    static class Sample {
        @NoRetention
        @WithRuntimeRetention
        public void work() { }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("@Retention declared on NoRetention?          "
                + (NoRetention.class.getAnnotation(Retention.class) != null));
        System.out.println("@Retention declared on WithRuntimeRetention? "
                + WithRuntimeRetention.class.getAnnotation(Retention.class).value());
        System.out.println();

        System.out.println("Class level:");
        System.out.println("   getAnnotation(NoRetention.class)          = " + Sample.class.getAnnotation(NoRetention.class));
        System.out.println("   getAnnotation(WithRuntimeRetention.class) = " + Sample.class.getAnnotation(WithRuntimeRetention.class));

        System.out.println("Method level:");
        System.out.println("   isAnnotationPresent(NoRetention.class)          = "
                + Sample.class.getMethod("work").isAnnotationPresent(NoRetention.class));
        System.out.println("   isAnnotationPresent(WithRuntimeRetention.class) = "
                + Sample.class.getMethod("work").isAnnotationPresent(WithRuntimeRetention.class));

        System.out.println();
        System.out.println("Simulating a framework that relies on @NoRetention:");
        if (Sample.class.isAnnotationPresent(NoRetention.class)) {
            System.out.println("   framework found the annotation and configured the class");
        } else {
            System.out.println("   framework found NOTHING -> the feature silently does not work!");
        }
        System.out.println("Fix: add @Retention(RetentionPolicy.RUNTIME) to the annotation.");
    }
}
