package com.javaassign5.ex2;

import org.apache.commons.text.CaseUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.WordUtils;

import java.io.File;
import java.util.Map;

/**
 * Practical Exercise 2 - transitive dependencies in action.
 *
 * Run:  mvn -q compile exec:exec
 */
public class TransitiveDemo {

    public static void main(String[] args) {
        String sentence = "  maven   downloads   the   dependencies   of   your   dependencies  ";

        System.out.println("1) DIRECT dependency  (commons-text, declared in pom.xml)");
        System.out.println("   WordUtils.capitalizeFully : " + WordUtils.capitalizeFully(sentence.trim().replaceAll("\\s+", " ")));
        System.out.println("   CaseUtils.toCamelCase     : " + CaseUtils.toCamelCase("transitive dependency demo", false));
        System.out.println("   StringSubstitutor         : " + new StringSubstitutor(
                Map.of("tool", "Maven", "lib", "commons-lang3")).replace("${tool} added ${lib} for us"));
        System.out.println("   loaded from               : " + jarOf(WordUtils.class));

        System.out.println();
        System.out.println("2) TRANSITIVE dependency (commons-lang3, NOT declared in pom.xml)");
        try {
            System.out.println("   StringUtils.abbreviate    : " + LangFeatures.summary(sentence));
            System.out.println("   ArrayUtils.reverse        : " + LangFeatures.reversedWords("one two three"));
            System.out.println("   StringUtils.isNumeric     : " + LangFeatures.looksNumeric("2026"));
            System.out.println("   loaded from               : " + jarOf(LangFeatures.mainClass()));
        } catch (NoClassDefFoundError e) {
            System.out.println("   FAILED: " + e + "");
            System.out.println("   -> commons-lang3 is missing. It was excluded, so the transitive link is broken.");
        }

        System.out.println();
        System.out.println("3) Runtime classpath that Maven built for us:");
        for (String entry : System.getProperty("java.class.path").split(File.pathSeparator)) {
            String name = entry.substring(entry.lastIndexOf(File.separatorChar) + 1);
            String kind;
            if (name.startsWith("commons-text")) kind = "DECLARED";
            else if (name.endsWith(".jar")) kind = "TRANSITIVE";
            else kind = "our compiled classes";
            System.out.printf("   %-28s %s%n", name.isEmpty() ? entry : name, kind);
        }
    }

    static String jarOf(Class<?> c) {
        String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
