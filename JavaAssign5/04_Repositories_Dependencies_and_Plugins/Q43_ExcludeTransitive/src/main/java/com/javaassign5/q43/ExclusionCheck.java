package com.javaassign5.q43;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;

/**
 * Q43: proves the exclusion worked.
 * The commons-logging API classes still exist (HttpClient needs them), but they are
 * loaded from jcl-over-slf4j-*.jar instead of the excluded commons-logging-*.jar.
 *
 * Run:  mvn compile exec:exec      (no network call is made)
 */
public class ExclusionCheck {

    public static void main(String[] args) {
        HttpGet request = new HttpGet("https://example.com/api");
        System.out.println("HttpClient works      : built request " + request.getRequestLine());

        System.out.println("LogFactory loaded from: " + jarOf(LogFactory.class));
        System.out.println("HttpGet loaded from   : " + jarOf(HttpGet.class));

        Log log = LogFactory.getLog(ExclusionCheck.class);
        log.info("this commons-logging call is printed by SLF4J (slf4j-simple)");

        boolean realCommonsLogging = jarOf(LogFactory.class).startsWith("commons-logging");
        System.out.println("Original commons-logging jar on classpath? " + realCommonsLogging);
    }

    private static String jarOf(Class<?> c) {
        String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
