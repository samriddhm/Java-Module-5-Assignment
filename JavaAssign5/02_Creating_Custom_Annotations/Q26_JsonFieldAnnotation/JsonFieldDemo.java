import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Q26 demo: serialize Employee objects to JSON using the names given by @JsonField.
 *
 * Run:  javac *.java && java JsonFieldDemo
 */
public class JsonFieldDemo {

    public static void main(String[] args) {
        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("desk", 42);
        extra.put("remote", true);

        Employee e1 = new Employee(101, "Shreyas", "Murgai", "shrey@example.com", "Dr. Lee",
                true, Employee.Level.JUNIOR, Arrays.asList("Java", "Python", "Security"),
                new Address("12 George St", "Sydney", "2000"), extra, "s3cr3t!");

        Employee e2 = new Employee(102, "Mia", "O\"Brien", null, null,
                false, Employee.Level.SENIOR, Arrays.asList(), null, new LinkedHashMap<>(), "hunter2");

        System.out.println("Employee 1 as JSON:");
        System.out.println(JsonSerializer.toJson(e1));
        System.out.println();
        System.out.println("Employee 2 as JSON (null email kept, null manager_name skipped, quote escaped):");
        System.out.println(JsonSerializer.toJson(e2));
        System.out.println();
        System.out.println("Notice: the 'password' field never appears because it has no @JsonField.");
    }
}
