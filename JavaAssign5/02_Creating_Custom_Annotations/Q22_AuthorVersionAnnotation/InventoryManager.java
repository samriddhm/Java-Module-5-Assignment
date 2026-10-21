/** Q22: example class using every element of @ClassInfo. */
@ClassInfo(
        author = "Shreyas Murgai",
        version = "2.1.0",
        date = "2026-10-21",
        reviewers = {"Dr. Smith", "Priya"},
        description = "Keeps track of stock levels")
public class InventoryManager {
    public int stockOf(String item) {
        return item.length() * 10; // dummy logic
    }
}
