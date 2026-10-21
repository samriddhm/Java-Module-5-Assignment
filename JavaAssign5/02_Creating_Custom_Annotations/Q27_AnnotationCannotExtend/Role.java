import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q27: base "annotation" we would LIKE to extend.
 *
 * This does NOT compile:
 *     public @interface AdminRole extends Role { }   // error: 'extends' not allowed for @interfaces
 *
 * Workaround = COMPOSITION: allow Role on other annotations (ANNOTATION_TYPE)
 * and put it on top of them, like Spring's @Service is annotated with @Component.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Role {
    String value();
    int level() default 1;
}
