package com.javaassign5.ex2;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Uses commons-lang3 - a library that is NOT in our pom.xml.
 * It is on the classpath only because commons-text depends on it (transitive dependency).
 * Kept in its own class so a missing jar shows up as a clear runtime error.
 */
public final class LangFeatures {

    private LangFeatures() { }

    public static String summary(String text) {
        return StringUtils.abbreviate(StringUtils.normalizeSpace(text), 30);
    }

    public static String reversedWords(String text) {
        String[] words = StringUtils.split(text);
        ArrayUtils.reverse(words);
        return String.join(" ", words);
    }

    public static boolean looksNumeric(String s) {
        return StringUtils.isNumeric(s);
    }

    public static Class<?> mainClass() {
        return StringUtils.class;
    }
}
