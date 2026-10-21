import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Q20: compile-time only annotation (SOURCE retention).
 * The CompileTimeProcessor prints every @Todo during compilation and
 * GENERATES a new Java source file (GeneratedTodoList.java) listing them.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Todo {
    String value();
}
