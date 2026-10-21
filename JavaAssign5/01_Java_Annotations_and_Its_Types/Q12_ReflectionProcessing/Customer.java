/** Q12: a class that uses annotations on the class, fields, methods and parameters. */
@Table(name = "customers")
public class Customer {

    @Column(name = "customer_id", primaryKey = true)
    private int id = 7;

    @Column(name = "full_name")
    private String name = "Shrey";

    private String temporaryNote = "not saved";   // no @Column -> ignored

    @RunOnStartup(order = 2)
    public void loadCache() {
        System.out.println("    loadCache() ran");
    }

    @RunOnStartup(order = 1)
    public void connect() {
        System.out.println("    connect() ran");
    }

    public void rename(@NotNull String newName, String reason) {
        this.name = newName;
    }
}
