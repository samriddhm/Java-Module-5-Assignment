import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q20: compile-time only annotation (SOURCE retention).
 * The CompileTimeProcessor checks that every field of an @Immutable class is final,
 * and STOPS THE BUILD with an error if it is not.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Immutable {
}
