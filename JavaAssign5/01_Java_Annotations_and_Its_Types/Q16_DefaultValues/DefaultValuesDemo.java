import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Q16 demo: prints the configuration of each service and marks which values
 * came from the annotation's DEFAULT.
 *
 * Run:  javac *.java && java DefaultValuesDemo
 */
public class DefaultValuesDemo {

    public static void main(String[] args) throws Exception {
        Class<?>[] services = {EmailService.class, PaymentService.class, ReportService.class};

        for (Class<?> s : services) {
            ServiceConfig cfg = s.getAnnotation(ServiceConfig.class);
            System.out.println(cfg.name());
            print("timeoutSeconds", cfg.timeoutSeconds(), cfg);
            print("cacheEnabled", cfg.cacheEnabled(), cfg);
            print("environment", cfg.environment(), cfg);
            print("tags", Arrays.toString(cfg.tags()), cfg);
            print("fallback", cfg.fallback().getSimpleName(), cfg);
            System.out.println();
        }
    }

    /** Uses Method.getDefaultValue() to tell whether the value is the default. */
    private static void print(String element, Object shown, ServiceConfig cfg) throws Exception {
        Method m = ServiceConfig.class.getMethod(element);
        Object actual = m.invoke(cfg);
        Object def = m.getDefaultValue();
        boolean isDefault = (actual instanceof Object[])
                ? Arrays.equals((Object[]) actual, (Object[]) def)
                : actual.equals(def);
        System.out.printf("   %-15s = %-20s %s%n", element, shown, isDefault ? "(default)" : "");
    }
}
