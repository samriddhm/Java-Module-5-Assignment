package com.javaassign5.q34;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Q34: uses the two libraries added in pom.xml.
 * Without the <dependency> entries, these imports would not compile.
 *
 * Run:  mvn compile exec:java
 */
public class App {

    public static void main(String[] args) {
        // ---- commons-lang3 ----
        String messy = "   maven   makes   dependencies   easy   ";
        System.out.println("commons-lang3 StringUtils:");
        System.out.println("  normalizeSpace : '" + StringUtils.normalizeSpace(messy) + "'");
        System.out.println("  isBlank(\"  \")  : " + StringUtils.isBlank("  "));
        System.out.println("  reverse        : " + StringUtils.reverse("maven"));
        System.out.println("  abbreviate     : " + StringUtils.abbreviate("Dependency management", 15));

        // ---- gson ----
        Map<String, Object> student = new LinkedHashMap<>();
        student.put("name", "Shrey");
        student.put("course", "Software Engineering");
        student.put("year", 1);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(student);
        System.out.println();
        System.out.println("Gson toJson:");
        System.out.println(json);

        Map<?, ?> back = gson.fromJson(json, Map.class);
        System.out.println("Gson fromJson -> name = " + back.get("name"));
    }

    /** Small helper (also used by the unit test): "  hello   world " -> "Hello world". */
    public static String tidy(String s) {
        return StringUtils.capitalize(StringUtils.normalizeSpace(s));
    }
}
