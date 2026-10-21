package com.javaassign5.q45;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Q45: the main class written into the JAR manifest (Main-Class).
 *
 * Build:  mvn clean package
 * Run:    java -jar target/q45-create-jar-1.0.0-all.jar Hello Maven
 */
public class JarApp {

    public static void main(String[] args) {
        String location = JarApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println("Running from : " + location.substring(location.lastIndexOf('/') + 1));
        System.out.println("App version  : " + JarApp.class.getPackage().getImplementationVersion());
        System.out.println("Java         : " + SystemUtils.JAVA_VERSION);
        String text = args.length == 0 ? "no arguments given" : String.join(" ", args);
        System.out.println("Your words   : " + StringUtils.center(text.toUpperCase(), 30, '*'));
    }
}
