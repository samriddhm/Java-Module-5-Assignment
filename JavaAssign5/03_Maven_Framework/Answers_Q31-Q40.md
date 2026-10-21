# Maven Framework — Answers (Q31–Q40)

Theory is answered here. Questions that ask for configuration have a ready-to-build Maven project:

| Question | Project folder | Commands (run inside the folder) |
|---|---|---|
| Q34 | `Q34_AddDependency/` | `mvn compile exec:java` · `mvn test` · `mvn dependency:tree` |
| Q38 | `Q38_JavaVersion/` | `mvn compile exec:java` |
| Q40 | `Q40_SpringBootPom/` | `mvn spring-boot:run` → open `http://localhost:8080/hello?name=Shrey` · `mvn test` |

> Requires JDK 17+ and Maven 3.6.3+ (`mvn -v` to check). The first build downloads libraries from Maven Central, so it needs internet.

---

## Q31. What is the purpose of Maven in Java development?

**Maven is a build automation and project management tool** for Java. You *describe* your project in one file (`pom.xml`) and Maven does the repetitive work.

| Without Maven | With Maven |
|---|---|
| Download every `.jar` by hand, plus the jars *those* jars need | Write the dependency once – Maven downloads it **and its dependencies** |
| Long `javac -cp a.jar:b.jar:...` commands | `mvn compile` |
| Every project has its own folder layout | **Standard layout** – any Java developer knows where things are |
| Run tests manually | `mvn test` runs all tests automatically |
| Build scripts differ per machine/IDE | Same build on every machine, IDE and CI server |

**Main jobs of Maven:**
1. **Dependency management** – downloads libraries from repositories, handles versions and transitive dependencies.
2. **Build lifecycle** – compile → test → package → install → deploy with one command.
3. **Standard project structure** – "convention over configuration":
   ```
   my-app/
   ├── pom.xml
   └── src/
       ├── main/java/        <- your code
       ├── main/resources/   <- config files
       ├── test/java/        <- unit tests
       └── test/resources/
   target/                   <- everything Maven builds goes here
   ```
4. **Packaging** – produces JAR, WAR or EAR files.
5. **Plugins** – extra tasks (code coverage, docs, deploying, Docker images…).
6. **Multi-module projects** – build many related projects together.
7. **Reporting/documentation** – `mvn site`, javadoc, test reports.

---

## Q32. Explain the role of the `pom.xml` file in Maven.

`pom.xml` = **Project Object Model**. It is the **heart of a Maven project** – an XML file in the project root that tells Maven *everything* about the project: what it is, what it needs and how to build it.

**Main sections:**

| Section | Purpose |
|---|---|
| `<modelVersion>` | Always `4.0.0` |
| `<groupId>`, `<artifactId>`, `<version>` | **Coordinates (GAV)** – the unique name of the project, e.g. `com.javaassign5 : q34-add-dependency : 1.0.0` |
| `<packaging>` | Output type: `jar` (default), `war`, `pom`, `ear` |
| `<name>`, `<description>`, `<url>` | Human-readable info |
| `<parent>` | Inherit settings from another POM (e.g. Spring Boot parent) |
| `<properties>` | Variables, e.g. Java version, library versions; used as `${name}` |
| `<dependencies>` | Libraries the project uses |
| `<dependencyManagement>` | Central place to fix versions (mostly in parent POMs) |
| `<build>` → `<plugins>` | Plugins and their configuration |
| `<modules>` | Sub-projects (multi-module) |
| `<repositories>` | Extra places to download from |
| `<distributionManagement>` | Where `mvn deploy` uploads to |
| `<profiles>` | Alternative settings (e.g. `dev` vs `prod`) |

**Minimal example:**
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>hello</artifactId>
    <version>1.0-SNAPSHOT</version>
</project>
```
Everything not written is inherited from Maven's built-in **Super POM** (default folders, default plugins, Maven Central as repository). `mvn help:effective-pom` shows the full combined POM.

---

## Q33. What are the differences between the local repository, central repository, and remote repository in Maven?

A **repository** is a storage place for artifacts (JARs, POMs, plugins).

| | Local repository | Central repository | Remote repository |
|---|---|---|---|
| **Where** | On **your computer**: `~/.m2/repository` (Windows `C:\Users\<you>\.m2\repository`) | On the internet: `https://repo.maven.apache.org/maven2` | Any other server – company Nexus/Artifactory, JitPack, GitHub Packages… |
| **Who manages it** | Maven fills it automatically | Maven community (Sonatype) | Your company / a third party |
| **What's in it** | Everything you've downloaded + what you `mvn install` | Almost every open-source Java library | Private company libraries, mirrors, libraries not on Central |
| **Configured in** | default; can change in `settings.xml` `<localRepository>` | built into Maven (Super POM) | `<repositories>` in `pom.xml` or `settings.xml`; `<mirrors>` |
| **Needs internet?** | No (works offline: `mvn -o`) | Yes | Usually yes (often inside company network) |

