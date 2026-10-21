import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q22: records the AUTHOR and VERSION of a class (plus optional extras).
 *
 * @Documented -> shows up in javadoc, because author/version is useful API information.
 * RUNTIME    -> can be read by reflection (e.g. to print an "About" screen).
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassInfo {
    String author();                          // required
    String version();                         // required, e.g. "1.2.0"
    String date() default "unknown";          // optional
    String[] reviewers() default {};          // optional
    String description() default "";         // optional
}
