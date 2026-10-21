# Java Annotations and Its Types — Answers (Q1–Q20)

Theory questions are answered in this file. Questions that need code also have a **demo folder** with runnable Java files (listed in the table below). Every demo has its real output pasted under the answer.

| Question | Demo folder | How to run (from inside the folder) |
|---|---|---|
| Q4 | `Q04_SuppressWarnings/` | `bash run.sh` |
| Q8, Q11 | `Q08_Q11_RetentionPolicies/` | `bash run.sh` |
| Q9 | `Q09_CreateCustomAnnotation/` | `javac *.java && java CustomAnnotationDemo` |
| Q12 | `Q12_ReflectionProcessing/` | `javac *.java && java ReflectionProcessingDemo` |
| Q15 | `Q15_AnnotationInheritance/` | `javac *.java && java InheritanceDemo` |
| Q16 | `Q16_DefaultValues/` | `javac *.java && java DefaultValuesDemo` |
| Q17 | `Q17_StringLengthValidation/` | `javac *.java && java LengthValidationDemo` |
| Q18 | `Q18_RepeatableAnnotations/` | `javac *.java && java RepeatableDemo` |
| Q19 | `Q19_DocumentedMetaAnnotation/` | `bash run.sh` |
| Q20 | `Q20_CompileTimeProcessing/` | `bash run.sh` |

> Requires JDK 11 or newer (tested on JDK 21). On Windows, run the commands inside each `run.sh` manually or use Git Bash.

---

## Q1. What is the purpose of annotations in Java?

An **annotation** is a label (metadata) that you attach to code — classes, methods, fields, parameters, etc. It starts with `@`. An annotation does **not** change what the code does by itself; it gives *information* to someone who reads it:

| Who reads it | What they use it for | Example |
|---|---|---|
| **The compiler** | Find mistakes, turn warnings on/off | `@Override`, `@SuppressWarnings`, `@FunctionalInterface` |
| **Build tools / annotation processors** | Generate code or check rules while compiling | Lombok's `@Getter`, MapStruct `@Mapper` |
| **The running program (via reflection)** | Change behaviour at runtime | JUnit `@Test`, Spring `@Autowired`, JPA `@Entity` |
| **Documentation tools** | Show it in javadoc | anything marked `@Documented` |

