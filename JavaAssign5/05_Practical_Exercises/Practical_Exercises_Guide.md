# Practical Exercise Suggestions — Solutions

All five exercises are complete Maven projects. Open a terminal **inside the exercise folder** and use the commands shown.

| # | Exercise (from the assignment) | Folder |
|---|---|---|
| 1 | Create a Maven project that uses annotations to validate user input | `Exercise1_AnnotationInputValidation/` |
| 2 | Build a Maven project that demonstrates the use of transitive dependencies | `Exercise2_TransitiveDependencies/` |
| 3 | Develop a simple core Java application that uses custom annotations and Maven to manage dependencies | `Exercise3_CustomAnnotationsCoreApp/` |
| 4 | Configure a Maven project to build a web application using maven-war-plugin | `Exercise4_WarWebApplication/` |
| 5 | Implement a Maven multi-module project with shared dependencies in the parent pom.xml | `Exercise5_MultiModuleProject/` |

> Requires **JDK 17+** and **Maven 3.6.3+**. The first build downloads libraries from Maven Central (internet needed).

---

## Exercise 1 — Annotation-based user input validation

**Idea:** a console sign-up form where **every rule – and even every question – is an annotation**. The form is generated from the model class with reflection, each answer is validated immediately, and wrong answers are asked again.

```
Exercise1_AnnotationInputValidation/
├── pom.xml
└── src/
    ├── main/java/com/javaassign5/ex1/
    │   ├── UserInputApp.java              main (interactive or demo mode)
    │   ├── annotations/  NotBlank, Length, Range, Pattern, Email, Prompt
    │   ├── validation/   Validator.java (reflection), Violation.java
    │   ├── form/         FormFiller.java (builds the form from @Prompt)
    │   └── model/        UserRegistration.java (the annotated form)
    └── test/java/com/javaassign5/ex1/validation/ValidatorTest.java   (4 tests)
```

**The model – no validation code at all:**
```java
public class UserRegistration {
    @Prompt(value = "Username (3-15 letters, digits or _)", order = 1)
    @NotBlank
    @Length(min = 3, max = 15)
    @Pattern(regex = "[A-Za-z0-9_]+", message = "may only contain letters, digits and _")
    private String username;

    @Prompt(value = "E-mail address", order = 2)
    @NotBlank @Email
    private String email;

    @Prompt(value = "Age", order = 3)
    @Range(min = 13, max = 120, message = "must be between {min} and {max} years")
    private int age;
    ...
}
```

**Commands:**
| Command | What it does |
|---|---|
| `mvn -q compile exec:java` | Interactive – type your own answers |
| `mvn -q compile exec:java -Dexec.args=demo` | Demo – answers are typed automatically (several wrong on purpose) |
| `mvn test` | Runs `ValidatorTest` (4 tests) |
| `mvn package` → `java -jar target/ex1-annotation-input-validation-1.0.0.jar` | Runnable jar |

**Output of demo mode:**
```
=== User Registration (rules come from annotations) ===
(demo mode - answers are typed automatically)
Username (3-15 letters, digits or _): ab
   x username must be between 3 and 15 characters (got 2)
Username (3-15 letters, digits or _): shrey!
   x username may only contain letters, digits and _
Username (3-15 letters, digits or _): shrey_m
E-mail address: shrey.example.com
   x email must be a valid e-mail address
E-mail address: shrey@example.com
Age: eighteen
   x age must be a whole number
Age: 8
   x age must be between 13 and 120 years (got 8)
Age: 19
Password (8+ characters, at least one digit): password
   x password must contain at least one digit
Password (8+ characters, at least one digit): s3cure
   x password must be between 8 and 64 characters (got 6)
Password (8+ characters, at least one digit): s3curePass
Phone number (optional - press Enter to skip): 12ab
   x phone must be 8-15 digits, may start with +
Phone number (optional - press Enter to skip): +61 412 345 678

Registration successful!
UserRegistration{username='shrey_m', email='shrey@example.com', age=19, password='**********', phone='+61 412 345 678'}
```

