# Creating Custom Annotations — Answers (Q21–Q30)

Every question in this section has a runnable demo folder. Run each one from inside its folder.

| Question | Demo folder | Command |
|---|---|---|
| Q21 | `Q21_MinLengthAnnotation/` | `javac *.java && java MinLengthDemo` |
| Q22 | `Q22_AuthorVersionAnnotation/` | `javac *.java && java ClassInfoDemo` |
| Q23 | `Q23_AnnotationInterfaceProcessing/` | `javac *.java && java AnnotationInterfaceDemo` |
| Q24 | *(theory – compiler errors shown below)* | – |
| Q25 | `Q25_ArrayParameters/` | `javac *.java && java ArrayParametersDemo` |
| Q26 | `Q26_JsonFieldAnnotation/` | `javac *.java && java JsonFieldDemo` |
| Q27 | `Q27_AnnotationCannotExtend/` | `javac *.java && java AnnotationExtendDemo` |
| Q28 | `Q28_ReflectionDemo/` | `javac *.java && java MiniTestRunner` |
| Q29 | `Q29_LogExecutionTime/` | `javac *.java && java LogExecutionTimeDemo` |
| Q30 | `Q30_MissingRetention/` | `javac *.java && java MissingRetentionDemo` |

> Requires JDK 11+ (tested on JDK 21). No external libraries are needed.

---

## Q21. Create a custom annotation `@MinLength` to validate the minimum length of a string in a Java class.

**Folder:** `Q21_MinLengthAnnotation/`

| File | What it is |
|---|---|
| `MinLength.java` | The annotation |
| `MinLengthValidator.java` | Reads `@MinLength` with reflection and checks values |
| `Registration.java` | A sign-up form that uses `@MinLength` |
| `MinLengthDemo.java` | `main` – validates three forms |

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)                     // readable at runtime
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface MinLength {
    int value();                                        // single-value -> @MinLength(5)
    String message() default "must have at least {min} characters but has {actual}";
    boolean trim() default true;                        // ignore surrounding spaces
}
```
```java
public class Registration {
    @MinLength(3)                                                      private String username;
    @MinLength(value = 8, message = "is too weak: needs {min}+ characters (got {actual})")
                                                                        private String password;
    @MinLength(value = 10, trim = false)                               private String address;
}
```
**How the validator works:** loop over `getDeclaredFields()` → `getAnnotation(MinLength.class)` → `setAccessible(true)` → read the value (null counts as `""`) → optionally `trim()` → compare `length()` with `value()` → collect messages. It also throws `IllegalStateException` if `@MinLength` is put on a non-text field or has a negative value. `validateOrThrow()` turns errors into an exception.

**Output:**
```
Registration{username='shrey', password='************', address='12 George St, Sydney'}
   OK - all @MinLength rules passed
Registration{username='  al  ', password='***', address='Sydney'}
   ERROR: username must have at least 3 characters but has 2
   ERROR: password is too weak: needs 8+ characters (got 3)
   ERROR: address must have at least 10 characters but has 6
Registration{username='null', password='', address='null'}
   ERROR: username must have at least 3 characters but has 0
   ERROR: password is too weak: needs 8+ characters (got 0)
   ERROR: address must have at least 10 characters but has 0

validateOrThrow() on an invalid form:
   Caught exception -> username must have at least 3 characters but has 2
```
Note `"  al  "` counts as 2 characters because `trim` defaults to `true`.

---

## Q22. Write a custom annotation to specify the author and version of a class.

**Folder:** `Q22_AuthorVersionAnnotation/` – `ClassInfo.java` (annotation), `InventoryManager.java`, `PaymentProcessor.java`, `LegacyHelper.java` (no annotation), `ClassInfoDemo.java` (main).

```java
@Documented                               // shows in javadoc - author/version is API info
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)                 // classes, interfaces, enums only
public @interface ClassInfo {
    String author();                      // required
    String version();                     // required
    String date() default "unknown";
    String[] reviewers() default {};
    String description() default "";
}

@ClassInfo(author = "Shreyas Murgai", version = "2.1.0", date = "2026-10-21",
           reviewers = {"Dr. Smith", "Priya"}, description = "Keeps track of stock levels")
