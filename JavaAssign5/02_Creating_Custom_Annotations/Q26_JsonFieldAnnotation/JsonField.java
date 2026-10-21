import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q26: @JsonField marks a field to be included in JSON output and sets its JSON KEY name.
 *
 *   @JsonField("first_name") private String firstName;   -> "first_name": "..."
 *   @JsonField               private int age;            -> "age": ...   (empty = use Java name)
 *   (no annotation)          private String password;    -> NOT serialized
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonField {
    /** The key to use in JSON. Empty string means "use the Java field name". */
    String value() default "";

    /** If false, the field is skipped when its value is null. */
    boolean includeNull() default true;
}
