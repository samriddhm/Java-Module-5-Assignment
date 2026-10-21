import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q17: custom annotation to validate the LENGTH of a String field.
 * Example:  @Length(min = 3, max = 15) private String username;
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)   // must be RUNTIME so the validator can read it
@Target(ElementType.FIELD)            // only allowed on fields
public @interface Length {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "length must be between {min} and {max}";
}
