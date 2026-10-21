import java.util.ArrayList;
import java.util.List;

/**
 * Q4 - BEFORE: this class has NO @SuppressWarnings.
 * Compile it with:  javac -Xlint:all WithWarnings.java OldApi.java
 * and the compiler prints "unchecked", "rawtypes" and "deprecation" warnings.
 */
public class WithWarnings {

    static List<String> legacyList() {
        List raw = new ArrayList();   // warning: [rawtypes]
        raw.add("apple");             // warning: [unchecked]
        return raw;                   // warning: [unchecked] conversion
    }

    static String callOldApi() {
        return OldApi.oldGreeting();  // warning: [deprecation]
    }

    public static void main(String[] args) {
        System.out.println(legacyList());
        System.out.println(callOldApi());
    }
}
