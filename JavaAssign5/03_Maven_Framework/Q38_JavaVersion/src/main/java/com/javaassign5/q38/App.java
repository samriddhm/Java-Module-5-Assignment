package com.javaassign5.q38;

/**
 * Q38: this file only compiles when Maven is set to Java 17+ (maven.compiler.release=17),
 * because it uses: a record (Java 16), a switch expression (Java 14) and a text block (Java 15).
 *
 * Run:  mvn compile exec:java
 */
public class App {

    /** record = Java 16+ */
    record Course(String code, int credits) { }

    public static void main(String[] args) {
        Course c = new Course("SOFT1902", 6);

        // switch expression = Java 14+
        String load = switch (c.credits()) {
            case 6 -> "standard unit";
            case 12 -> "double unit";
            default -> "other";
        };

        // text block = Java 15+
        String report = """
                Java version running this app : %s
                Compiled for Java (class file) : %d
                Course : %s (%s)
                """.formatted(System.getProperty("java.version"), classFileJavaVersion(), c, load);

        System.out.print(report);
    }

    /** Reads this class's own .class file header to find which Java version it was compiled for. */
    private static int classFileJavaVersion() {
        try (var in = new java.io.DataInputStream(App.class.getResourceAsStream("App.class"))) {
            in.readInt();                 // magic number 0xCAFEBABE
            in.readUnsignedShort();       // minor version
            int major = in.readUnsignedShort();
            return major - 44;            // 61 -> Java 17, 65 -> Java 21
        } catch (Exception e) {
            return -1;
        }
    }
}