public class InventoryManager { ... }

@ClassInfo(author = "Alex Chen", version = "1.0.0")
public class PaymentProcessor { ... }
```
The demo prints an "About" report and checks the version matches `MAJOR.MINOR.PATCH`.

**Output:**
```
=== InventoryManager ===
   Author      : Shreyas Murgai
   Version     : 2.1.0  (valid semantic version)
   Date        : 2026-10-21
   Reviewers   : Dr. Smith, Priya
   Description : Keeps track of stock levels
   Major version number = 2
=== PaymentProcessor ===
   Author      : Alex Chen
   Version     : 1.0.0  (valid semantic version)
   Date        : unknown
   Reviewers   : none
   Description : -
   Major version number = 1
=== LegacyHelper ===
   (no @ClassInfo - author and version unknown)
```

---

## Q23. How do you process custom annotations using Java's `Annotation` interface?

**Key idea:** `java.lang.annotation.Annotation` is the **parent interface of every annotation type**. When you write `@interface Permission`, Java makes `Permission` extend `Annotation` automatically. Every annotation object you get from reflection is therefore an `Annotation`, which gives you:

| Method | What it gives |
|---|---|
| `Class<? extends Annotation> annotationType()` | The annotation's type, e.g. `Permission.class` (use this, **not** `getClass()`, which returns a proxy class) |
| `equals(Object)` | `true` if same type **and** same element values |
| `hashCode()` | Based on element values |
| `toString()` | e.g. `@Permission(level=5, role="ADMIN")` |

**Processing steps:**
1. Get annotations as `Annotation[]` with `getAnnotations()` / `getDeclaredAnnotations()`.
2. **Generic processing:** call `a.annotationType()`, then `annotationType().getDeclaredMethods()` to list the elements and `method.invoke(a)` to read each value — works for *any* annotation, even ones you have never seen.
3. **Type-specific processing:** `if (a instanceof Permission) { Permission p = (Permission) a; p.role(); }`.

**Folder:** `Q23_AnnotationInterfaceProcessing/` – annotations `@Permission`, `@Cacheable` on `ProductController`.

```java
for (Annotation a : m.getAnnotations()) {
    Class<? extends Annotation> type = a.annotationType();          // generic
    for (Method el : type.getDeclaredMethods()) {
        Object value = el.invoke(a);                                // read any element
    }
    if (a instanceof Permission) {                                  // type-specific
        Permission p = (Permission) a;
        System.out.println("needs role " + p.role());
    }
}
```

**Output:**
```
PART 1 - Generic processing with the Annotation interface
Method deleteProduct() has 2 annotation(s)
   @Permission {level=5, role=ADMIN}
        toString(): @Permission(level=5, role="ADMIN")
   @Deprecated {forRemoval=false, since=}
        toString(): @java.lang.Deprecated(forRemoval=false, since="")
Method getProduct() has 2 annotation(s)
   @Permission {level=1, role=USER}
        toString(): @Permission(level=1, role="USER")
   @Cacheable {keys=[productId], seconds=300}
        toString(): @Cacheable(keys={"productId"}, seconds=300)
Method healthCheck() has 0 annotation(s)

PART 2 - Type-specific processing (instanceof + cast)
   deleteProduct(): needs role ADMIN (level 5)  <- sensitive!
   getProduct(): needs role USER (level 1)
   getProduct(): cache for 300s, keys [productId]

PART 3 - equals()/hashCode() compare annotation VALUES, not objects
   class @Permission equals getProduct @Permission?   true  (same values: role=USER, level=1)
   class @Permission equals deleteProduct @Permission? false
   hashCodes equal for equal annotations?            true
   Runtime class of the annotation object: a JDK dynamic proxy implementing Permission
