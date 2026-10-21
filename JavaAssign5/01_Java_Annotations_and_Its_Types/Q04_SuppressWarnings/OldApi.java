/**
 * Q4 helper class: contains a deprecated method.
 * Calling it from ANOTHER class produces a "deprecation" warning.
 */
public class OldApi {

    /** @deprecated use {@link #newGreeting()} instead. */
    @Deprecated
    public static String oldGreeting() {
        return "Hello from the OLD method";
    }

    public static String newGreeting() {
        return "Hello from the NEW method";
    }
}
