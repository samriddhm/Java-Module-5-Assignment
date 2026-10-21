/** Q20: uses @Todo - the processor reports these while compiling. */
@Todo("split this class into OrderReader and OrderWriter")
public class OrderService {

    @Todo("add input validation")
    public void placeOrder(String item) {
        System.out.println("Order placed for " + item);
    }

    @Todo("send a confirmation email")
    public void cancelOrder(String item) {
        System.out.println("Order cancelled for " + item);
    }
}