```
The generic part even handled the built-in `@Deprecated` without any special code.

---

## Q24. What are the restrictions on defining annotation methods?

Annotation "methods" are really **elements** (named values). They have strict rules:

| # | Rule | Bad example | Compiler error (JDK 21) |
|---|---|---|---|
| 1 | **No parameters** | `String name(int x);` | *elements in annotation interface declarations cannot declare formal parameters* |
| 2 | **No `throws` clause** | `String name() throws Exception;` | *throws clause not allowed in @interface members* |
| 3 | **Not generic** | `<T> T name();` | *elements in annotation interface declarations cannot be generic methods* |
| 4 | **Return type must be one of:** primitive (`int`, `boolean`, …), `String`, `Class` / `Class<?>`, an **enum**, another **annotation**, or a **1-D array** of these | `Object name();`, `List<String> names();`, `Integer n();` | *invalid type for annotation interface element* |
| 5 | **Only 1-dimensional arrays** | `int[][] grid();` | *invalid type for annotation interface element* |
| 6 | **Default values must be compile-time constants** and **never `null`** | `String name() default null;` | *element value must be a constant expression* |
| 7 | **No body** – they are always abstract (until a default is given with `default`, which is still not a body) | `String name() { return ""; }` | *interface abstract methods cannot have body* |
| 8 | **Cannot clash with methods of `Object` or `Annotation`** | `String hashCode();` | *…declares an element with the same name as method hashCode()* |
| 9 | Implicitly `public abstract` – you may not make them `private`, `protected`, `static`, `final` or `default` (in the interface-method sense) | `private int x();` | *modifier private not allowed here* |
| 10 | The annotation type itself **cannot use `extends`** (see Q27), cannot be generic (`@interface A<T>` is illegal), and **cannot contain itself** as an element type (directly or indirectly) | `@interface A { A self(); }`, `@interface G<T> { }` | *type of element A is cyclic* / *annotation interface G cannot be generic* |

**What *is* allowed:** constants (fields, implicitly `public static final`), nested classes/enums/interfaces (like `Review.Status` in Q9, `MyTest.None` in Q28), and the special element name **`value`** (allows the `@X(5)` shortcut).

```java
public @interface Valid {
    int MAX = 100;                       // OK - constant
    enum Mode { STRICT, LENIENT }        // OK - nested enum
    String value();                      // OK - String
    int limit() default MAX;             // OK - constant default
    Mode mode() default Mode.STRICT;     // OK - enum
    Class<?>[] groups() default {};      // OK - 1-D array of Class
    Deprecated meta() default @Deprecated;  // OK - annotation-typed element
}
```

---

## Q25. Can annotations take arrays as parameters? Provide an example.

**Yes.** An element can be a **one-dimensional array** of any allowed type: `String[]`, primitives like `int[]`, enums, `Class<?>[]`, or annotations (Q18's `Schedules` used `Schedule[]`).

**Syntax rules:**
- Several values → braces: `roles = {"ADMIN", "USER"}`
- **One value → braces can be skipped:** `roles = "ADMIN"` means `{"ADMIN"}`
- Empty array: `{}` (also a common default, since `null` isn't allowed)
- If the element is named `value`, you can drop the name: `@AllowedRoles({"ADMIN", "USER"})`
- At runtime the getter returns a **copy** of the array, so callers can't change the annotation.

**Folder:** `Q25_ArrayParameters/`
```java
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface AllowedRoles { String[] value(); }

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface Endpoint {
    enum HttpMethod { GET, POST, PUT, DELETE }
    String[] paths();
    HttpMethod[] methods() default {HttpMethod.GET};
    int[] successCodes() default {200};
    Class<? extends Exception>[] handles() default {};
}

public class OrderApi {
    @AllowedRoles({"ADMIN", "MANAGER", "CLERK"})
    @Endpoint(paths = {"/orders", "/orders/all"})
    public String listOrders() { ... }

    @AllowedRoles({"ADMIN", "MANAGER"})
    @Endpoint(paths = "/orders/new", methods = {Endpoint.HttpMethod.POST, Endpoint.HttpMethod.PUT},
              successCodes = {200, 201}, handles = {IOException.class, IllegalArgumentException.class})
    public String createOrder() { ... }

    @AllowedRoles("ADMIN")                               // one value - no braces
    @Endpoint(paths = "/orders/delete", methods = Endpoint.HttpMethod.DELETE, successCodes = 204)
    public String deleteOrder() { ... }
}
```

**Output:**
```
1) Reading the arrays
   createOrder()
      roles        = [ADMIN, MANAGER]
      paths        = [/orders/new]
      methods      = [POST, PUT]
      successCodes = [200, 201]
      handles      = [IOException, IllegalArgumentException]
   deleteOrder()
      roles        = [ADMIN]
      paths        = [/orders/delete]
      methods      = [DELETE]
      successCodes = [204]
      handles      = []
   listOrders()
      roles        = [ADMIN, MANAGER, CLERK]
      paths        = [/orders, /orders/all]
      methods      = [GET]
      successCodes = [200]
      handles      = []

