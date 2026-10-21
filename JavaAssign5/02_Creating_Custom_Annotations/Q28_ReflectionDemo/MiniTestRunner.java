import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Q28 demo: a MINI UNIT-TEST FRAMEWORK built only with custom annotations + reflection.
 * This is how JUnit works underneath:
 *   1. find methods with @BeforeEachTest and @MyTest,
 *   2. for each test: create a new instance, run the @BeforeEachTest methods, invoke the test,
 *   3. use @MyTest.expected and @Skip to decide PASS / FAIL / SKIPPED.
 *
 * Run:  javac *.java && java MiniTestRunner
 */
public class MiniTestRunner {

    public static void main(String[] args) throws Exception {
        run(ShoppingCartTests.class);
    }

    static void run(Class<?> testClass) throws Exception {
        System.out.println("Running tests in " + testClass.getSimpleName());
        System.out.println("------------------------------------------------------------");

        List<Method> beforeEach = new ArrayList<>();
        List<Method> tests = new ArrayList<>();
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(BeforeEachTest.class)) beforeEach.add(m);
            if (m.isAnnotationPresent(MyTest.class)) tests.add(m);
        }
        tests.sort(Comparator.comparing(Method::getName));

        int passed = 0, failed = 0, skipped = 0;
        for (Method test : tests) {
            MyTest info = test.getAnnotation(MyTest.class);
            String label = info.name().isEmpty() ? test.getName() : info.name();

            Skip skip = test.getAnnotation(Skip.class);
            if (skip != null) {
                System.out.printf("[SKIP] %-36s reason: %s%n", label, skip.value());
                skipped++;
                continue;
            }

            Object instance = testClass.getDeclaredConstructor().newInstance();   // fresh object per test
            for (Method b : beforeEach) b.invoke(instance);

            Class<? extends Throwable> expected = info.expected();
            boolean expectsException = expected != MyTest.None.class;
            try {
                test.invoke(instance);
                if (expectsException) {
                    System.out.printf("[FAIL] %-36s expected %s but nothing was thrown%n",
                            label, expected.getSimpleName());
                    failed++;
                } else {
                    System.out.printf("[PASS] %s%n", label);
                    passed++;
                }
            } catch (InvocationTargetException wrapped) {
                Throwable actual = wrapped.getCause();          // the real exception from the test
                if (expectsException && expected.isInstance(actual)) {
                    System.out.printf("[PASS] %-36s (threw %s as expected)%n", label, actual.getClass().getSimpleName());
                    passed++;
                } else {
                    System.out.printf("[FAIL] %-36s %s: %s%n", label,
                            actual.getClass().getSimpleName(), actual.getMessage());
                    failed++;
                }
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Total: " + tests.size() + " | Passed: " + passed + " | Failed: " + failed
                + " | Skipped: " + skipped);
        System.out.println("Methods without @MyTest that were ignored: " + Arrays.stream(testClass.getDeclaredMethods())
                .filter(m -> !m.isAnnotationPresent(MyTest.class) && !m.isAnnotationPresent(BeforeEachTest.class)
                        && !m.getName().equals("check"))
                .map(Method::getName).collect(java.util.stream.Collectors.toList()));
    }
}