**Search order when Maven needs a dependency:**
```
1. Local repository (~/.m2)      -> found? use it (fast, no download)
2. Central / remote repositories -> download it and SAVE a copy in local
3. Not found anywhere             -> build fails: "Could not resolve dependencies"
```
Technically, *Central is one kind of remote repository* — the default one.

```xml
<!-- Adding an extra remote repository in pom.xml -->
<repositories>
    <repository>
        <id>company-repo</id>
        <url>https://nexus.mycompany.com/repository/maven-public/</url>
    </repository>
</repositories>
```

---

## Q34. How do you add a dependency to a Maven project? Provide an example.

**Steps:**
1. Find the library's coordinates (groupId, artifactId, version) – on [search.maven.org](https://search.maven.org) or [mvnrepository.com](https://mvnrepository.com).
2. Add a `<dependency>` inside `<dependencies>` in `pom.xml`.
3. Optionally add a `<scope>` (`compile` is the default; `test` for test-only libraries).
4. Run a build (`mvn compile`) – Maven downloads it (and its own dependencies) into `~/.m2`. In an IDE, click "Reload Maven project".

**Project:** `Q34_AddDependency/`
```xml
<properties>
    <commons-lang3.version>3.14.0</commons-lang3.version>
    <gson.version>2.10.1</gson.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>${commons-lang3.version}</version>
    </dependency>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>${gson.version}</version>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
`App.java` uses both libraries; `AppTest.java` uses JUnit.

**Expected output of `mvn -q compile exec:java`:**
```
commons-lang3 StringUtils:
  normalizeSpace : 'maven makes dependencies easy'
  isBlank("  ")  : true
  reverse        : nevam
  abbreviate     : Dependency m...

Gson toJson:
{
  "name": "Shrey",
  "course": "Software Engineering",
  "year": 1
}
Gson fromJson -> name = Shrey
```

---

## Q35. What is the purpose of Maven plugins?

**Maven itself does almost nothing** — it is a framework that runs **plugins**. *All* real work (compiling, testing, packaging, deploying) is done by plugins. Each plugin offers one or more **goals** (tasks).

| Plugin | Goal(s) | What it does |
|---|---|---|
| `maven-compiler-plugin` | `compile`, `testCompile` | Compiles Java code |
| `maven-surefire-plugin` | `test` | Runs unit tests |
| `maven-failsafe-plugin` | `integration-test`, `verify` | Runs integration tests |
| `maven-jar-plugin` / `maven-war-plugin` | `jar` / `war` | Packages the project |
| `maven-shade-plugin` / `maven-assembly-plugin` | `shade` / `single` | Builds a "fat" JAR with all dependencies |
| `maven-install-plugin` | `install` | Copies the artifact to `~/.m2` |
| `maven-deploy-plugin` | `deploy` | Uploads to a remote repository |
| `maven-clean-plugin` | `clean` | Deletes `target/` |
| `maven-dependency-plugin` | `tree`, `analyze` | Shows/inspects dependencies |
| `maven-javadoc-plugin` | `javadoc` | Generates API docs |
| `exec-maven-plugin` | `java` | Runs a main class |
| `jacoco-maven-plugin` | `prepare-agent`, `report` | Code coverage |
| `spring-boot-maven-plugin` | `run`, `repackage` | Runs/packages Spring Boot apps |

**Two kinds:** *build plugins* (in `<build><plugins>`, run during the build) and *reporting plugins* (in `<reporting>`, used by `mvn site`).

**Purposes in short:** reuse (one plugin works for every project), extendability (add any new task), and consistency (the same steps everywhere). Configuration and examples: see Q46 in section 4.

---

## Q36. Explain the Maven build lifecycle.

A **build lifecycle** is a **fixed, ordered list of phases** (steps). When you ask for a phase, Maven runs **every phase before it, in order**. Plugin goals are attached ("bound") to phases, so each phase knows what to do.

Maven has **three built-in lifecycles**:

| Lifecycle | Job | Main phases |
|---|---|---|
| **clean** | Remove old build output | `pre-clean` → `clean` → `post-clean` |
| **default** (a.k.a. build) | Build, test, package, deploy | `validate` → `compile` → `test` → `package` → `verify` → `install` → `deploy` (full list in Q37) |
| **site** | Generate a project website/reports | `pre-site` → `site` → `post-site` → `site-deploy` |

**Picture it like a staircase:**
```
validate → compile → test → package → verify → install → deploy
                              ▲
              "mvn package" climbs every step up to here
