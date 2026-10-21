/** Q22: example class using only the required elements of @ClassInfo. */
@ClassInfo(author = "Alex Chen", version = "1.0.0")
public class PaymentProcessor {
    public boolean pay(double amount) {
        return amount > 0;
    }
}
