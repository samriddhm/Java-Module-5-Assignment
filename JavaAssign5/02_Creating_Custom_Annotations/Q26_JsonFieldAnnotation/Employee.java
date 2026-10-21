import java.util.List;
import java.util.Map;

/** Q26: model class - Java field names are camelCase, JSON keys are snake_case. */
public class Employee {

    enum Level { JUNIOR, SENIOR }

    @JsonField("employee_id")
    private final int id;

    @JsonField("first_name")
    private final String firstName;

    @JsonField("last_name")
    private final String lastName;

    @JsonField                                  // empty -> key is "email"
    private final String email;

    @JsonField(value = "manager_name", includeNull = false)   // skipped when null
    private final String managerName;

    @JsonField("is_active")
    private final boolean active;

    @JsonField
    private final Level level;

    @JsonField
    private final List<String> skills;

    @JsonField("home_address")
    private final Address address;

    @JsonField("extra")
    private final Map<String, Object> extra;

    private final String password;              // NOT annotated -> never written to JSON

    public Employee(int id, String firstName, String lastName, String email, String managerName,
                    boolean active, Level level, List<String> skills, Address address,
                    Map<String, Object> extra, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.managerName = managerName;
        this.active = active;
        this.level = level;
        this.skills = skills;
        this.address = address;
        this.extra = extra;
        this.password = password;
    }
}
