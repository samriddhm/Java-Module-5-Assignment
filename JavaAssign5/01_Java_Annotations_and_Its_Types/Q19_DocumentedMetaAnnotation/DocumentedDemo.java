import java.lang.annotation.Documented;

/**
 * Q19 demo (runtime part): @Documented only affects JAVADOC.
 * At runtime, both annotations are still readable by reflection.
 * Run the javadoc part with run.sh.
 *
 * Run:  javac *.java && java DocumentedDemo
 */
public class DocumentedDemo {

    public static void main(String[] args) {
        System.out.println("Is ApiInfo marked @Documented?      "
                + ApiInfo.class.isAnnotationPresent(Documented.class));
        System.out.println("Is InternalNote marked @Documented? "
                + InternalNote.class.isAnnotationPresent(Documented.class));
        System.out.println();
        System.out.println("At runtime, PaymentGateway still has BOTH annotations:");
        System.out.println("   " + PaymentGateway.class.getAnnotation(ApiInfo.class));
        System.out.println("   " + PaymentGateway.class.getAnnotation(InternalNote.class));
        System.out.println();
        System.out.println("=> @Documented changes only what appears in the generated javadoc HTML.");
    }
}
