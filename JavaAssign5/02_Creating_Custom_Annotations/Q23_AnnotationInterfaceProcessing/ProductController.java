/** Q23: class whose methods carry the custom annotations. */
@Permission(role = "USER")
public class ProductController {

    @Permission(role = "USER")
    @Cacheable(seconds = 300, keys = {"productId"})
    public String getProduct(int productId) {
        return "Product #" + productId;
    }

    @Permission(role = "ADMIN", level = 5)
    @Deprecated
    public void deleteProduct(int productId) { }

    public void healthCheck() { }
}
