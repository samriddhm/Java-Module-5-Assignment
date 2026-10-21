package com.javaassign5.ex1.validation;

import com.javaassign5.ex1.form.FormFiller;
import com.javaassign5.ex1.model.UserRegistration;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Unit tests for the annotation-driven validator and form. Run with: mvn test */
class ValidatorTest {

    @Test
    void validUserHasNoViolations() {
        UserRegistration u = new UserRegistration("shrey_m", "shrey@example.com", 19, "s3curePass", "");
        assertTrue(Validator.validate(u).isEmpty());
    }

    @Test
    void everyRuleIsReported() {
        UserRegistration u = new UserRegistration("a!", "not-an-email", 5, "short", "12ab");
        List<String> rules = Validator.validate(u).stream()
                .map(v -> v.getField() + ":" + v.getRule())
                .collect(Collectors.toList());

        assertTrue(rules.contains("username:Length"));
        assertTrue(rules.contains("username:Pattern"));
        assertTrue(rules.contains("email:Email"));
        assertTrue(rules.contains("age:Range"));
        assertTrue(rules.contains("password:Length"));
        assertTrue(rules.contains("password:Pattern"));
        assertTrue(rules.contains("phone:Pattern"));
        assertEquals(7, rules.size());
    }

    @Test
    void blankRequiredFieldOnlyReportsNotBlank() {
        UserRegistration u = new UserRegistration("   ", "shrey@example.com", 19, "s3curePass", null);
        List<Violation> v = Validator.validate(u);
        assertEquals(1, v.size());
        assertEquals("NotBlank", v.get(0).getRule());
    }

    @Test
    void formReAsksUntilValid() throws Exception {
        String input = "ab\nshrey_m\nshrey@example.com\nabc\n30\npassw0rd123\n\n";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        FormFiller filler = new FormFiller(new Scanner(input), new PrintStream(buffer), false);

        UserRegistration u = filler.fill(UserRegistration.class);

        assertEquals("shrey_m", u.getUsername());
        assertEquals(30, u.getAge());
        String printed = buffer.toString();
        assertTrue(printed.contains("username must be between 3 and 15 characters"));
        assertTrue(printed.contains("age must be a whole number"));
    }
}