**Concepts used:** custom annotations with defaults and `{min}`/`{max}` message templates (Q16, Q17, Q21), `RUNTIME` retention (Q8), reflection on fields (Q12), Maven `test` scope and exec plugin (Q34, Q48, Q50).

---

## Exercise 2 — Transitive dependencies

**Idea:** declare only **two** dependencies and show that Maven brings in **many more** – and that the code can even use a library it never declared.

```
pom.xml declares:          commons-text 1.12.0 (compile)   +   junit-jupiter 5.10.2 (test)
Maven actually resolves:   commons-lang3 3.14.0  <- transitive via commons-text
                           junit-jupiter-api / -params / -engine, opentest4j,
                           junit-platform-commons, junit-platform-engine, apiguardian-api
                                                 <- transitive via junit-jupiter
```

```
Exercise2_TransitiveDependencies/
├── pom.xml                                    (+ profile "no-lang3" that cuts the link)
└── src/
    ├── main/java/com/javaassign5/ex2/
    │   ├── TransitiveDemo.java                main
    │   └── LangFeatures.java                  uses commons-lang3 (NOT declared!)
    └── test/java/com/javaassign5/ex2/LangFeaturesTest.java
```

**Commands and expected results:**

`mvn dependency:tree`
```
[INFO] com.javaassign5:ex2-transitive-dependencies:jar:1.0.0
[INFO] +- org.apache.commons:commons-text:jar:1.12.0:compile
[INFO] |  \- org.apache.commons:commons-lang3:jar:3.14.0:compile
[INFO] \- org.junit.jupiter:junit-jupiter:jar:5.10.2:test
[INFO]    +- org.junit.jupiter:junit-jupiter-api:jar:5.10.2:test
[INFO]    |  +- org.opentest4j:opentest4j:jar:1.3.0:test
[INFO]    |  +- org.junit.platform:junit-platform-commons:jar:1.10.2:test
[INFO]    |  \- org.apiguardian:apiguardian-api:jar:1.1.2:test
[INFO]    +- org.junit.jupiter:junit-jupiter-params:jar:5.10.2:test
[INFO]    \- org.junit.jupiter:junit-jupiter-engine:jar:5.10.2:test
[INFO]       \- org.junit.platform:junit-platform-engine:jar:1.10.2:test
```

`mvn -q compile exec:exec`
```
1) DIRECT dependency  (commons-text, declared in pom.xml)
   WordUtils.capitalizeFully : Maven Downloads The Dependencies Of Your Dependencies
   CaseUtils.toCamelCase     : transitiveDependencyDemo
   StringSubstitutor         : Maven added commons-lang3 for us
   loaded from               : commons-text-1.12.0.jar

2) TRANSITIVE dependency (commons-lang3, NOT declared in pom.xml)
   StringUtils.abbreviate    : maven downloads the depende...
   ArrayUtils.reverse        : three two one
   StringUtils.isNumeric     : true
   loaded from               : commons-lang3-3.14.0.jar

3) Runtime classpath that Maven built for us:
   classes                      our compiled classes
   commons-text-1.12.0.jar      DECLARED
   commons-lang3-3.14.0.jar     TRANSITIVE
```
(The JUnit jars are not on the runtime classpath – test-scope dependencies are only used for `mvn test`.)

`mvn dependency:analyze`
```
[WARNING] Used undeclared dependencies found:
[WARNING]    org.apache.commons:commons-lang3:jar:3.14.0:compile
```
→ Maven itself points out that relying on a transitive dependency directly is fragile; best practice is to declare it.

**Breaking the link (experiment):**
| Command | Result |
|---|---|
| `mvn -q compile` then `mvn -q exec:exec -Pno-lang3` | Part 2 prints `FAILED: java.lang.NoClassDefFoundError: org/apache/commons/lang3/StringUtils` – the exclusion removed the transitive jar at runtime |
| `mvn -q compile -Pno-lang3` | Compile error: `package org.apache.commons.lang3 does not exist` |

