import java.util.ArrayList;
import java.util.List;

/** Q28: the class being tested. */
public class ShoppingCart {

    private final List<Double> prices = new ArrayList<>();

    public void add(double price) {
        if (price < 0) throw new IllegalArgumentException("price cannot be negative");
        prices.add(price);
    }

    public int count() {
        return prices.size();
    }

    public double total() {
        double sum = 0;
        for (double p : prices) sum += p;
        return sum;
    }

    /** Deliberately buggy: a 10% discount should multiply by 0.9, not 0.8. */
    public double totalWithDiscount() {
        return total() * 0.8;
    }
}
