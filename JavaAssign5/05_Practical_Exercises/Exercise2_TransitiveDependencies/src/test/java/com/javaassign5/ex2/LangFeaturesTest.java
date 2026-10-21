package com.javaassign5.ex2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs on JUnit - whose API, engine, opentest4j etc. are themselves transitive
 * dependencies of the single "junit-jupiter" artifact declared in pom.xml.
 */
class LangFeaturesTest {

    @Test
    void reversesWords() {
        assertEquals("three two one", LangFeatures.reversedWords("one two three"));
    }

    @Test
    void abbreviatesLongText() {
        String s = LangFeatures.summary("a   very   long   sentence   that   keeps   going   on");
        assertTrue(s.endsWith("..."));
        assertEquals(30, s.length());
    }

    @Test
    void transitiveClassComesFromCommonsLang3Jar() {
        assertTrue(TransitiveDemo.jarOf(LangFeatures.mainClass()).startsWith("commons-lang3"));
    }
}
