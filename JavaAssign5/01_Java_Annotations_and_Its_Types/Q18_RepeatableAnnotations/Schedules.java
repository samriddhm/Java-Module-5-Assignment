import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q18: the CONTAINER annotation for @Schedule.
 * Rules: it must have a value() element that returns Schedule[],
 * and its @Retention/@Target must be compatible with @Schedule's.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedules {
    Schedule[] value();
}
