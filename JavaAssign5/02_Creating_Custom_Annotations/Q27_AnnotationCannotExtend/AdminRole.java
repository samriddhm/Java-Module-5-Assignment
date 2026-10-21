import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q27: a "composed" (meta-annotated) annotation.
 * Instead of `extends Role`, it CARRIES @Role on itself.
 */
@Role(value = "ADMIN", level = 10)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdminRole {
    String department() default "IT";
}
