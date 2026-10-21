import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Q30: this annotation has NO @Retention.
 * Java then uses the DEFAULT policy: RetentionPolicy.CLASS
 * -> stored in the .class file, but NOT available to reflection at runtime.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NoRetention {
    String value() default "no retention given";
}
