import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q25: annotation with arrays of EVERY allowed kind:
 * String[], primitive int[], enum[], Class[] (and annotation[] in Q18's Schedules).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Endpoint {

    enum HttpMethod { GET, POST, PUT, DELETE }

    String[] paths();                                         // String array
    HttpMethod[] methods() default {HttpMethod.GET};          // enum array with a default
    int[] successCodes() default {200};                       // primitive array
    Class<? extends Exception>[] handles() default {};        // Class array, empty default
}
