import java.lang.annotation.Annotation;

/**
 * Q27 demo: annotations cannot use `extends`, but:
 *   (a) every annotation type implicitly extends java.lang.annotation.Annotation, and
 *   (b) you can get "inheritance-like" reuse with META-ANNOTATIONS (composition).
 *
 * Run:  javac *.java && java AnnotationExtendDemo
 */
public class AnnotationExtendDemo {

    @Role("USER")
    static class ReportPage { }

    @AdminRole(department = "Finance")
    static class SettingsPage { }

    static class PublicPage { }

    public static void main(String[] args) {
        System.out.println("(a) Implicit super-interface of an annotation type:");
        for (Class<?> superIface : AdminRole.class.getInterfaces()) {
            System.out.println("    AdminRole implements " + superIface.getName());
        }
        System.out.println("    AdminRole.class.isAnnotation() = " + AdminRole.class.isAnnotation());
        System.out.println();

        System.out.println("(b) Finding @Role directly OR through a composed annotation:");
        for (Class<?> page : new Class<?>[]{ReportPage.class, SettingsPage.class, PublicPage.class}) {
            Role role = findRole(page);
            System.out.println("    " + page.getSimpleName() + " -> "
                    + (role == null ? "no role required" : "role=" + role.value() + ", level=" + role.level()));
        }
    }

    /** Looks for @Role on the class, or on any annotation that is on the class (one level deep). */
    static Role findRole(Class<?> c) {
        Role direct = c.getAnnotation(Role.class);
        if (direct != null) return direct;
        for (Annotation a : c.getAnnotations()) {
            Role meta = a.annotationType().getAnnotation(Role.class);
            if (meta != null) return meta;
        }
        return null;
    }
}
