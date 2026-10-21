package com.javaassign5.q46;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Q46: proves two plugin settings from pom.xml took effect:
 *   - exec-maven-plugin passed the argument "configured-in-pom"
 *   - maven-compiler-plugin used "-parameters", so real parameter names are kept
 *     (without it, reflection would show arg0, arg1).
 *
 * Run:  mvn compile exec:java
 */
public class PluginDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("Argument from exec-maven-plugin config: "
                + (args.length > 0 ? args[0] : "(none)"));

        Method m = PluginDemo.class.getMethod("greet", String.class, int.class);
        StringBuilder names = new StringBuilder();
        for (Parameter p : m.getParameters()) names.append(p.getName()).append(' ');
        System.out.println("Parameter names seen by reflection   : " + names.toString().trim()
                + "   (real names -> '-parameters' compiler flag worked)");
        System.out.println(greet("Shrey", 3));
    }

    public static String greet(String studentName, int times) {
        return ("Hi " + studentName + "! ").repeat(times).trim();
    }
}
