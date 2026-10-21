import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q9 - STEP 1: declare the annotation with the @interface keyword.
 * STEP 2: add meta-annotations to say WHERE it can go (@Target)
 *         and HOW LONG it is kept (@Retention).
 * STEP 3: add "elements" (they look like methods with no body),
 *         optionally with default values.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)                      // keep it so reflection can read it
@Target({ElementType.TYPE, ElementType.METHOD})          // allowed on classes and methods
public @interface Review {

    /** An enum declared inside the annotation, used as an element type. */
    enum Status { PENDING, APPROVED, REJECTED }

    String reviewer();                     // required (no default)
    Status status() default Status.PENDING; // optional
    String comment() default "";           // optional
}
