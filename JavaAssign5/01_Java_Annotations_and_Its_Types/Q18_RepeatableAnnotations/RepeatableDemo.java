import java.lang.reflect.Method;

/**
 * Q18 demo: reading repeatable annotations.
 *
 * KEY POINT: when an annotation is repeated, the compiler wraps the copies in the
 * container (@Schedules). So getAnnotation(Schedule.class) returns NULL for a
 * repeated annotation - use getAnnotationsByType(Schedule.class) instead.
 *
 * Run:  javac *.java && java RepeatableDemo
 */
public class RepeatableDemo {

    public static void main(String[] args) throws Exception {
        for (String name : new String[]{"fullBackup", "cleanTempFiles", "oldStyleReport"}) {
            Method m = BackupJobs.class.getMethod(name);
            System.out.println(name + "()");
            System.out.println("   getAnnotation(Schedule.class)  = " + m.getAnnotation(Schedule.class));
            System.out.println("   getAnnotation(Schedules.class) = "
                    + (m.getAnnotation(Schedules.class) == null ? "null" : "container with "
                    + m.getAnnotation(Schedules.class).value().length + " item(s)"));

            Schedule[] all = m.getAnnotationsByType(Schedule.class);   // works in every case
            System.out.println("   getAnnotationsByType -> " + all.length + " schedule(s):");
            for (Schedule s : all) {
                System.out.printf("      runs every %-9s at %02d:00%n", s.day(), s.hour());
            }
            System.out.println();
        }
    }
}
