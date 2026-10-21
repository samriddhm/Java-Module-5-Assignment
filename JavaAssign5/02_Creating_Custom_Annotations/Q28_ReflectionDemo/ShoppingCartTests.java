/** Q28: test class written with our own annotations - no JUnit needed. */
public class ShoppingCartTests {

    private ShoppingCart cart;

    @BeforeEachTest
    public void setUp() {
        cart = new ShoppingCart();      // fresh cart for every test
    }

    @MyTest(name = "empty cart has zero items")
    public void emptyCart() {
        check(cart.count() == 0, "expected 0 items");
    }

    @MyTest(name = "total adds up prices")
    public void totalAddsUp() {
        cart.add(10.0);
        cart.add(5.5);
        check(cart.total() == 15.5, "expected 15.5 but was " + cart.total());
    }

    @MyTest(name = "negative price is rejected", expected = IllegalArgumentException.class)
    public void negativePrice() {
        cart.add(-1);
    }

    @MyTest(name = "10% discount is applied")
    public void discount() {
        cart.add(100);
        check(cart.totalWithDiscount() == 90.0, "expected 90.0 but was " + cart.totalWithDiscount());
    }

    @MyTest(name = "checkout talks to payment server")
    @Skip("payment server not available in the lab")
    public void checkout() {
        throw new IllegalStateException("should never run");
    }

    public void notATest() {
        throw new IllegalStateException("not annotated - must never be called");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
