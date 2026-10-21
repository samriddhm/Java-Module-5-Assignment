import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Q8/Q11: SOURCE retention - thrown away by the compiler. Not in the .class file. */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SourceLevel {
    String value();
}
