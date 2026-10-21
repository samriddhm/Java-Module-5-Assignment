import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q21: @MinLength - the annotated String must have AT LEAST value() characters.
 *
 * Usage:  @MinLength(5) private String password;
 *         @MinLength(value = 3, message = "name is too short") private String name;
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface MinLength {

    /** Minimum number of characters (single-value element, so "value =" can be skipped). */
    int value();

    /** Error message; {min} and {actual} are replaced by the validator. */
    String message() default "must have at least {min} characters but has {actual}";

    /** If true, leading/trailing spaces are ignored before counting. */
    boolean trim() default true;
}
