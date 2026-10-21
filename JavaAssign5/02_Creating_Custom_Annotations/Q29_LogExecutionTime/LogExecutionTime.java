import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q29: put this on a method to have its execution time logged automatically.
 *
 * RUNTIME retention -> the proxy (TimingProxy) can see it while the program runs.
 * METHOD target     -> only methods can be timed.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogExecutionTime {

    /** Optional label for the log line (default = method name). */
    String value() default "";

    /** Print a WARNING if the call takes longer than this many milliseconds. */
    long warnAboveMs() default Long.MAX_VALUE;
}
