package com.javaassign5.q41;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

/**
 * Q41: prints WHICH jar each library was really loaded from,
 * proving which version Maven chose when there was a conflict.
 *
 * Run:  mvn compile exec:exec
 */
public class VersionReport {

    public static void main(String[] args) {
        System.out.println("commons-text  loaded from: " + jarOf(WordUtils.class));
        System.out.println("commons-lang3 loaded from: " + jarOf(StringUtils.class));
        System.out.println("commons-lang3 version    : " + StringUtils.class.getPackage().getImplementationVersion());
        System.out.println();
        System.out.println("Both libraries work together: " + WordUtils.capitalizeFully(StringUtils.reverse("ecnatsni")));
    }

    private static String jarOf(Class<?> c) {
        String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