2) Using the String[] for role-based access control
   maria (MANAGER) -> createOrder(): order created
   maria (MANAGER) -> deleteOrder(): ACCESS DENIED (needs one of [ADMIN])
   maria (MANAGER) -> listOrders(): all orders
   tom (CLERK) -> createOrder(): ACCESS DENIED (needs one of [ADMIN, MANAGER])
   tom (CLERK) -> deleteOrder(): ACCESS DENIED (needs one of [ADMIN])
   tom (CLERK) -> listOrders(): all orders
```

---

## Q26. Create a custom annotation `@JsonField` to specify field names for JSON serialization.

**Folder:** `Q26_JsonFieldAnnotation/`

| File | Role |
|---|---|
| `JsonField.java` | The annotation – `value()` = JSON key, `includeNull()` |
| `JsonSerializer.java` | Turns any object into pretty JSON using `@JsonField` (no libraries) |
| `Employee.java`, `Address.java` | Models – camelCase Java names, snake_case JSON names |
| `JsonFieldDemo.java` | `main` |

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonField {
    String value() default "";          // "" -> use the Java field name
    boolean includeNull() default true; // false -> skip the field when it is null
}

public class Employee {
    @JsonField("employee_id")  private final int id;
    @JsonField("first_name")   private final String firstName;
    @JsonField                 private final String email;          // key = "email"
    @JsonField(value = "manager_name", includeNull = false) private final String managerName;
    @JsonField("home_address") private final Address address;       // nested object
    private final String password;                                  // no annotation -> never output
    ...
}
```
**Serializer logic (core):**
```java
for (Field field : obj.getClass().getDeclaredFields()) {
    JsonField jf = field.getAnnotation(JsonField.class);
    if (jf == null) continue;                                  // opt-in: only annotated fields
    field.setAccessible(true);
    Object v = field.get(obj);
    if (v == null && !jf.includeNull()) continue;
    String key = jf.value().isEmpty() ? field.getName() : jf.value();
    // write "key": value  (recursively for nested objects, lists, maps; strings are escaped)
}
```
This is exactly the idea behind Jackson's `@JsonProperty` and Gson's `@SerializedName`.

**Output:**
```
Employee 1 as JSON:
{
  "employee_id": 101,
  "first_name": "Shreyas",
  "last_name": "Murgai",
  "email": "shrey@example.com",
  "manager_name": "Dr. Lee",
  "is_active": true,
  "level": "JUNIOR",
  "skills": ["Java", "Python", "Security"],
  "home_address": {
    "street_name": "12 George St",
    "city": "Sydney",
    "post_code": "2000"
  },
  "extra": {
    "desk": 42,
    "remote": true
  }
}

Employee 2 as JSON (null email kept, null manager_name skipped, quote escaped):
{
  "employee_id": 102,
  "first_name": "Mia",
  "last_name": "O\"Brien",
  "email": null,
  "is_active": false,
  "level": "SENIOR",
  "skills": [],
  "home_address": null,
  "extra": {}
}

Notice: the 'password' field never appears because it has no @JsonField.
```

---

## Q27. Can a custom annotation extend another annotation? Why or why not?

**No.** An annotation type cannot use `extends` (or `implements`). The compiler rejects it:
```java
public @interface AdminRole extends Role { }
// error: 'extends' not allowed for @interfaces
```

**Why not?**
1. **Every annotation type already extends `java.lang.annotation.Annotation` automatically.** The language reserves that slot – you can't choose another parent.
2. **Keeps annotations simple.** They are meant to be plain, constant metadata. Inheritance would raise hard questions: which `@Target`/`@Retention` wins? What if parent and child define the same element with different defaults? How should `getAnnotation(Role.class)` behave when it finds an `AdminRole`?
3. **Fast, predictable processing.** Compilers, the JVM and tools can read an annotation without walking a type hierarchy.
4. Also note: if you manually write `interface X extends Annotation`, `X` is **not** an annotation type – only `@interface` creates one.

