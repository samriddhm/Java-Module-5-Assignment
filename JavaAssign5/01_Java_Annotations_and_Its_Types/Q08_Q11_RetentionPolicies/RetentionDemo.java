import java.lang.annotation.Annotation;

/**
 * Q8 + Q11 demo: which annotations survive until runtime?
 *
 * Run:  javac *.java && java RetentionDemo
 * Then: javap -v AnnotatedService.class   (see run.sh) to look inside the bytecode.
 */
public class RetentionDemo {

    public static void main(String[] args) {
        Class<AnnotatedService> c = AnnotatedService.class;

        System.out.println("Checking AnnotatedService with reflection:");
        System.out.println("  @SourceLevel  present at runtime? " + c.isAnnotationPresent(SourceLevel.class));
        System.out.println("  @ClassLevel   present at runtime? " + c.isAnnotationPresent(ClassLevel.class));
        System.out.println("  @RuntimeLevel present at runtime? " + c.isAnnotationPresent(RuntimeLevel.class));

        RuntimeLevel r = c.getAnnotation(RuntimeLevel.class);
        System.out.println("  @RuntimeLevel value = \"" + r.value() + "\"");

        Annotation[] all = c.getAnnotations();
        System.out.println("Total annotations visible to reflection: " + all.length);
        for (Annotation a : all) {
            System.out.println("  -> " + a.annotationType().getSimpleName());
        }

        System.out.println();
        System.out.println("Conclusion: SOURCE is gone after compiling, CLASS is in the .class file");
        System.out.println("but invisible to reflection, and only RUNTIME can be read at runtime.");
    }
}
