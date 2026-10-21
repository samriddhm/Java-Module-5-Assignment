import java.io.IOException;

/** Q25: methods using array-valued annotation elements. */
public class OrderApi {

    @AllowedRoles({"ADMIN", "MANAGER", "CLERK"})
    @Endpoint(paths = {"/orders", "/orders/all"})
    public String listOrders() { return "all orders"; }

    @AllowedRoles({"ADMIN", "MANAGER"})
    @Endpoint(paths = "/orders/new",                                   // single value - braces optional
              methods = {Endpoint.HttpMethod.POST, Endpoint.HttpMethod.PUT},
              successCodes = {200, 201},
              handles = {IOException.class, IllegalArgumentException.class})
    public String createOrder() { return "order created"; }

    @AllowedRoles("ADMIN")                                              // single value - braces optional
    @Endpoint(paths = "/orders/delete", methods = Endpoint.HttpMethod.DELETE, successCodes = 204)
    public String deleteOrder() { return "order deleted"; }
}
