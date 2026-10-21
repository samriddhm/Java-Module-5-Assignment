package com.javaassign5.ex1.model;

import com.javaassign5.ex1.annotations.Email;
import com.javaassign5.ex1.annotations.Length;
import com.javaassign5.ex1.annotations.NotBlank;
import com.javaassign5.ex1.annotations.Pattern;
import com.javaassign5.ex1.annotations.Prompt;
import com.javaassign5.ex1.annotations.Range;

/**
 * The user input form. ALL rules and even the questions shown to the user
 * are declared with annotations - there is no validation code in this class.
 */
public class UserRegistration {

    @Prompt(value = "Username (3-15 letters, digits or _)", order = 1)
    @NotBlank
    @Length(min = 3, max = 15)
    @Pattern(regex = "[A-Za-z0-9_]+", message = "may only contain letters, digits and _")
    private String username;

    @Prompt(value = "E-mail address", order = 2)
    @NotBlank
    @Email
    private String email;

    @Prompt(value = "Age", order = 3)
    @Range(min = 13, max = 120, message = "must be between {min} and {max} years")
    private int age;

    @Prompt(value = "Password (8+ characters, at least one digit)", order = 4)
    @NotBlank
    @Length(min = 8, max = 64)
    @Pattern(regex = ".*\\d.*", message = "must contain at least one digit")
    private String password;

    @Prompt(value = "Phone number (optional - press Enter to skip)", order = 5)
    @Pattern(regex = "\\+?[0-9 ]{8,15}", message = "must be 8-15 digits, may start with +")
    private String phone;

    public UserRegistration() { }

    public UserRegistration(String username, String email, int age, String password, String phone) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.password = password;
        this.phone = phone;
    }

    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public int getAge()         { return age; }
    public String getPhone()    { return phone; }

    @Override
    public String toString() {
        return "UserRegistration{username='" + username + "', email='" + email + "', age=" + age
                + ", password='" + (password == null ? "" : "*".repeat(password.length())) + "'"
                + ", phone='" + (phone == null || phone.isEmpty() ? "-" : phone) + "'}";
    }
}
