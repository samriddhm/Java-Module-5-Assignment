/**
 * Q19: a public API class that uses both annotations.
 * In the javadoc HTML only {@code @ApiInfo} appears in the class signature.
 */
@ApiInfo(since = "2.0", owner = "Payments Team")
@InternalNote("TODO: refactor the retry logic")
public class PaymentGateway {

    /**
     * Charges an amount.
     * @param amount the amount in dollars
     * @return true if the charge succeeded
     */
    @ApiInfo(since = "2.1", owner = "Payments Team")
    @InternalNote("uses the legacy bank API")
    public boolean charge(double amount) {
        return amount > 0;
    }
}