**Main purposes in short:**
1. **Information for the compiler** – catch errors early (`@Override` fails if you didn't really override).
2. **Compile-time and deploy-time processing** – tools generate code, XML, config files.
3. **Runtime processing** – frameworks find your classes and methods and wire them together.
4. **Replace XML configuration** – config sits right next to the code it describes.

```java
@Override                     // tells the compiler: "I am overriding a parent method"
public String toString() { return "User"; }
```

---

## Q2. Name three built-in Java annotations and their uses.

| Annotation | Used on | What it does |
|---|---|---|
| `@Override` | methods | Tells the compiler the method overrides a superclass/interface method. If it doesn't (e.g. a typo in the name), you get a **compile error**. |
| `@Deprecated` | classes, methods, fields, … | Marks something as old/not recommended. Anyone who uses it gets a **warning**. Since Java 9 it has `since` and `forRemoval` elements. |
| `@SuppressWarnings` | classes, methods, fields, local variables, … | Tells the compiler to **hide** specific warnings (e.g. `"unchecked"`, `"deprecation"`) inside that scope. |

Other built-ins worth knowing: `@FunctionalInterface` (compile error if the interface has more than one abstract method) and `@SafeVarargs` (hides "unsafe generic varargs" warnings).

```java
@FunctionalInterface
interface Calculator { int apply(int a, int b); }   // exactly one abstract method -> OK

class Shape {
    @Deprecated(since = "2.0", forRemoval = true)
    void drawOld() { }

    @Override
    public String toString() { return "Shape"; }
}
```

---

## Q3. What is the difference between `@Override` and `@Deprecated` annotations?

| | `@Override` | `@Deprecated` |
|---|---|---|
| **Purpose** | Says "this method replaces a parent method" | Says "don't use this any more, it may be removed" |
| **Used on** | Methods only | Classes, methods, fields, constructors, packages, etc. |
| **Who is affected** | The person **writing** the method | The people **calling/using** the item |
| **If something is wrong** | **Error** – compiler refuses if no parent method matches | **Warning** – code still compiles, users are warned |
| **Retention** | `SOURCE` (gone after compiling) | `RUNTIME` (still visible to reflection and IDEs) |
| **Elements** | none (marker annotation) | `since` (String) and `forRemoval` (boolean), Java 9+ |

```java
class Animal {
    void makeSound() { }
    @Deprecated(since = "1.5")
    void oldSleep() { }          // callers will see a warning
}

class Dog extends Animal {
    @Override
    void makeSound() { }         // OK

    // @Override
    // void makeSoud() { }       // COMPILE ERROR: method does not override - typo caught!
}
```

---

## Q4. How does the `@SuppressWarnings` annotation work? Provide an example.

**How it works:**
- You give it one or more **warning names** as Strings: `@SuppressWarnings("unchecked")` or `@SuppressWarnings({"unchecked", "rawtypes"})`.
- The compiler stops printing those warnings **only inside** the annotated element (class, method, field, constructor, parameter or local variable).
- It has `SOURCE` retention, so it is used by the compiler only and does not exist in the `.class` file.
- Best practice: put it on the **smallest** scope possible (a single variable or method, not the whole class) and only when you are sure the code is safe.

**Common warning names:** `unchecked`, `rawtypes`, `deprecation`, `removal`, `unused`, `serial`, `fallthrough`, `all`.

**Demo:** `Q04_SuppressWarnings/` — `WithWarnings.java` (no suppression) vs `SuppressWarningsDemo.java` (same code with suppression). Both compiled with `-Xlint:all` (turns on every warning).

```java
@SuppressWarnings({"rawtypes", "unchecked"})
static List<String> legacyList() {
    List raw = new ArrayList();
    raw.add("apple");
    return raw;
}

@SuppressWarnings("deprecation")
static String callOldApi() {
    return OldApi.oldGreeting();      // deprecated method, warning hidden
}
```

**Output of `bash run.sh` (shortened):**
```
===== 1) Compiling WithWarnings.java (no @SuppressWarnings) =====
WithWarnings.java:12: warning: [rawtypes] found raw type: List
WithWarnings.java:12: warning: [rawtypes] found raw type: ArrayList
WithWarnings.java:13: warning: [unchecked] unchecked call to add(E) as a member of the raw type List
WithWarnings.java:14: warning: [unchecked] unchecked conversion
WithWarnings.java:18: warning: [deprecation] oldGreeting() in OldApi has been deprecated
5 warnings

===== 2) Compiling SuppressWarningsDemo.java (with @SuppressWarnings) =====
(no warnings)

===== 3) Running SuppressWarningsDemo =====
legacyList()  -> [apple, banana]
callOldApi()  -> Hello from the OLD method
sumOfLegacy() -> 6
Compiled with -Xlint:all and no warnings for this class.
```

---

## Q5. What are meta-annotations in Java?

A **meta-annotation** is an *annotation that is put on another annotation*. It describes **how the annotation itself behaves** — where it can be used, how long it is kept, whether it is inherited, and so on.

Think of it like the label on a label: "this sticker may only be used on boxes, and it must stay on until delivery."

```java
@Retention(RetentionPolicy.RUNTIME)   // meta-annotation: keep until runtime
@Target(ElementType.METHOD)           // meta-annotation: only on methods
public @interface Test { }            // <- the annotation being described
```

They live in the package `java.lang.annotation`.

---

## Q6. List the meta-annotations available in Java and describe their purposes.

| Meta-annotation | Purpose | Example |
|---|---|---|
| `@Retention` | **How long** the annotation is kept: `SOURCE`, `CLASS` (default) or `RUNTIME`. (See Q8.) | `@Retention(RetentionPolicy.RUNTIME)` |
| `@Target` | **Where** the annotation may be used: `TYPE`, `FIELD`, `METHOD`, `PARAMETER`, `CONSTRUCTOR`, `LOCAL_VARIABLE`, `ANNOTATION_TYPE`, `PACKAGE`, `TYPE_PARAMETER` (8), `TYPE_USE` (8), `MODULE` (9), `RECORD_COMPONENT` (16). (See Q10.) | `@Target({ElementType.FIELD, ElementType.METHOD})` |
| `@Documented` | The annotation is **shown in javadoc** of the elements that use it. (See Q19.) | `@Documented` |
| `@Inherited` | A **class-level** annotation is **inherited by subclasses**. (See Q15.) | `@Inherited` |
| `@Repeatable` (Java 8) | The annotation can be used **more than once** on the same element; you name a container annotation. (See Q18.) | `@Repeatable(Schedules.class)` |

Also in the same package, used *as* annotations on code: `@Native` (marks a constant field that native code may reference). `@FunctionalInterface`, `@SafeVarargs`, `@Override`, `@Deprecated`, `@SuppressWarnings` are built-in annotations but **not** meta-annotations — they go on normal code.

---

## Q7. How can annotations affect runtime behavior in Java?

An annotation on its own does nothing. It changes runtime behaviour **only when some code reads it with reflection and acts on it**. For that the annotation needs `@Retention(RetentionPolicy.RUNTIME)`.

**The pattern:**
1. Mark code with a RUNTIME annotation.
2. At runtime, a framework/your code scans classes with reflection (`getAnnotation`, `isAnnotationPresent`…).
3. Depending on what it finds, it calls methods, injects objects, validates values, wraps methods in proxies, etc.

**Real examples:**

| Annotation | Runtime effect |
|---|---|
| JUnit `@Test` | JUnit finds and runs every `@Test` method |
| Spring `@Autowired` | Spring injects an object into the field/constructor |
| Spring `@Transactional` | Spring wraps the method in a proxy that starts/commits a DB transaction |
| JPA `@Entity`, `@Column` | Hibernate maps the class to a table and fields to columns |
| Jakarta `@NotNull`, `@Size` | Bean Validation rejects invalid objects |
| Servlet `@WebServlet("/hello")` | The server routes the URL `/hello` to that class |

**Tiny example** (this exact idea is demonstrated in Q12 and Q28):
```java
for (Method m : obj.getClass().getDeclaredMethods()) {
    if (m.isAnnotationPresent(RunOnStartup.class)) {
        m.invoke(obj);          // behaviour depends on the annotation!
    }
}
```

---

## Q8. What is the retention policy in annotations? Explain the types of retention policies.

The **retention policy** decides **how long an annotation survives**: only in the source code, also in the compiled `.class` file, or all the way into the running program. It is set with `@Retention` using the enum `java.lang.annotation.RetentionPolicy`.

| Policy | In `.java` source | In `.class` file | Readable by reflection at runtime | Typical use |
|---|---|---|---|---|
| `SOURCE` | ✅ | ❌ (discarded by compiler) | ❌ | Compiler hints and annotation processors: `@Override`, `@SuppressWarnings`, Lombok |
| `CLASS` (**default**) | ✅ | ✅ (as "RuntimeInvisibleAnnotations") | ❌ | Bytecode tools (ProGuard, code analysers) that read `.class` files |
| `RUNTIME` | ✅ | ✅ (as "RuntimeVisibleAnnotations") | ✅ | Frameworks: `@Test`, `@Autowired`, `@Entity`, `@Deprecated` |

Think of it as three stops on a journey: **source → compiled class → running JVM**. `SOURCE` gets off at stop 1, `CLASS` at stop 2, `RUNTIME` stays until the end.

**Demo:** `Q08_Q11_RetentionPolicies/` — one class has all three kinds of annotation. Reflection only sees the RUNTIME one; `javap` shows the CLASS one is stored in the bytecode but marked invisible.

```java
@SourceLevel("only the compiler sees me")
@ClassLevel("I live in the .class file but not at runtime")
@RuntimeLevel("reflection can read me")
public class AnnotatedService { }
```

**Output of `bash run.sh`:**
```
===== 1) Reflection (runtime) view =====
Checking AnnotatedService with reflection:
  @SourceLevel  present at runtime? false
  @ClassLevel   present at runtime? false
  @RuntimeLevel present at runtime? true
  @RuntimeLevel value = "reflection can read me"
Total annotations visible to reflection: 1
  -> RuntimeLevel

===== 2) Bytecode view: javap -v AnnotatedService.class =====
RuntimeVisibleAnnotations:
  0: #14(#15=s#16)
    RuntimeLevel(
--
RuntimeInvisibleAnnotations:
  0: #18(#15=s#19)
    ClassLevel(
```
`SourceLevel` does not appear in the bytecode at all.

---

## Q9. How do you create a custom annotation in Java?

**Five steps:**

1. **Declare** it with `@interface` (not `interface`).
2. **Add `@Retention`** – usually `RUNTIME` if you want to read it with reflection.
3. **Add `@Target`** – where it may be used.
4. **Add elements** – they look like methods without a body; they can have `default` values. Allowed types: primitives, `String`, `Class`, enums, other annotations, and 1-D arrays of these.
5. **Use it** on code, then **process it** (reflection at runtime, or an annotation processor at compile time).

**Demo:** `Q09_CreateCustomAnnotation/`

```java
// Step 1-4: Review.java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Review {
    enum Status { PENDING, APPROVED, REJECTED }
    String reviewer();                        // required
    Status status() default Status.PENDING;   // optional
    String comment() default "";              // optional
}

// Step 5a: use it - ReportGenerator.java
@Review(reviewer = "Alice", status = Review.Status.APPROVED, comment = "Clean design")
public class ReportGenerator {
    @Review(reviewer = "Bob") public void generatePdf() { }
    @Review(reviewer = "Carol", status = Review.Status.REJECTED, comment = "Needs unit tests")
    public void generateCsv() { }
    public void helper() { }
}

// Step 5b: process it - CustomAnnotationDemo.java
Review r = m.getAnnotation(Review.class);
if (r != null && r.status() == Review.Status.REJECTED) { ... }
```

**Output:**
```
Class ReportGenerator -> reviewer=Alice, status=APPROVED, comment="Clean design"
Method generateCsv() -> reviewer=Carol, status=REJECTED, comment="Needs unit tests"
    WARNING: generateCsv was rejected!
Method generatePdf() -> reviewer=Bob, status=PENDING, comment=""
Method helper() -> not reviewed
```

---

## Q10. What is the use of the `@Target` meta-annotation?

`@Target` **restricts where an annotation can be placed**. If someone puts it in a place that is not listed, the **compiler gives an error**. Without `@Target`, an annotation can be used on almost any declaration (all declaration contexts; type-use is excluded).

**`ElementType` values:**

| Value | Allowed on |
|---|---|
| `TYPE` | class, interface, enum, record, annotation |
| `FIELD` | fields (incl. enum constants) |
| `METHOD` | methods |
| `PARAMETER` | method/constructor parameters |
| `CONSTRUCTOR` | constructors |
| `LOCAL_VARIABLE` | local variables |
| `ANNOTATION_TYPE` | other annotations (this is how you make your own meta-annotations) |
| `PACKAGE` | `package-info.java` |
| `TYPE_PARAMETER` (Java 8) | generic type parameters `<@X T>` |
| `TYPE_USE` (Java 8) | any use of a type: `@NonNull String s`, `new @X Foo()`, casts |
| `MODULE` (Java 9) | `module-info.java` |
| `RECORD_COMPONENT` (Java 16) | record components |

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty { }

class Form {
    @NotEmpty String name;            // OK - FIELD
    void save(@NotEmpty String s) { } // OK - PARAMETER

    // @NotEmpty                      // COMPILE ERROR:
    // void reset() { }               // "annotation interface not applicable to this kind of declaration"
}
```

---

## Q11. Explain the difference between `@Retention` with `RUNTIME` and `CLASS`.

| | `RetentionPolicy.CLASS` | `RetentionPolicy.RUNTIME` |
|---|---|---|
| Stored in `.class` file? | Yes – attribute `RuntimeInvisibleAnnotations` | Yes – attribute `RuntimeVisibleAnnotations` |
| Loaded into the JVM? | **No** | **Yes** |
| `getAnnotation()` result | always `null` | the annotation object |
| Is it the default? | **Yes** (used when `@Retention` is missing) | No – must be written explicitly |
| Who uses it | Bytecode tools that read `.class` files directly (obfuscators, static analysers, instrumentation agents) | Frameworks and your own code using reflection (Spring, JUnit, Hibernate) |
| Cost | Slightly smaller memory use at runtime | Annotation objects are created in memory |

**Rule of thumb:** if you plan to call `getAnnotation(...)`, you **must** use `RUNTIME`. Forgetting this is the #1 bug with custom annotations (see Q30).

**Proof:** the demo for Q8 (`Q08_Q11_RetentionPolicies/`) shows `@ClassLevel` is in the bytecode (`RuntimeInvisibleAnnotations`) but `isAnnotationPresent(ClassLevel.class)` returns `false`, while `@RuntimeLevel` returns `true`.

---

## Q12. How can you use reflection to process annotations in Java?

The `java.lang.reflect` classes `Class`, `Method`, `Field`, `Constructor` and `Parameter` all implement **`AnnotatedElement`**, which gives these methods:

| Method | Returns |
|---|---|
| `isAnnotationPresent(X.class)` | `true` / `false` |
| `getAnnotation(X.class)` | the annotation (or `null`) – includes `@Inherited` ones on classes |
| `getDeclaredAnnotation(X.class)` | only if written directly on this element |
| `getAnnotations()` / `getDeclaredAnnotations()` | all annotations as an array |
| `getAnnotationsByType(X.class)` | array – works with repeatable annotations (Java 8) |
| `Method.getParameterAnnotations()` | `Annotation[][]` – one array per parameter |

**Steps:** get the `Class` object → loop over its fields/methods → check for the annotation → read its values → do something (call the method, read the field, validate…). Remember the annotation must have `RUNTIME` retention, and use `setAccessible(true)` to read private fields.

**Demo:** `Q12_ReflectionProcessing/` – uses annotations on all four levels:
- `@Table` on the class + `@Column` on fields → builds an SQL `INSERT` statement.
- `@RunOnStartup(order=…)` on methods → invokes them in order.
- `@NotNull` on a parameter → blocks a call with a `null` argument.

```java
Table t = clazz.getAnnotation(Table.class);
for (Field f : clazz.getDeclaredFields()) {
    Column col = f.getAnnotation(Column.class);
    if (col == null) continue;
    f.setAccessible(true);
    Object value = f.get(customer);
    ...
}
```

**Output:**
```
1) CLASS annotations
   Customer maps to table 'customers'
