import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q8/Q11: RUNTIME retention.
 * Stored in the .class file as "RuntimeVisibleAnnotations" AND loaded by the JVM,
 * so reflection CAN read it while the program runs.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RuntimeLevel {
    String value();
}
