import java.util.ArrayList;
import java.util.List;

/**
 * Q4 - AFTER: the same code as WithWarnings.java, but each warning is
 * silenced with @SuppressWarnings, placed on the SMALLEST possible scope.
 *
 * Compile it with:  javac -Xlint:all SuppressWarningsDemo.java OldApi.java
 * and NO warnings are printed for this class.
 */
public class SuppressWarningsDemo {

    // 1) Suppress two warning types on one method using an array of names.
    @SuppressWarnings({"rawtypes", "unchecked"})
    static List<String> legacyList() {
        List raw = new ArrayList();
        raw.add("apple");
        raw.add("banana");
        return raw;
    }

    // 2) Suppress a single warning type (deprecation) on one method.
    @SuppressWarnings("deprecation")
    static String callOldApi() {
        return OldApi.oldGreeting();
    }

    // 3) Suppress on a single local variable declaration (narrowest scope).
    static int sumOfLegacy() {
        @SuppressWarnings("unchecked")
        List<Integer> numbers = (List<Integer>) (Object) new ArrayList<>(List.of(1, 2, 3));
        int total = 0;
        for (int n : numbers) total += n;
        return total;
    }

    public static void main(String[] args) {
        System.out.println("legacyList()  -> " + legacyList());
        System.out.println("callOldApi()  -> " + callOldApi());
        System.out.println("sumOfLegacy() -> " + sumOfLegacy());
        System.out.println("Compiled with -Xlint:all and no warnings for this class.");
    }
}
