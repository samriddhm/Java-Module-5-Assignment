import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Q12 demo: the main reflection methods for reading annotations.
 *
 *   isAnnotationPresent(X.class)  -> true/false
 *   getAnnotation(X.class)        -> the annotation object (or null)
 *   getAnnotations()              -> all annotations (incl. inherited)
 *   getDeclaredAnnotations()      -> only annotations written directly on it
 *   getParameterAnnotations() / Parameter.getAnnotation(...)
 *
 * Run:  javac *.java && java ReflectionProcessingDemo
 */
public class ReflectionProcessingDemo {

    public static void main(String[] args) throws Exception {
        Customer customer = new Customer();
        Class<?> clazz = customer.getClass();

        // ---------- 1) Class level ----------
        System.out.println("1) CLASS annotations");
        if (clazz.isAnnotationPresent(Table.class)) {
            Table t = clazz.getAnnotation(Table.class);
            System.out.println("   " + clazz.getSimpleName() + " maps to table '" + t.name() + "'");
        }

        // ---------- 2) Field level: build an SQL INSERT ----------
        System.out.println("2) FIELD annotations -> generate SQL");
        List<String> cols = new ArrayList<>();
        List<String> vals = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            Column col = f.getAnnotation(Column.class);
            if (col == null) {
                System.out.println("   skipping field '" + f.getName() + "' (no @Column)");
                continue;
            }
            f.setAccessible(true);                     // allow reading a private field
            Object value = f.get(customer);
            cols.add(col.name() + (col.primaryKey() ? "(PK)" : ""));
            vals.add(value instanceof String ? "'" + value + "'" : String.valueOf(value));
        }
        System.out.println("   INSERT INTO " + clazz.getAnnotation(Table.class).name()
                + " (" + String.join(", ", cols) + ") VALUES (" + String.join(", ", vals) + ");");

        // ---------- 3) Method level: call methods in order ----------
        System.out.println("3) METHOD annotations -> invoke @RunOnStartup methods by order");
        List<Method> startup = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(RunOnStartup.class)) startup.add(m);
        }
        startup.sort(Comparator.comparingInt(m -> m.getAnnotation(RunOnStartup.class).order()));
        for (Method m : startup) {
            System.out.println("   invoking " + m.getName() + " (order "
                    + m.getAnnotation(RunOnStartup.class).order() + ")");
            m.invoke(customer);
        }

        // ---------- 4) Parameter level ----------
        System.out.println("4) PARAMETER annotations");
        Method rename = clazz.getMethod("rename", String.class, String.class);
        Parameter[] params = rename.getParameters();
        for (int i = 0; i < params.length; i++) {
            boolean notNull = params[i].isAnnotationPresent(NotNull.class);
            System.out.println("   rename() parameter #" + i + " (" + params[i].getType().getSimpleName()
                    + ") @NotNull? " + notNull);
        }
        Object[] callArgs = {null, "typo"};
        System.out.println("   trying rename(null, \"typo\") with a @NotNull check...");
        try {
            checkNotNull(rename, callArgs);
            rename.invoke(customer, callArgs);
        } catch (IllegalArgumentException e) {
            System.out.println("   BLOCKED: " + e.getMessage());
        }

        // ---------- 5) Listing everything generically ----------
        System.out.println("5) getDeclaredAnnotations() on the class:");
        for (Annotation a : clazz.getDeclaredAnnotations()) {
            System.out.println("   " + a);
        }
    }

    /** Throws if any argument whose parameter is marked @NotNull is null. */
    static void checkNotNull(Method m, Object[] args) {
        Annotation[][] paramAnnotations = m.getParameterAnnotations();
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof NotNull && args[i] == null) {
                    throw new IllegalArgumentException("parameter #" + i + " of "
                            + m.getName() + "() is @NotNull but null was passed");
                }
            }
        }
    }
}