**Concepts used:** transitive dependencies (Q42), scopes and transitivity (Q48), exclusions (Q43), `dependency:tree`/`analyze` goals (Q39), profiles.

---

## Exercise 3 — Core Java app with custom annotations + Maven dependencies ("Task Tracker")

**Idea:** a command-line to-do app where custom annotations drive **three** separate features, and Maven manages **Gson** and **Commons Lang3**:

| Annotation | Feature | Processed by |
|---|---|---|
| `@CliCommand(name, usage, description, minArgs)` | Turns methods into commands; **help screen is generated** from it | `CommandDispatcher` (reflection) |
| `@Required`, `@MaxLength` | Validates a new task | `EntityValidator` (reflection) |
| `@JsonName("task_id")` | Sets JSON keys when exporting | **Gson** via a custom `FieldNamingStrategy` – our annotation plugged into a Maven-managed library |

```
Exercise3_CustomAnnotationsCoreApp/
├── pom.xml          gson, commons-lang3, junit-jupiter (test), exec + shade plugins
└── src/
    ├── main/java/com/javaassign5/ex3/
    │   ├── TaskTrackerApp.java        main (demo script or -i interactive)
    │   ├── TaskCommands.java          the @CliCommand methods
    │   ├── annotations/  CliCommand, Required, MaxLength, JsonName
    │   ├── core/         CommandDispatcher, EntityValidator, JsonExporter
    │   └── model/        Task.java
    └── test/java/com/javaassign5/ex3/TaskTrackerTest.java   (6 tests)
```

```java
@CliCommand(name = "add", usage = "add <priority> <title...>", minArgs = 2,
            description = "add a task (priority = low | medium | high)")
public String add(String[] args) { ... }

public class Task {
    @JsonName("task_id") private final int id;
    @Required(message = "title is required") @MaxLength(40) @JsonName("task_title")
    private final String title;
    ...
}

// JsonExporter - connects our annotation to Gson
FieldNamingStrategy strategy = field -> {
    JsonName n = field.getAnnotation(JsonName.class);
    return n != null ? n.value() : field.getName();
};
```

**Commands:**
| Command | What it does |
|---|---|
| `mvn -q compile exec:java` | Runs a scripted demo session |
| `mvn -q compile exec:java -Dexec.args="-i"` | Interactive – type `add high Buy milk`, `list`, `done 1`, `export`, `exit` |
| `mvn test` | 6 unit tests |
| `mvn package` → `java -jar target/ex3-task-tracker-1.0.0-all.jar -i` | Fat jar with Gson + Lang3 inside |

**Output of the demo session:**
```
=== Task Tracker (commands discovered via @CliCommand) ===
> help
Commands:
  add <priority> <title...>   add a task (priority = low | medium | high)
  done <id>                   mark a task as finished
  export                      print all tasks as JSON (keys from @JsonName)
  list                        show all tasks
  remove <id>                 delete a task
  stats                       count tasks by status
  help                        show this list
  exit                        quit (interactive mode)

> add high "Finish Java assignment 5"
Added task #1: Finish Java assignment 5

> add medium Revise annotations for the quiz
Added task #2: Revise annotations for the quiz

> add low Clean desk
Added task #3: Clean desk

> add urgent Something
Error: priority must be low, medium or high

> add high This title is far too long to be accepted by MaxLength
Rejected: title must be at most 40 characters (got 54)

> done 1
Task #1 marked as done

> done 99
Error: no task with id 99

> list
ID  PRIORITY  DONE  TITLE
1   HIGH      [x]   Finish Java assignment 5
2   MEDIUM    [ ]   Revise annotations for the ...
3   LOW       [ ]   Clean desk

> stats
3 task(s): 1 done, 2 open, 0 open HIGH priority

> remove 3
Removed task #3

> export
[
  {
    "task_id": 1,
    "task_title": "Finish Java assignment 5",
    "priority_level": "HIGH",
    "is_done": true
  },
  {
    "task_id": 2,
    "task_title": "Revise annotations for the quiz",
    "priority_level": "MEDIUM",
    "is_done": false
  }
]

> fly
Unknown command 'fly'. Type 'help'.
```