```

**Examples:**
| Command | What actually runs |
|---|---|
| `mvn compile` | validate, compile |
| `mvn test` | validate, compile, test-compile, **test** |
| `mvn package` | … everything up to **package** → makes `target/*.jar` |
| `mvn install` | … + install → copies JAR into `~/.m2` |
| `mvn clean install` | clean lifecycle first, then default lifecycle up to install |

**Bindings:** the packaging type decides which goals run in which phase. For `jar` packaging, e.g. `compile` → `compiler:compile`, `test` → `surefire:test`, `package` → `jar:jar`, `install` → `install:install`, `deploy` → `deploy:deploy`.

---

## Q37. What are the default phases of the Maven lifecycle?

The **default lifecycle has 23 phases** (Maven 3). The ones you use every day are in **bold**:

| # | Phase | What happens |
|---|---|---|
| 1 | **validate** | Check the project is correct and all info is available |
| 2 | initialize | Set up build state, properties |
| 3 | generate-sources | Generate source code (e.g. from annotation processors, WSDL) |
| 4 | process-sources | Filter/process source code |
| 5 | generate-resources | Generate resources |
| 6 | process-resources | Copy `src/main/resources` into `target/classes` |
| 7 | **compile** | Compile `src/main/java` |
| 8 | process-classes | Post-process compiled classes (e.g. bytecode enhancement) |
| 9 | generate-test-sources | Generate test source code |
| 10 | process-test-sources | Process test sources |
| 11 | generate-test-resources | Create test resources |
| 12 | process-test-resources | Copy `src/test/resources` |
| 13 | test-compile | Compile `src/test/java` |
| 14 | process-test-classes | Post-process test classes |
| 15 | **test** | Run unit tests (Surefire) – the build fails if a test fails |
| 16 | prepare-package | Prepare before packaging |
| 17 | **package** | Create the JAR/WAR in `target/` |
| 18 | pre-integration-test | Set up the integration test environment |
| 19 | integration-test | Run integration tests (Failsafe) |
| 20 | post-integration-test | Tear down the environment |
| 21 | **verify** | Check the package is valid and quality criteria are met |
| 22 | **install** | Put the package into the **local** repository (`~/.m2`) |
| 23 | **deploy** | Upload the package to a **remote** repository |

**The 7 key phases to remember:** `validate → compile → test → package → verify → install → deploy`.

---

## Q38. How do you configure the Java version in a Maven project?

There are three ways – **option 1 is recommended**:

```xml
<!-- OPTION 1 (JDK 9+): "release" sets source + target + the allowed Java API in one go -->
<properties>
    <maven.compiler.release>17</maven.compiler.release>
</properties>

<!-- OPTION 2 (older, works on JDK 8): set source and target separately -->
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>

<!-- OPTION 3: configure the compiler plugin directly -->
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <release>17</release>
            </configuration>
        </plugin>
    </plugins>
</build>

<!-- SPRING BOOT projects: the parent POM reads this property -->
<properties>
    <java.version>17</java.version>
</properties>
```

**Why `release` is better than `source`/`target`:** with `source/target 8` on JDK 17, you could accidentally use a Java 11 API (e.g. `String.isBlank()`) and it would compile but crash on Java 8. `release 8` checks the API too, so it fails at compile time.

**Important:** the JDK that *runs* Maven must be **at least** the version you target (`mvn -v` shows it). The `maven-enforcer-plugin` with `requireJavaVersion` gives a clear error if not.

**Project:** `Q38_JavaVersion/` uses option 1 plus the enforcer plugin. `App.java` uses a **record**, a **switch expression** and a **text block**, so it only compiles at Java 17+. It also reads its own `.class` file header to prove which version it was compiled for.

**Expected output of `mvn -q compile exec:java` (on JDK 21):**
```
Java version running this app : 21.0.x
Compiled for Java (class file) : 17
Course : Course[code=SOFT1902, credits=6] (standard unit)
```
Change `17` to `11` in the pom and `mvn compile` fails with errors such as *"records are not supported in -source 11"*.

---

## Q39. What is a Maven goal, and how is it different from a phase?

- A **goal** is a **specific task done by a plugin**. Written `plugin:goal`, e.g. `compiler:compile`, `surefire:test`, `jar:jar`, `dependency:tree`.
- A **phase** is a **step in a lifecycle** (e.g. `compile`, `test`, `package`). A phase does nothing on its own – it is a *slot* that goals are bound to.

**Analogy:** the lifecycle is a recipe. **Phases** are the steps ("prepare", "cook", "serve"). **Goals** are the actual actions done by tools ("chop onions with knife", "fry in pan"). Each step can have zero, one or many actions.

| | Phase | Goal |
|---|---|---|
| What it is | A stage in a lifecycle | A task inside a plugin |
| Example | `package` | `jar:jar` |
| Command | `mvn package` | `mvn jar:jar` or `mvn dependency:tree` |
| Runs earlier steps? | **Yes** – all previous phases in the lifecycle | **No** – only that one goal |
| Who defines it | Maven (fixed list) | Each plugin |
| Relationship | Has 0..n goals bound to it | Bound to 0..1 phase (or run directly) |

**Example – the difference in practice:**
```bash
mvn package           # runs validate, compile, test ... package  -> tested JAR
mvn jar:jar           # ONLY creates the JAR from whatever is already in target/classes
mvn dependency:tree   # a goal not bound to any phase - just prints the dependency tree
mvn clean dependency:tree compile   # you can mix phases and goals
```

**Binding a goal to a phase in `pom.xml`:**
```xml
<plugin>
    <artifactId>maven-source-plugin</artifactId>
    <executions>
        <execution>
            <phase>package</phase>          <!-- the phase -->
            <goals><goal>jar</goal></goals> <!-- the goal: source:jar -->
        </execution>
    </executions>
</plugin>
```

---

## Q40. Write an example `pom.xml` file for a project using Spring Boot.

**Project:** `Q40_SpringBootPom/` – a complete, runnable Spring Boot 3 REST app.

```
Q40_SpringBootPom/
├── pom.xml
└── src/
    ├── main/java/com/javaassign5/q40/
    │   ├── DemoApplication.java        (@SpringBootApplication main class)
    │   └── GreetingController.java     (GET /hello, POST /students with validation)
    ├── main/resources/application.properties
    └── test/java/com/javaassign5/q40/GreetingControllerTest.java
```

**`pom.xml`:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.5</version>
        <relativePath/>
    </parent>

    <groupId>com.javaassign5</groupId>
    <artifactId>q40-spring-boot-demo</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**What each part does:**
| Part | Why |
|---|---|
| `<parent>` spring-boot-starter-parent | Provides tested versions of all Spring/3rd-party libraries (so starters have **no `<version>`**), Java/encoding defaults, and pre-configured plugins |
| `<java.version>17` | Spring Boot 3 needs Java 17+ |
| `spring-boot-starter-web` | Spring MVC + Jackson (JSON) + embedded Tomcat |
| `spring-boot-starter-validation` | Bean Validation (`@NotBlank`, `@Size`, `@Valid`) |
| `spring-boot-starter-actuator` | `/actuator/health` monitoring endpoints |
| `spring-boot-devtools` (runtime, optional) | Auto-restart while coding; not shipped to other projects |
| `spring-boot-starter-test` (test) | JUnit 5, Mockito, AssertJ, MockMvc |
| `spring-boot-maven-plugin` | `mvn spring-boot:run`, and repackages the JAR into an executable fat JAR |

**Try it:**
```bash
mvn spring-boot:run
curl "http://localhost:8080/hello?name=Shrey"          # {"message":"Hello, Shrey!"}
curl -X POST localhost:8080/students -H "Content-Type: application/json" \
     -d '{"name":"Shrey","year":1}'                     # {"registered":"Shrey","year":1}
curl -X POST localhost:8080/students -H "Content-Type: application/json" \
     -d '{"name":"","year":9}'                          # HTTP 400 - validation failed
curl localhost:8080/actuator/health                     # {"status":"UP"}

mvn clean package && java -jar target/q40-spring-boot-demo-1.0.0.jar
```
Alternative without a parent: import `spring-boot-dependencies` as a BOM in `<dependencyManagement>` with `<scope>import</scope>`.
