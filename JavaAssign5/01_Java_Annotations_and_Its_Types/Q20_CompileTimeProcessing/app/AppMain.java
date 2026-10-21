/**
 * Q20: uses GeneratedTodoList - a class that does NOT exist in the source folder.
 * It is written by CompileTimeProcessor during compilation.
 */
public class AppMain {
    public static void main(String[] args) {
        new OrderService().placeOrder("laptop");
        System.out.println(new Point(3, 4));

        System.out.println("TODO list generated at compile time:");
        for (String item : GeneratedTodoList.ITEMS) {
            System.out.println("  - " + item);
        }
    }
}