**Concepts used:** custom annotations on methods and fields (Q9, Q22–Q26), reflection-driven dispatch (Q28), annotation + library integration (Q14, Q26), dependency management and fat jars (Q34, Q45).

---

## Exercise 4 — Web application built with maven-war-plugin ("Student Portal")

**Idea:** a small Jakarta Servlet web app (home page, greeting page, student list with a form) that Maven packages into **`student-portal.war`**, ready for Tomcat/Jetty.

```
Exercise4_WarWebApplication/
├── pom.xml                       packaging=war, maven-war-plugin, jetty plugin
└── src/
    ├── main/java/com/javaassign5/ex4/
    │   ├── HelloServlet.java          @WebServlet("/hello")
    │   ├── StudentServlet.java        @WebServlet("/students")  GET list / POST add
    │   ├── RequestLoggingFilter.java  @WebFilter("/*")
    │   ├── StudentStore.java          in-memory data + validation
    │   └── Page.java                  shared HTML layout
    ├── main/webapp/                   -> copied to the ROOT of the war
    │   ├── index.html, 404.html, css/style.css
    │   └── WEB-INF/web.xml            welcome file, 404 page, session timeout
    ├── main/webapp-filtered/build-info.txt   -> ${project.version} etc. replaced at build time
    └── test/java/com/javaassign5/ex4/StudentStoreTest.java
```

**Key pom.xml parts:**
```xml
<packaging>war</packaging>

<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>          <!-- server supplies it: NOT put in WEB-INF/lib -->
</dependency>

<build>
    <finalName>student-portal</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <version>3.4.0</version>
            <configuration>
                <warSourceDirectory>src/main/webapp</warSourceDirectory>
                <failOnMissingWebXml>true</failOnMissingWebXml>
                <webResources>
                    <resource>
                        <directory>src/main/webapp-filtered</directory>
                        <filtering>true</filtering>
                    </resource>
                </webResources>
                <archive><manifest><addDefaultImplementationEntries>true</addDefaultImplementationEntries></manifest></archive>
                <packagingExcludes>**/*.psd,**/.DS_Store</packagingExcludes>
            </configuration>
        </plugin>
        <plugin>   <!-- mvn jetty:run -->
            <groupId>org.eclipse.jetty.ee10</groupId>
            <artifactId>jetty-ee10-maven-plugin</artifactId>
            <version>12.0.12</version>
        </plugin>
    </plugins>
</build>
```

**Commands:**
| Command | What it does |
|---|---|
| `mvn clean package` | Builds `target/student-portal.war` (tests run first) |
| `mvn jetty:run` | Starts the app at **http://localhost:8080/** straight from the source folders (Ctrl+C to stop) |
| `mvn jetty:run-war` | Same but runs the built WAR (so `build-info.txt` with filtered values is available) |
| Copy `target/student-portal.war` to `<Tomcat 10.1+>/webapps/` | Real deployment → **http://localhost:8080/student-portal/** |
| `mvn test` | 3 unit tests for `StudentStore` |

**What's inside the WAR** (`jar tf target/student-portal.war`):
```
index.html
404.html
css/style.css
build-info.txt                        <- filtered: "Version  : 1.0.0"
WEB-INF/web.xml
WEB-INF/classes/com/javaassign5/ex4/HelloServlet.class
WEB-INF/classes/com/javaassign5/ex4/StudentServlet.class
WEB-INF/classes/com/javaassign5/ex4/RequestLoggingFilter.class
WEB-INF/classes/com/javaassign5/ex4/StudentStore.class ...
WEB-INF/lib/commons-text-1.12.0.jar   <- compile scope: packaged
WEB-INF/lib/commons-lang3-3.14.0.jar  <- its transitive dependency: packaged
META-INF/MANIFEST.MF
(no jakarta.servlet-api jar - provided scope)
```

