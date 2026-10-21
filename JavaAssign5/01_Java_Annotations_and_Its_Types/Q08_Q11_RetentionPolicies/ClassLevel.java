import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q8/Q11: CLASS retention (this is also the DEFAULT when @Retention is missing).
 * Stored in the .class file as "RuntimeInvisibleAnnotations",
 * but the JVM does NOT load it, so reflection cannot see it.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ClassLevel {
    String value();
}
