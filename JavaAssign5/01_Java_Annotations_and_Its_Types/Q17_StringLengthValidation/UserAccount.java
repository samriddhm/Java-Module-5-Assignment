/** Q17: model class whose String fields are checked with @Length. */
public class UserAccount {

    @Length(min = 3, max = 15)
    private String username;

    @Length(min = 8, message = "password must have at least {min} characters")
    private String password;

    @Length(max = 50)
    private String bio;

    private String nickname;   // no rule

    public UserAccount(String username, String password, String bio, String nickname) {
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        String shortBio = (bio != null && bio.length() > 20) ? bio.substring(0, 20) + "..." : bio;
        return "UserAccount{username='" + username + "', bio='" + shortBio + "'}";
    }
}