2) FIELD annotations -> generate SQL
   skipping field 'temporaryNote' (no @Column)
   INSERT INTO customers (customer_id(PK), full_name) VALUES (7, 'Shrey');
3) METHOD annotations -> invoke @RunOnStartup methods by order
   invoking connect (order 1)
    connect() ran
   invoking loadCache (order 2)
    loadCache() ran
4) PARAMETER annotations
   rename() parameter #0 (String) @NotNull? true
   rename() parameter #1 (String) @NotNull? false
   trying rename(null, "typo") with a @NotNull check...
   BLOCKED: parameter #0 of rename() is @NotNull but null was passed
5) getDeclaredAnnotations() on the class:
   @Table(name="customers")
```

---

## Q13. What is the difference between marker annotations and single-value annotations?

| Type | Number of elements | How you write it | Examples |
|---|---|---|---|
| **Marker** | **0** – its presence alone is the information | `@Name` (no brackets needed) | `@Override`, `@FunctionalInterface`, `@Inherited`, `@Documented` |
| **Single-value** | **1** element, normally named `value` | `@Name(x)` – you can skip `value =` | `@SuppressWarnings("unchecked")`, `@Retention(RUNTIME)`, `@Target(METHOD)` |
| *Full / multi-value* (for comparison) | 2 or more | `@Name(a = 1, b = "x")` | `@Deprecated(since="9", forRemoval=true)` |

```java
// Marker annotation
@Retention(RetentionPolicy.RUNTIME)
@interface Audited { }

