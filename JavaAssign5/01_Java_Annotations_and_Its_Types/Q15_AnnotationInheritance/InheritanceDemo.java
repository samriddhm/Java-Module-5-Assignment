/**
 * Q15 demo: Can annotations be inherited?
 *
 * Rules shown:
 *  1. Only CLASS-level annotations marked with @Inherited are inherited by subclasses.
 *  2. Annotations without @Inherited are not inherited.
 *  3. getAnnotation() sees inherited ones, getDeclaredAnnotation() does not.
 *  4. A subclass can "override" by declaring the annotation itself.
 *  5. Annotations on interfaces are NOT inherited by implementing classes.
 *  6. Annotations on methods are NOT inherited by overriding methods (even with @Inherited).
 *
 * Run:  javac *.java && java InheritanceDemo
 */
public class InheritanceDemo {

    public static void main(String[] args) throws Exception {
        show("Animal", Animal.class);
        show("Dog   ", Dog.class);
        show("Puppy ", Puppy.class);
        show("Cat   ", Cat.class);

        System.out.println();
        System.out.println("getDeclaredAnnotation on Dog (only what is written on Dog itself): "
                + Dog.class.getDeclaredAnnotation(InheritedTag.class));

        System.out.println();
        System.out.println("Method annotations:");
        System.out.println("  Animal.speak() @InheritedTag = "
                + value(Animal.class.getMethod("speak").getAnnotation(InheritedTag.class)));
        System.out.println("  Dog.speak()    @InheritedTag = "
                + value(Dog.class.getMethod("speak").getAnnotation(InheritedTag.class))
                + "   <- overriding methods never inherit annotations");
    }

    private static void show(String label, Class<?> c) {
        System.out.println(label + " -> @InheritedTag: " + pad(value(c.getAnnotation(InheritedTag.class)))
                + " | @PlainTag: " + value(c.getAnnotation(PlainTag.class)));
    }

    private static String value(InheritedTag t) { return t == null ? "none" : "\"" + t.value() + "\""; }
    private static String value(PlainTag t)     { return t == null ? "none" : "\"" + t.value() + "\""; }

    private static String pad(String s) { return String.format("%-14s", s); }
}
