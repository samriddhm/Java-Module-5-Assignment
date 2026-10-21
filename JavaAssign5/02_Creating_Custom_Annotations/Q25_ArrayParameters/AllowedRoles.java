import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q25: annotation whose ONLY element is an array called "value".
 * Shortcuts allowed:
 *     @AllowedRoles({"ADMIN", "MANAGER"})    -> several values
 *     @AllowedRoles("ADMIN")                 -> ONE value, no braces needed
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowedRoles {
    String[] value();
}