**Pages:** `/` home with a greeting form · `/hello?name=Shrey` → "Hello, Shrey!" (HTML-escaped with commons-text, so `<script>` input is harmless) · `/students` table + "Add a student" form (POST → redirect) · any unknown URL → custom 404 page. The filter logs each request, e.g. `GET /students (3 ms)`.

**Concepts used:** `war` packaging and maven-war-plugin, `provided` scope (Q48), resource filtering, plugin configuration (Q46), annotations in a framework (`@WebServlet`, `@WebFilter` – Q14).

---

## Exercise 5 — Multi-module project with shared dependencies in the parent ("Library System")

```
Exercise5_MultiModuleProject/
├── pom.xml                    PARENT (packaging=pom) - shared deps, managed versions, plugin versions
├── ex5-model/                 Book.java (+ BookTest)
├── ex5-service/               LibraryService.java (+ LibraryServiceTest)   -> depends on ex5-model
└── ex5-app/                   LibraryApp.java (main, Gson report, fat jar) -> depends on ex5-service
```

**What the parent shares – three different mechanisms:**
| Parent section | Contents | Effect on the children |
|---|---|---|
| `<dependencies>` | `commons-lang3`, `junit-jupiter` (test) | **Every module gets them automatically** – none of the child poms mention them, yet `Book`, `LibraryService` and `LibraryApp` all use `StringUtils`, and all tests use JUnit |
| `<dependencyManagement>` | `gson`, `ex5-model`, `ex5-service` with versions | Children **opt in** by naming them **without a version** (`ex5-app` uses Gson; nobody else gets it) |
| `<pluginManagement>` | compiler, surefire, exec, shade versions | Children use plugins without versions |

```xml
<!-- ex5-app/pom.xml - note: no versions, no commons-lang3, no junit -->
<dependencies>
    <dependency>
        <groupId>com.javaassign5</groupId>
        <artifactId>ex5-service</artifactId>        <!-- ex5-model arrives transitively -->
    </dependency>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
    </dependency>
</dependencies>
```

**Commands (from the `Exercise5_MultiModuleProject` folder):**
| Command | What it does |
|---|---|
| `mvn clean install` | Builds model → service → app in the right order, runs 6 tests |
| `mvn -q -pl ex5-app exec:java` | Runs the app (after `install`) |
| `java -jar ex5-app/target/ex5-app-1.0.0-all.jar` | Runs the fat jar |
| `mvn -pl ex5-service -am test` | Tests only the service module and what it needs |
| `mvn -pl ex5-app dependency:tree` | Shows the inherited and transitive dependencies of the app |

**Expected `mvn clean install` summary:**
```
[INFO] Reactor Build Order:
[INFO] Exercise 5 - Library System (parent)                               [pom]
[INFO] Exercise 5 - Model                                                 [jar]
[INFO] Exercise 5 - Service                                               [jar]
[INFO] Exercise 5 - App                                                   [jar]
...
[INFO] Exercise 5 - Library System (parent) ............... SUCCESS
[INFO] Exercise 5 - Model ................................. SUCCESS
[INFO] Exercise 5 - Service ............................... SUCCESS
[INFO] Exercise 5 - App ................................... SUCCESS
[INFO] BUILD SUCCESS
```

**Output of the app:**
```
============================= Catalogue ==============================
  Effective Java                      Joshua Bloch        2018  available
  Java Concurrency in Practice        Brian Goetz         2006  available
  Clean Code                          Robert Martin       2008  available
  Spring in Action                    Craig Walls         2018  available

============================= Borrowing ==============================
  Borrowed: Clean Code
  Refused : 'Clean Code' is already on loan

=========================== Search 'java' ============================
  Effective Java                      Joshua Bloch        2018  available
  Java Concurrency in Practice        Brian Goetz         2006  available

========================= JSON report (Gson) =========================
{
  "totalBooks": 4,
  "available": 3,
  "onLoan": [
    "Clean Code"
  ]
}
```

**Concepts used:** multi-module projects and the reactor (Q47), inheritance of `<dependencies>`, `dependencyManagement` and `pluginManagement` (Q32, Q41, Q46), transitive module dependencies (Q42), fat jars (Q45).
