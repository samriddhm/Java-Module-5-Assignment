import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q28: marks a method as a test (like JUnit's @Test).
 * expected = the exception the test is SUPPOSED to throw (None = no exception expected).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyTest {

    /** Placeholder meaning "no exception expected" (annotation defaults cannot be null). */
    class None extends Throwable {
        private static final long serialVersionUID = 1L;
        private None() { }
    }

    String name() default "";
    Class<? extends Throwable> expected() default None.class;
}