**Workaround – composition with meta-annotations.** Allow the "base" annotation on other annotations (`@Target(ANNOTATION_TYPE)`) and place it on them. Frameworks then look one level up. Spring does this everywhere: `@Service`, `@Repository` and `@Controller` are all annotated with `@Component`; `@SpringBootApplication` is built from three annotations.

**Folder:** `Q27_AnnotationCannotExtend/`
```java
@Retention(RUNTIME) @Target({TYPE, ANNOTATION_TYPE})
public @interface Role { String value(); int level() default 1; }

@Role(value = "ADMIN", level = 10)                 // "inherits" Role by carrying it
@Retention(RUNTIME) @Target(TYPE)
public @interface AdminRole { String department() default "IT"; }

static Role findRole(Class<?> c) {
    Role direct = c.getAnnotation(Role.class);
    if (direct != null) return direct;
    for (Annotation a : c.getAnnotations()) {      // look on the annotations themselves
        Role meta = a.annotationType().getAnnotation(Role.class);
        if (meta != null) return meta;
    }
    return null;
}
```

**Output:**
```
(a) Implicit super-interface of an annotation type:
    AdminRole implements java.lang.annotation.Annotation
    AdminRole.class.isAnnotation() = true

(b) Finding @Role directly OR through a composed annotation:
    ReportPage -> role=USER, level=1
    SettingsPage -> role=ADMIN, level=10
    PublicPage -> no role required
```

---

## Q28. Write a program to demonstrate how custom annotations can be used with reflection.

**Folder:** `Q28_ReflectionDemo/` – a **mini unit-testing framework** (how JUnit works inside), built from three custom annotations and reflection only.

| File | Role |
|---|---|
| `MyTest.java` | `@MyTest(name, expected)` – marks a test; `expected` = exception it should throw |
| `BeforeEachTest.java` | Marker – run before every test |
| `Skip.java` | `@Skip("reason")` – skip a test |
| `ShoppingCart.java` | The class under test (contains a **deliberate bug** in the discount) |
| `ShoppingCartTests.java` | Tests written with our annotations |
| `MiniTestRunner.java` | `main` – finds and runs the tests with reflection |

**What the runner does with reflection:**
1. `getDeclaredMethods()` → collect `@BeforeEachTest` and `@MyTest` methods.
2. For each test: `@Skip` present? → report SKIP.
3. Otherwise create a **new instance** (`getDeclaredConstructor().newInstance()`), call the `@BeforeEachTest` methods, then `invoke()` the test.
4. Catch `InvocationTargetException` → compare the real cause with `@MyTest.expected()` → PASS / FAIL.

```java
@MyTest(name = "negative price is rejected", expected = IllegalArgumentException.class)
public void negativePrice() { cart.add(-1); }

@MyTest(name = "checkout talks to payment server")
@Skip("payment server not available in the lab")
public void checkout() { ... }
```

**Output:**
```
Running tests in ShoppingCartTests
------------------------------------------------------------
[SKIP] checkout talks to payment server     reason: payment server not available in the lab
[FAIL] 10% discount is applied              AssertionError: expected 90.0 but was 80.0
[PASS] empty cart has zero items
[PASS] negative price is rejected           (threw IllegalArgumentException as expected)
[PASS] total adds up prices
------------------------------------------------------------
Total: 5 | Passed: 3 | Failed: 1 | Skipped: 1
Methods without @MyTest that were ignored: [notATest]
```
The FAIL is intentional – it proves the framework catches a real bug (`totalWithDiscount()` multiplies by 0.8 instead of 0.9).

---

## Q29. How can you define a custom annotation to log method execution time?

**Two parts are needed:**
1. **The annotation** – `RUNTIME` retention, `METHOD` target.
2. **Something that intercepts the call** and measures it. Options:
   - **JDK dynamic proxy** (`java.lang.reflect.Proxy`) – plain Java, needs an interface. ← used in the demo
   - **Spring AOP** – an `@Aspect` with `@Around("@annotation(LogExecutionTime)")` (most common in real projects).
   - A Java agent / bytecode library (ByteBuddy, AspectJ) for classes without interfaces.

