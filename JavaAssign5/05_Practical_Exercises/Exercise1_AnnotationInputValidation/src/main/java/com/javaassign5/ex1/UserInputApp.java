package com.javaassign5.ex1;

import com.javaassign5.ex1.form.FormFiller;
import com.javaassign5.ex1.model.UserRegistration;
import com.javaassign5.ex1.validation.Validator;
import com.javaassign5.ex1.validation.Violation;

import java.util.List;
import java.util.Scanner;

/**
 * Practical Exercise 1 - entry point.
 *
 *   mvn -q compile exec:java                        -> type your own answers
 *   mvn -q compile exec:java -Dexec.args=demo       -> scripted answers (some wrong on purpose)
 */
public class UserInputApp {

    /** Scripted answers for demo mode: each wrong answer is followed by a corrected one. */
    private static final String DEMO_INPUT = String.join("\n",
            "ab",                    // username too short
            "shrey!",                // invalid character
            "shrey_m",               // OK
            "shrey.example.com",     // not an e-mail
            "shrey@example.com",     // OK
            "eighteen",              // not a number
            "8",                     // too young
            "19",                    // OK
            "password",              // no digit
            "s3cure",                // too short
            "s3curePass",            // OK
            "12ab",                  // invalid phone
            "+61 412 345 678"        // OK
    ) + "\n";

    public static void main(String[] args) throws Exception {
        boolean demo = args.length > 0 && (args[0].equals("demo") || args[0].equals("--demo"));
        Scanner in = demo ? new Scanner(DEMO_INPUT) : new Scanner(System.in);

        System.out.println("=== User Registration (rules come from annotations) ===");
        if (demo) System.out.println("(demo mode - answers are typed automatically)");

        FormFiller filler = new FormFiller(in, System.out, demo);
        UserRegistration user = filler.fill(UserRegistration.class);

        // Final check of the whole object (defence in depth)
        List<Violation> problems = Validator.validate(user);
        System.out.println();
        if (problems.isEmpty()) {
            System.out.println("Registration successful!");
            System.out.println(user);
        } else {
            System.out.println("Registration rejected:");
            problems.forEach(p -> System.out.println("  - " + p));
        }
    }
}