// Single-value annotation - element MUST be called "value" for the shortcut
@Retention(RetentionPolicy.RUNTIME)
@interface Priority { int value(); }

class Job {
    @Audited                // marker: just "yes, this is audited"
    @Priority(5)            // shortcut for @Priority(value = 5)
    void run() { }
}

// Reading them:
boolean audited = m.isAnnotationPresent(Audited.class);   // marker -> check presence
int p = m.getAnnotation(Priority.class).value();          // single-value -> read value
```

Note: an annotation with several elements can still use the shortcut if all other elements have defaults and you only set `value`.

---

## Q14. How do annotations integrate with frameworks like Spring and Hibernate?

Frameworks use annotations as **configuration written directly on your classes**, instead of long XML files. At startup the framework **scans** your packages, reads the RUNTIME annotations with reflection, and builds everything for you.

**Spring (dependency injection, web, transactions):**
| Annotation | What Spring does with it |
|---|---|
| `@Component`, `@Service`, `@Repository`, `@Controller` | Creates an object ("bean") of this class and manages it |
| `@Autowired` / constructor injection | Injects the needed bean |
| `@Configuration` + `@Bean` | Methods that create beans |
| `@RestController`, `@GetMapping("/users")` | Maps HTTP requests to methods |
| `@Transactional` | Wraps the method in a **proxy** that opens/commits/rolls back a transaction |
| `@Value("${app.name}")` | Injects a value from `application.properties` |
| `@SpringBootApplication` | One annotation that combines `@Configuration`, `@EnableAutoConfiguration` and `@ComponentScan` (a **composed** annotation) |

**Hibernate / JPA (database mapping - ORM):**
| Annotation | Meaning |
|---|---|
| `@Entity` | This class is stored in the database |
| `@Table(name = "users")` | Table name |
| `@Id`, `@GeneratedValue` | Primary key and how it is generated |
| `@Column(name = "email", nullable = false, unique = true)` | Column mapping |
| `@OneToMany`, `@ManyToOne`, `@JoinColumn` | Relationships between tables |
| `@Transient` | Do not save this field |

**Example – the two working together:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
}

@Service
public class UserService {
    private final UserRepository repo;

    @Autowired                         // Spring injects the repository
    public UserService(UserRepository repo) { this.repo = repo; }

    @Transactional                     // Spring opens/commits a DB transaction
    public User register(String email) {
        User u = new User();
        u.setEmail(email);
        return repo.save(u);           // Hibernate turns this into INSERT INTO users ...
    }
}
```

