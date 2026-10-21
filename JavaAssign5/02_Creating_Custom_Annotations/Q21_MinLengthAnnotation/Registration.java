/** Q21: a sign-up form whose fields use @MinLength. */
public class Registration {

    @MinLength(3)
    private String username;

    @MinLength(value = 8, message = "is too weak: needs {min}+ characters (got {actual})")
    private String password;

    @MinLength(value = 10, trim = false)
    private String address;

    public Registration(String username, String password, String address) {
        this.username = username;
        this.password = password;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Registration{username='" + username + "', password='" + "*".repeat(
                password == null ? 0 : password.length()) + "', address='" + address + "'}";
    }
}
