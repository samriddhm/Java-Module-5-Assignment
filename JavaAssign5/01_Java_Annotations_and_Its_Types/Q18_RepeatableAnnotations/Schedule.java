import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q18: a REPEATABLE annotation (Java 8+).
 * @Repeatable points to the "container" annotation (Schedules) that the
 * compiler uses behind the scenes to hold several @Schedule values.
 */
@Repeatable(Schedules.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedule {
    String day();
    int hour() default 9;
}
