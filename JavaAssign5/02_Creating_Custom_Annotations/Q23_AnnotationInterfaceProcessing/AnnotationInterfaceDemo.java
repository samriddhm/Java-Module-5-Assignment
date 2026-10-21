import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Q23 demo: processing custom annotations through the java.lang.annotation.Annotation interface.
 *
 * Every annotation type automatically extends java.lang.annotation.Annotation, and every
 * annotation object you get from reflection implements it. The interface gives you:
 *     Class<? extends Annotation> annotationType()
 *     boolean equals(Object)   int hashCode()   String toString()
 *
 * This lets you write GENERIC code that handles ANY annotation without knowing its type
 * in advance - plus type-specific code using instanceof / casting.
 *
 * Run:  javac *.java && java AnnotationInterfaceDemo
 */
public class AnnotationInterfaceDemo {

    public static void main(String[] args) throws Exception {
        Method[] methods = ProductController.class.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        System.out.println("PART 1 - Generic processing with the Annotation interface");
        for (Method m : methods) {
            Annotation[] annotations = m.getAnnotations();      // returns Annotation[]
            System.out.println("Method " + m.getName() + "() has " + annotations.length + " annotation(s)");
            for (Annotation a : annotations) {
                describeGenerically(a);
            }
        }

        System.out.println();
        System.out.println("PART 2 - Type-specific processing (instanceof + cast)");
        for (Method m : methods) {
            for (Annotation a : m.getAnnotations()) {
                if (a instanceof Permission) {
                    Permission p = (Permission) a;
                    System.out.println("   " + m.getName() + "(): needs role " + p.role()
                            + " (level " + p.level() + ")" + (p.level() >= 5 ? "  <- sensitive!" : ""));
                } else if (a instanceof Cacheable) {
                    Cacheable c = (Cacheable) a;
                    System.out.println("   " + m.getName() + "(): cache for " + c.seconds() + "s, keys "
                            + Arrays.toString(c.keys()));
                }
            }
        }

        System.out.println();
        System.out.println("PART 3 - equals()/hashCode() compare annotation VALUES, not objects");
        Permission onClass = ProductController.class.getAnnotation(Permission.class);
        Permission onGet = ProductController.class.getMethod("getProduct", int.class).getAnnotation(Permission.class);
        Permission onDelete = ProductController.class.getMethod("deleteProduct", int.class).getAnnotation(Permission.class);
        System.out.println("   class @Permission equals getProduct @Permission?   " + onClass.equals(onGet)
                + "  (same values: role=USER, level=1)");
        System.out.println("   class @Permission equals deleteProduct @Permission? " + onClass.equals(onDelete));
        System.out.println("   hashCodes equal for equal annotations?            "
                + (onClass.hashCode() == onGet.hashCode()));
        System.out.println("   Runtime class of the annotation object: "
                + (java.lang.reflect.Proxy.isProxyClass(onClass.getClass()) ? "a JDK dynamic proxy" : onClass.getClass())
                + " implementing " + onClass.annotationType().getSimpleName());
    }

    /** Prints any annotation's type and ALL of its element values, using only the Annotation interface. */
    static void describeGenerically(Annotation a) throws Exception {
        Class<? extends Annotation> type = a.annotationType();
        StringBuilder sb = new StringBuilder("   @" + type.getSimpleName() + " {");
        Method[] elements = type.getDeclaredMethods();
        Arrays.sort(elements, Comparator.comparing(Method::getName));
        for (int i = 0; i < elements.length; i++) {
            Method el = elements[i];
            Object value = el.invoke(a);
            String shown = (value instanceof Object[]) ? Arrays.toString((Object[]) value) : String.valueOf(value);
            sb.append(el.getName()).append("=").append(shown);
            if (i < elements.length - 1) sb.append(", ");
        }
        sb.append("}");
        System.out.println(sb);
        System.out.println("        toString(): " + a);
    }
}
