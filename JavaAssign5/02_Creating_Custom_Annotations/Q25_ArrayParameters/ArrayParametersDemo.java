import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Q25 demo: reading array values from annotations and using them for access control.
 *
 * Run:  javac *.java && java ArrayParametersDemo
 */
public class ArrayParametersDemo {

    public static void main(String[] args) throws Exception {
        Method[] methods = OrderApi.class.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        System.out.println("1) Reading the arrays");
        for (Method m : methods) {
            Endpoint ep = m.getAnnotation(Endpoint.class);
            AllowedRoles roles = m.getAnnotation(AllowedRoles.class);
            System.out.println("   " + m.getName() + "()");
            System.out.println("      roles        = " + Arrays.toString(roles.value()));
            System.out.println("      paths        = " + Arrays.toString(ep.paths()));
            System.out.println("      methods      = " + Arrays.toString(ep.methods()));
            System.out.println("      successCodes = " + Arrays.toString(ep.successCodes()));
            System.out.println("      handles      = " + Arrays.toString(
                    Arrays.stream(ep.handles()).map(Class::getSimpleName).toArray()));
        }

        System.out.println();
        System.out.println("2) Using the String[] for role-based access control");
        OrderApi api = new OrderApi();
        String[][] users = {{"maria", "MANAGER"}, {"tom", "CLERK"}};
        for (String[] user : users) {
            for (Method m : methods) {
                List<String> allowed = Arrays.asList(m.getAnnotation(AllowedRoles.class).value());
                if (allowed.contains(user[1])) {
                    System.out.println("   " + user[0] + " (" + user[1] + ") -> " + m.getName() + "(): "
                            + m.invoke(api));
                } else {
                    System.out.println("   " + user[0] + " (" + user[1] + ") -> " + m.getName()
                            + "(): ACCESS DENIED (needs one of " + allowed + ")");
                }
            }
        }
    }
}
