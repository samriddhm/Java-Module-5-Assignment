import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q16: an annotation where most elements have DEFAULT values,
 * so the user only has to write what is different.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceConfig {

    enum Env { DEV, TEST, PROD }

    String name();                         // REQUIRED - no default
    int timeoutSeconds() default 30;       // primitive default
    boolean cacheEnabled() default true;   // boolean default
    Env environment() default Env.DEV;     // enum default
    String[] tags() default {};            // array default (empty array, NOT null)
    Class<?> fallback() default Void.class; // class default (null is not allowed)
}