**How it works underneath:** classpath scanning → reflection (`getAnnotation`) → creating objects / dynamic proxies (JDK `Proxy` or CGLIB/ByteBuddy) → calling your code. Some tools (Lombok, MapStruct, Hibernate's metamodel generator) use **compile-time** annotation processors instead (see Q20). The Q29 demo shows a mini version of the proxy technique.

---

## Q15. Can annotations be inherited? If yes, how?

**Yes, but only in one specific case:** the annotation must be marked with the meta-annotation **`@Inherited`**, and it must be on a **class** (not an interface, not a method).

**Rules:**
1. `@Inherited` + on a superclass → subclasses "have" it when you call `getAnnotation()`.
2. Without `@Inherited` → not inherited.
3. `getDeclaredAnnotation()` ignores inherited annotations (only what is written on that class).
4. If the subclass declares the same annotation itself, its own value wins.
5. Annotations on **interfaces** are **not** inherited by implementing classes.
6. Annotations on **methods** are **not** inherited by overriding methods (even with `@Inherited`). Frameworks like Spring work around this with their own helpers (`AnnotationUtils.findAnnotation`).

**Demo:** `Q15_AnnotationInheritance/`
```java
@Inherited @Retention(RUNTIME) @Target({TYPE, METHOD})
public @interface InheritedTag { String value(); }

@Retention(RUNTIME) @Target(TYPE)
public @interface PlainTag { String value(); }

@InheritedTag("from Animal") @PlainTag("from Animal")
class Animal { @InheritedTag("on Animal.speak()") public void speak() { } }
class Dog extends Animal { @Override public void speak() { } }
@InheritedTag("from Puppy") class Puppy extends Dog { }
@InheritedTag("from interface Pet") interface Pet { }
class Cat implements Pet { }
```

**Output:**
```
Animal -> @InheritedTag: "from Animal"  | @PlainTag: "from Animal"
Dog    -> @InheritedTag: "from Animal"  | @PlainTag: none
Puppy  -> @InheritedTag: "from Puppy"   | @PlainTag: none
Cat    -> @InheritedTag: none           | @PlainTag: none

getDeclaredAnnotation on Dog (only what is written on Dog itself): null

Method annotations:
  Animal.speak() @InheritedTag = "on Animal.speak()"
  Dog.speak()    @InheritedTag = none   <- overriding methods never inherit annotations
```

---

## Q16. What is the use of default values in annotations? Provide an example.

A **default value** (`default ...`) makes an element **optional**. If the user doesn't write it, the default is used. This:
- keeps annotations short (`@ServiceConfig(name = "Email")` instead of listing six values),
- gives sensible standard behaviour,
- lets you add new elements later without breaking existing code.

**Rules:** the default must be a **compile-time constant**; it can **never be `null`** (use `""`, `{}`, `Void.class`, or a special enum value instead). Elements without a default are **required**. You can read the default with `Method.getDefaultValue()`.

**Demo:** `Q16_DefaultValues/`
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceConfig {
    enum Env { DEV, TEST, PROD }
    String name();                          // REQUIRED
    int timeoutSeconds() default 30;
    boolean cacheEnabled() default true;
    Env environment() default Env.DEV;
    String[] tags() default {};             // empty array, not null
    Class<?> fallback() default Void.class; // "no class" marker, not null
}

@ServiceConfig(name = "EmailService")                    // all defaults
class EmailService { }

@ServiceConfig(name = "PaymentService", timeoutSeconds = 5,
               environment = ServiceConfig.Env.PROD, tags = {"critical", "finance"})
class PaymentService { }
```

**Output (shortened):**
```
EmailService
   timeoutSeconds  = 30                   (default)
   cacheEnabled    = true                 (default)
   environment     = DEV                  (default)
   tags            = []                   (default)
   fallback        = Void                 (default)

PaymentService
   timeoutSeconds  = 5
   cacheEnabled    = true                 (default)
   environment     = PROD
   tags            = [critical, finance]
   fallback        = Void                 (default)
```

---

## Q17. Write a custom annotation to validate the length of a string.

**Demo:** `Q17_StringLengthValidation/` – 4 files:

| File | Role |
|---|---|
| `Length.java` | The annotation (`min`, `max`, `message`) |
| `LengthValidator.java` | Reads `@Length` with reflection and checks each field |
| `UserAccount.java` | Model class using `@Length` |
| `LengthValidationDemo.java` | Main – validates a good and a bad object |

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "length must be between {min} and {max}";
}

public class UserAccount {
    @Length(min = 3, max = 15)                     private String username;
    @Length(min = 8, message = "password must have at least {min} characters")
                                                   private String password;
    @Length(max = 50)                              private String bio;
}

// LengthValidator (core loop)
for (Field field : obj.getClass().getDeclaredFields()) {
    Length rule = field.getAnnotation(Length.class);
    if (rule == null) continue;
    field.setAccessible(true);
    String value = (String) field.get(obj);
    int len = (value == null) ? 0 : value.length();
    if (len < rule.min() || len > rule.max()) errors.add(...);
}
```
The validator also reports misuse (e.g. `@Length` on a non-String field, or `min > max`).

**Output:**
```
Validating UserAccount{username='shrey', bio='Software engineering...'}
   VALID

Validating UserAccount{username='ab', bio='xxxxxxxxxxxxxxxxxxxx...'}
   INVALID - 3 problem(s):
   - username: length must be between 3 and 15 (was 2: "ab")
   - password: password must have at least 8 characters (was 3: "123")
   - bio: length must be between 0 and 50 (was 60: "xxxxxxxxxxxxxxxxxxxx...")
```

---

## Q18. What are repeatable annotations in Java? Provide an example.

Before Java 8 you could use an annotation **only once** per element. A **repeatable annotation** (Java 8+) can be written **several times** on the same element.

**How to make one:**
1. Put `@Repeatable(Container.class)` on the annotation.
2. Create the **container annotation** with an element `value()` that returns an array of the repeatable annotation.
3. Retention and target of the container must be compatible (at least as long-lived, and covering the same places).

**Important behaviour:** when you repeat it, the compiler secretly wraps all copies inside the container. So `getAnnotation(Schedule.class)` returns **`null`** for a repeated one — use **`getAnnotationsByType(Schedule.class)`**, which works whether it was used once or many times.

**Demo:** `Q18_RepeatableAnnotations/`
```java
@Repeatable(Schedules.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedule { String day(); int hour() default 9; }

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedules { Schedule[] value(); }

public class BackupJobs {
    @Schedule(day = "Monday")
    @Schedule(day = "Wednesday", hour = 14)
    @Schedule(day = "Friday", hour = 18)
    public void fullBackup() { }
}
```

**Output (shortened):**
```
fullBackup()
   getAnnotation(Schedule.class)  = null
   getAnnotation(Schedules.class) = container with 3 item(s)
   getAnnotationsByType -> 3 schedule(s):
      runs every Monday    at 09:00
      runs every Wednesday at 14:00
      runs every Friday    at 18:00

cleanTempFiles()
   getAnnotation(Schedule.class)  = @Schedule(hour=2, day="Sunday")
   getAnnotation(Schedules.class) = null
   getAnnotationsByType -> 1 schedule(s):
      runs every Sunday    at 02:00
```
Real-world examples: `@PropertySource` in Spring, `@ComponentScan`, JUnit 5 `@Tag`.

---

## Q19. How does the `@Documented` meta-annotation work?

By default, **javadoc does not show annotations** in the documentation of classes and methods that use them. If an annotation type is marked **`@Documented`**, then every use of it **appears in the generated javadoc** (in the class/method signature), because it is treated as part of the public API.

- It affects **only documentation** – nothing changes at compile time or runtime.
- Use it for annotations that users of your API need to know about (e.g. `@Deprecated`, `@FunctionalInterface`, Spring's `@Service` are all `@Documented`).
- Don't use it for internal/tooling annotations.

**Demo:** `Q19_DocumentedMetaAnnotation/` – `@ApiInfo` is `@Documented`, `@InternalNote` is not. `PaymentGateway` uses both. `run.sh` runs `javadoc` and counts each annotation in the generated HTML.

```java
@Documented @Retention(RUNTIME) @Target({TYPE, METHOD})
public @interface ApiInfo { String since(); String owner(); }

@Retention(RUNTIME) @Target({TYPE, METHOD})
public @interface InternalNote { String value(); }

@ApiInfo(since = "2.0", owner = "Payments Team")
@InternalNote("TODO: refactor the retry logic")
public class PaymentGateway { ... }
```

**Output of `bash run.sh`:**
```
===== 1) Runtime view =====
Is ApiInfo marked @Documented?      true
Is InternalNote marked @Documented? false

At runtime, PaymentGateway still has BOTH annotations:
   @ApiInfo(since="2.0", owner="Payments Team")
   @InternalNote("TODO: refactor the retry logic")

=> @Documented changes only what appears in the generated javadoc HTML.

===== 2) Javadoc view =====
Occurrences of '@ApiInfo'      in docs/PaymentGateway.html: 6  (shown - it is @Documented)
Occurrences of '@InternalNote' in docs/PaymentGateway.html: 0  (hidden - it is not @Documented)
```

---

## Q20. How are annotations processed during the compilation phase?

Java has a **Pluggable Annotation Processing API** (JSR 269, package `javax.annotation.processing`). An **annotation processor** is a class that **the compiler (`javac`) runs while compiling**. It can:
1. **Report** notes, warnings and **errors** (an error stops the build),
2. **Validate** your code against rules,
3. **Generate new source files** (or resources) — which are then compiled too.

It **cannot modify** existing classes through the standard API (Lombok does this using internal compiler hacks).

**How it works – step by step:**
1. Write a class that extends `AbstractProcessor` and overrides `process(...)`.
2. Declare which annotations it handles with `@SupportedAnnotationTypes`.
3. Register it: either `javac -processor MyProcessor ...`, or a file `META-INF/services/javax.annotation.processing.Processor` inside the processor JAR (auto-discovered; with Maven use `<annotationProcessorPaths>` in the compiler plugin).
4. `javac` parses the sources and runs processing in **rounds**: round 1 gives the processor all annotated elements; if the processor generates new files, another round runs over those; this repeats until no new files appear. Then normal compilation finishes.
5. Inside `process()` you get `Element` objects (the compiler's view of classes, methods, fields — not `Class` objects, because nothing is compiled yet). `Messager` prints messages, `Filer` creates files.

The annotations only need `SOURCE` retention because processing happens before the `.class` files exist.

**Real uses:** Lombok (`@Getter`), MapStruct, Dagger, Hibernate JPA metamodel, AutoValue, Micronaut, and javac's own `@Override` / `@FunctionalInterface` checks.

**Demo:** `Q20_CompileTimeProcessing/`
```
processor/  Immutable.java, Todo.java, CompileTimeProcessor.java   <- compiled first
app/        Point.java (@Immutable), OrderService.java (@Todo), AppMain.java
broken/     MutablePoint.java (@Immutable but has a non-final field)
```
`AppMain` uses a class `GeneratedTodoList` that **does not exist in the source** — the processor writes it during compilation.

```java
@SupportedAnnotationTypes({"Immutable", "Todo"})
public class CompileTimeProcessor extends AbstractProcessor {
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element type : roundEnv.getElementsAnnotatedWith(Immutable.class)) {
            for (Element member : type.getEnclosedElements())
                if (member.getKind() == ElementKind.FIELD && !member.getModifiers().contains(Modifier.FINAL))
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "... non-final field ...", member);
        }
        ...  // NOTE for each @Todo, then Filer.createSourceFile("GeneratedTodoList")
    }
}
```

**Output of `bash run.sh`:**
```
===== STEP 2: compile the app WITH the processor (watch the Notes) =====
Note: [processor] round 1 - annotations to process: [Immutable, Todo]
Note: [processor] @Immutable check passed for Point
Note: [processor] TODO in OrderService: split this class into OrderReader and OrderWriter
Note: [processor] TODO in OrderService.placeOrder(): add input validation
Note: [processor] TODO in OrderService.cancelOrder(): send a confirmation email
Note: [processor] generated GeneratedTodoList.java with 3 item(s)
Note: [processor] round 2 - annotations to process: []
Note: [processor] round 3 - annotations to process: []

===== Generated source file: out/generated/GeneratedTodoList.java =====
// AUTO-GENERATED by CompileTimeProcessor - do not edit
public final class GeneratedTodoList {
    private GeneratedTodoList() { }
    public static final String[] ITEMS = {
        "OrderService -> split this class into OrderReader and OrderWriter",
        "OrderService.placeOrder() -> add input validation",
        "OrderService.cancelOrder() -> send a confirmation email",
    };
}

===== STEP 3: run the app =====
Order placed for laptop
Point(3, 4)
TODO list generated at compile time:
  - OrderService -> split this class into OrderReader and OrderWriter
  - OrderService.placeOrder() -> add input validation
  - OrderService.cancelOrder() -> send a confirmation email

===== STEP 4: compile a class that breaks the @Immutable rule =====
broken/MutablePoint.java:8: error: @Immutable class 'MutablePoint' has a non-final field 'y'
    private int y;          // <-- not final: the processor reports an ERROR here
                ^
1 error
>>> Compilation FAILED because of the processor's error (this is expected).
```
Round 2 happens because a new file was generated; round 3 is the final "processing over" round.
