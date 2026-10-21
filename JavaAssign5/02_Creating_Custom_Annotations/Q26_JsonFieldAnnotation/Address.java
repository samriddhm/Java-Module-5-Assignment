/** Q26: nested object - also uses @JsonField. */
public class Address {

    @JsonField("street_name")
    private final String street;

    @JsonField
    private final String city;

    @JsonField("post_code")
    private final String postCode;

    public Address(String street, String city, String postCode) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
    }
}