**Folder:** `Q29_LogExecutionTime/`

| File | Role |
|---|---|
| `LogExecutionTime.java` | The annotation: `value()` label, `warnAboveMs()` threshold |
| `ReportService.java` | Interface |
| `ReportServiceImpl.java` | Business code – **no timing code**, just annotations |
| `TimingProxy.java` | `InvocationHandler` that times annotated methods |
| `LogExecutionTimeDemo.java` | `main` |

```java
@Documented @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface LogExecutionTime {
    String value() default "";
    long warnAboveMs() default Long.MAX_VALUE;
}

public class ReportServiceImpl implements ReportService {
    @LogExecutionTime("Build report")
    public String buildReport(int rows) { ... }

    public int countUsers() { return 42; }             // not timed

    @LogExecutionTime(value = "Export to CSV", warnAboveMs = 200)
    public String slowExport() { ... }
}

// TimingProxy.invoke(...)
Method real = target.getClass().getMethod(m.getName(), m.getParameterTypes());
LogExecutionTime t = real.getAnnotation(LogExecutionTime.class);
if (t == null) return m.invoke(target, args);
long start = System.nanoTime();
try { return m.invoke(target, args); }
finally { long ms = (System.nanoTime() - start) / 1_000_000; System.out.println("[TIMER] ... " + ms + " ms"); }

// Usage
ReportService service = TimingProxy.wrap(new ReportServiceImpl(), ReportService.class);
```

**Output** (milliseconds vary slightly per run):
```
Calling buildReport(1000)...
[TIMER] Build report took 133 ms
  result: report with 1000 rows
Calling countUsers() (not annotated, so no timer line)...
  result: 42
Calling slowExport()...
[TIMER] Export to CSV took 300 ms
[TIMER] WARNING: Export to CSV is slower than the 200 ms limit!
  result: export.csv
```

**Spring AOP version** (for reference – needs `spring-boot-starter-aop`):
```java
@Aspect @Component
public class ExecutionTimeAspect {
    @Around("@annotation(logExecutionTime)")
    public Object time(ProceedingJoinPoint pjp, LogExecutionTime logExecutionTime) throws Throwable {
        long start = System.currentTimeMillis();
        try { return pjp.proceed(); }
        finally { System.out.println(pjp.getSignature() + " took " + (System.currentTimeMillis() - start) + " ms"); }
    }
}
```

---

## Q30. What happens if a custom annotation is not annotated with `@Retention`?

It gets the **default retention policy: `RetentionPolicy.CLASS`**.

That means:
- ✅ It compiles fine – **no error and no warning**.
- ✅ It is stored in the `.class` file (as `RuntimeInvisibleAnnotations`).
- ❌ The JVM does not load it, so **reflection cannot see it**: `getAnnotation()` returns `null`, `isAnnotationPresent()` returns `false`.
- ❌ Any framework or code that reads it at runtime **silently does nothing** – a classic bug.
- ✅ It still works for compile-time annotation processors and for bytecode tools.

**Fix:** add `@Retention(RetentionPolicy.RUNTIME)` if you need it at runtime (or `SOURCE` if only the compiler needs it).

**Folder:** `Q30_MissingRetention/` – `NoRetention.java` (no `@Retention`), `WithRuntimeRetention.java` (RUNTIME), `MissingRetentionDemo.java`.

**Output:**
```
@Retention declared on NoRetention?          false
@Retention declared on WithRuntimeRetention? RUNTIME

Class level:
   getAnnotation(NoRetention.class)          = null
   getAnnotation(WithRuntimeRetention.class) = @WithRuntimeRetention("hello")
Method level:
   isAnnotationPresent(NoRetention.class)          = false
   isAnnotationPresent(WithRuntimeRetention.class) = true

Simulating a framework that relies on @NoRetention:
   framework found NOTHING -> the feature silently does not work!
Fix: add @Retention(RetentionPolicy.RUNTIME) to the annotation.
```
You can also confirm with `javap -v 'MissingRetentionDemo$Sample.class'` – `NoRetention` appears under `RuntimeInvisibleAnnotations` (see also the Q8/Q11 demo in section 1).
