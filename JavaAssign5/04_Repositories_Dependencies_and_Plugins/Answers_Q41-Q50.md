# Repositories, Dependencies, and Plugins — Answers (Q41–Q50)

Theory is answered here; every question that needs configuration has a ready-to-build Maven project.

| Question | Project folder | Commands (inside the folder) |
|---|---|---|
| Q41 | `Q41_ConflictResolution/` | `mvn compile exec:exec` · `mvn dependency:tree -Dverbose` |
| Q43 | `Q43_ExcludeTransitive/` | `mvn dependency:tree` · `mvn compile exec:exec` |
| Q45 | `Q45_CreateJar/` | `mvn clean package` → `java -jar target/q45-create-jar-1.0.0-all.jar Hello Maven` |
| Q46 | `Q46_PluginConfiguration/` | `mvn validate` · `mvn compile exec:java` |
| Q47 | `Q47_MultiModuleProject/` | `mvn clean install` → `mvn -pl q47-app exec:java` |
| Q49 | `Q49_DeployToRemoteRepo/` | `mvn clean deploy -DaltDeploymentRepository=local-folder::file:./target/remote-repo` |
| Q50 | `Q50_SurefireUnitTests/` | `mvn test` · `mvn test -Pall-tests` |

> Requires JDK 17+ and Maven 3.6.3+. The first build needs internet to download from Maven Central.

---

## Q41. How does Maven resolve conflicts when multiple versions of a dependency are included?

When two paths in the dependency tree bring **different versions of the same library**, Maven must pick **one** (only one version of a class can be on the classpath). It uses **"dependency mediation"** with these rules, in order of strength:

| # | Rule | Meaning |
|---|---|---|
| 1 | **`<dependencyManagement>` wins** | A version fixed in `dependencyManagement` (yours, a parent's, or an imported BOM) is used for **both direct and transitive** occurrences. Strongest control. |
| 2 | **Nearest definition wins** | The version **closest to your project** in the tree is chosen. A direct dependency (depth 1) always beats a transitive one (depth 2+). |
| 3 | **First declaration wins** (tie-breaker) | If two versions are at the **same depth**, the one whose path is declared **first** in `pom.xml` wins. |

> ⚠️ Maven does **not** automatically choose the *newest* version. (Gradle does.) That is why you sometimes get `NoSuchMethodError` at runtime – an older version won.

**Example tree:**
```
my-app
├── A 1.0
│   └── C 2.0          (depth 2)
└── B 1.0
    └── D 1.0
        └── C 1.0      (depth 3)
=> C 2.0 is used (nearest)
```

**Ways to control it yourself:**
- Declare the library **directly** with the version you want (rule 2).
- Pin it in **`<dependencyManagement>`** (rule 1) – best for multi-module projects.
- **Exclude** the unwanted transitive version (Q43).
- Use the **maven-enforcer-plugin** rules `dependencyConvergence` / `requireUpperBoundDeps` to make the build **fail** when conflicts exist.
- Inspect with `mvn dependency:tree -Dverbose` (shows "omitted for conflict with …").

**Project:** `Q41_ConflictResolution/`
```xml
<dependencies>
    <!-- brings commons-lang3 3.14.0 transitively (depth 2) -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-text</artifactId>
        <version>1.12.0</version>
    </dependency>
    <!-- direct (depth 1) - NEAREST, so this wins -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.12.0</version>
    </dependency>
</dependencies>

<!-- uncomment in the pom to see rule 1 beat rule 2 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.17.0</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```
**Expected output of `mvn dependency:tree -Dverbose` (shortened):**
```
com.javaassign5:q41-conflict-resolution:jar:1.0.0
+- org.apache.commons:commons-text:jar:1.12.0:compile
|  \- (org.apache.commons:commons-lang3:jar:3.14.0:compile - omitted for conflict with 3.12.0)
\- org.apache.commons:commons-lang3:jar:3.12.0:compile
```
**Expected output of `mvn -q compile exec:exec`:**
```
commons-text  loaded from: commons-text-1.12.0.jar
commons-lang3 loaded from: commons-lang3-3.12.0.jar
commons-lang3 version    : 3.12.0
```
With the `dependencyManagement` block uncommented, it prints `commons-lang3-3.17.0.jar`.

---

## Q42. What is a transitive dependency in Maven?

A **transitive dependency** is a dependency **of your dependency**. You only declare what *you* use; Maven reads each library's own `pom.xml` and automatically downloads what *it* needs, and so on, all the way down.

```
Your project
└── spring-boot-starter-web          <- DIRECT (you wrote it in pom.xml)
    ├── spring-webmvc                <- TRANSITIVE (depth 2)
    │   └── spring-core              <- TRANSITIVE (depth 3)
    ├── jackson-databind             <- TRANSITIVE
    └── tomcat-embed-core            <- TRANSITIVE
```
One line in your pom can pull in dozens of jars. See them with `mvn dependency:tree`.

**Benefits:** no manual hunting for jars; versions that the library authors tested.
**Risks:** version conflicts (Q41), "dependency bloat", unwanted libraries (fix with exclusions – Q43), security issues in libraries you didn't know you had.

**Scope affects whether it is passed on:**

| Your dependency's scope → its dependency's scope | compile | provided | runtime | test |
|---|---|---|---|---|
| **compile** | compile | – | runtime | – |
| **provided** | provided | – | provided | – |
| **runtime** | runtime | – | runtime | – |
| **test** | test | – | test | – |

(`–` = not transitive.) So `provided` and `test` dependencies of a library are **never** passed to you, and `<optional>true</optional>` dependencies are not passed on either.

**Real example used in this assignment:** `Q41_ConflictResolution` – `commons-text` brings `commons-lang3`. `05_Practical_Exercises/Exercise2_TransitiveDependencies` uses `StringUtils` from `commons-lang3` **without declaring it**.

---

## Q43. How do you exclude a specific transitive dependency in Maven?

Add an **`<exclusions>`** block **inside the dependency that brings it in**. You give only `groupId` and `artifactId` – **no version**.

**Common reasons:** you want to replace it with something else (e.g. a logging bridge), it conflicts with another library, it has a security problem, or it is already provided by the server.

**Project:** `Q43_ExcludeTransitive/` – Apache HttpClient normally brings `commons-logging`. We exclude it and replace it with `jcl-over-slf4j` (same API, sends logs to SLF4J).

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.14</version>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- replacement -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
    <version>2.0.13</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>2.0.13</version>
</dependency>
```

**`mvn dependency:tree` – before vs after:**
```
BEFORE                                            AFTER (with exclusion)
+- httpclient:4.5.14                              +- httpclient:4.5.14
|  +- httpcore:4.4.16                             |  +- httpcore:4.4.16
|  +- commons-logging:1.2     <- removed ->       |  \- commons-codec:1.11
|  \- commons-codec:1.11                          +- jcl-over-slf4j:2.0.13
                                                  |  \- slf4j-api:2.0.13
                                                  \- slf4j-simple:2.0.13
```
**Expected output of `mvn -q compile exec:exec`:**
```
HttpClient works      : built request GET https://example.com/api HTTP/1.1
LogFactory loaded from: jcl-over-slf4j-2.0.13.jar
HttpGet loaded from   : httpclient-4.5.14.jar
[main] INFO com.javaassign5.q43.ExclusionCheck - this commons-logging call is printed by SLF4J (slf4j-simple)
Original commons-logging jar on classpath? false
```

**Other options:**
- Exclude **everything** a dependency brings (Maven 3.2.1+): `<exclusion><groupId>*</groupId><artifactId>*</artifactId></exclusion>`
- If *you* publish a library, mark a dependency `<optional>true</optional>` so it is never passed on.
- Exclusions apply only to the path of **that** dependency – if another dependency also brings it, exclude it there too (check `mvn dependency:tree`).

---

## Q44. What is the purpose of the maven-compiler-plugin?

The **maven-compiler-plugin compiles your Java source code** (it runs `javac` for you). It is bound to the lifecycle automatically, so you don't have to declare it, but you usually *do* declare it to fix its version and configure it.

| Goal | Default phase | Compiles |
|---|---|---|
| `compiler:compile` | `compile` | `src/main/java` → `target/classes` |
| `compiler:testCompile` | `test-compile` | `src/test/java` → `target/test-classes` |

**Things you configure with it:**
| Setting | Example | Purpose |
|---|---|---|
| `release` (or `source` + `target`) | `17` | Java version (Q38) |
| `encoding` | `UTF-8` | Source file encoding |
| `compilerArgs` | `-Xlint:all`, `-parameters`, `-Werror` | Extra javac flags |
| `annotationProcessorPaths` | Lombok, MapStruct | Run **annotation processors** at compile time (links to Q20!) |
| `showWarnings`, `showDeprecation` | `true` | Print warnings |
| `fork`, `executable` | another JDK | Compile with a different `javac` |

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <release>17</release>
        <encoding>UTF-8</encoding>
        <showWarnings>true</showWarnings>
        <compilerArgs>
            <arg>-Xlint:all</arg>
            <arg>-parameters</arg>
        </compilerArgs>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.34</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```
Live example: `Q46_PluginConfiguration` configures `-Xlint:all` and `-parameters` and proves `-parameters` worked.

---

## Q45. How can you use Maven to create a JAR file for your project?

**Basic way:** keep `<packaging>jar</packaging>` (the default) and run:
```bash
mvn clean package
# -> target/<artifactId>-<version>.jar      e.g. target/q45-create-jar-1.0.0.jar
```
The `package` phase runs `maven-jar-plugin:jar`, which zips `target/classes` (your compiled classes + resources) and a `META-INF/MANIFEST.MF`. Tests run first – skip them with `-DskipTests`. `mvn install` also copies the jar to `~/.m2`.

**Two kinds of JAR:**
| | Thin (normal) JAR | Fat / Uber JAR |
|---|---|---|
| Contains | Only your classes | Your classes **+ all dependencies** |
| Made by | `maven-jar-plugin` | `maven-shade-plugin` or `maven-assembly-plugin` (or `spring-boot-maven-plugin`) |
| Run with | `java -cp app.jar:libs/* Main` (or `java -jar` with Class-Path manifest + `lib/` folder) | `java -jar app-all.jar` – works anywhere |
| Use for | Libraries other projects depend on | Applications you ship |

**Project:** `Q45_CreateJar/` builds **both**.
```xml
<!-- (1) THIN JAR with Main-Class and Class-Path in the manifest -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.4.1</version>
    <configuration>
        <archive>
            <manifest>
                <mainClass>com.javaassign5.q45.JarApp</mainClass>
                <addClasspath>true</addClasspath>
                <classpathPrefix>lib/</classpathPrefix>
                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
            </manifest>
        </archive>
    </configuration>
</plugin>

<!-- (2) FAT JAR -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.3</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals><goal>shade</goal></goals>
            <configuration>
                <shadedArtifactAttached>true</shadedArtifactAttached>
                <shadedClassifierName>all</shadedClassifierName>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.javaassign5.q45.JarApp</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```
The pom also uses `maven-dependency-plugin:copy-dependencies` to put the libraries in `target/lib/`, so the thin jar can run too.

**Expected result:**
```
$ mvn -q clean package
$ ls target/*.jar target/lib
target/q45-create-jar-1.0.0-all.jar   target/q45-create-jar-1.0.0.jar
target/lib: commons-lang3-3.14.0.jar

$ java -jar target/q45-create-jar-1.0.0-all.jar Hello Maven
Running from : q45-create-jar-1.0.0-all.jar
App version  : 1.0.0
Java         : 21.0.x
Your words   : *********HELLO MAVEN**********

$ java -jar target/q45-create-jar-1.0.0.jar Hello Maven      # thin jar + target/lib
Running from : q45-create-jar-1.0.0.jar
...
```
Look inside with `jar tf target/q45-create-jar-1.0.0.jar`.

---

## Q46. How do you include and configure a plugin in pom.xml? Provide an example.

Plugins go in **`<build><plugins>`**. Each one is identified by `groupId` + `artifactId` + `version` (always pin the version for repeatable builds).

**The building blocks:**
| Element | Use |
|---|---|
| `<configuration>` directly under `<plugin>` | Settings for **all** goals of that plugin |
| `<executions><execution>` | **Bind** a goal to a lifecycle `<phase>`, with its own `<id>` and optional `<configuration>` |
| `<dependencies>` inside `<plugin>` | Extra jars the plugin itself needs |
| `<pluginManagement>` | Declare versions/config **without activating** – used in parent POMs; children then just name the plugin |
| `<inherited>false</inherited>` | Don't pass this plugin to child modules |

**Project:** `Q46_PluginConfiguration/` shows all four common patterns:
```xml
<build>
    <!-- A) pluginManagement: version decided once -->
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>
        </plugins>
    </pluginManagement>

    <plugins>
        <!-- B) plugin-level configuration -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <compilerArgs>
                    <arg>-Xlint:all</arg>
                    <arg>-parameters</arg>
                </compilerArgs>
            </configuration>
        </plugin>

        <!-- C) execution bound to a phase -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>3.1.0</version>
            <executions>
                <execution>
                    <id>banner</id>
                    <phase>validate</phase>
                    <goals><goal>run</goal></goals>
                    <configuration>
                        <target>
                            <echo message="=== Building ${project.artifactId} ${project.version} ==="/>
                        </target>
                    </configuration>
                </execution>
            </executions>
        </plugin>

        <!-- D) goal used on demand: mvn compile exec:java -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <mainClass>com.javaassign5.q46.PluginDemo</mainClass>
                <arguments><argument>configured-in-pom</argument></arguments>
            </configuration>
        </plugin>
    </plugins>
</build>
```
(The real pom also has `maven-enforcer-plugin` bound to `validate`, checking Maven ≥ 3.6.3 and Java ≥ 17.)

**Expected output of `mvn compile exec:java` (shortened):**
```
[INFO] --- enforcer:3.4.1:enforce (check-build-environment) @ q46-plugin-configuration ---
[INFO] --- antrun:3.1.0:run (banner) @ q46-plugin-configuration ---
     [echo] === Building q46-plugin-configuration 1.0.0 with Java 21.0.x ===
[INFO] --- compiler:3.13.0:compile (default-compile) @ q46-plugin-configuration ---
[INFO] --- exec:3.1.0:java (default-cli) @ q46-plugin-configuration ---
Argument from exec-maven-plugin config: configured-in-pom
Parameter names seen by reflection   : studentName times   (real names -> '-parameters' compiler flag worked)
Hi Shrey! Hi Shrey! Hi Shrey!
```
Find every option of a plugin: `mvn help:describe -Dplugin=compiler -Ddetail`.

---

## Q47. Explain how to create a multi-module Maven project.

A **multi-module project** is one big project made of several smaller Maven projects (**modules**) that are built together. A **parent POM** (packaging `pom`) lists the modules and shares settings.

**Why:** split code into clear parts (e.g. `core`, `web`, `app`), share versions/plugins in one place, build everything with one command, and let Maven work out the build order.

**Steps:**
1. **Create the parent folder and `pom.xml`** with `<packaging>pom</packaging>`.
2. **List the modules** in `<modules>` (folder names).
3. Put **shared settings** in the parent: `<properties>`, `<dependencyManagement>` (versions), `<dependencies>` (given to every module), `<pluginManagement>`.
4. **Create each module folder** with its own `pom.xml` that has a `<parent>` section pointing to the parent (groupId and version are then inherited).
5. **Module-to-module dependencies** are normal `<dependency>` entries (e.g. app → core).
6. **Build from the parent folder:** `mvn clean install`. Maven's **reactor** sorts modules so dependencies are built first.

(Shortcut: `mvn archetype:generate` in the parent folder adds new modules and updates `<modules>` automatically.)

**Project:** `Q47_MultiModuleProject/`
```
Q47_MultiModuleProject/
├── pom.xml                         (parent, packaging=pom)
├── q47-core/
│   ├── pom.xml
│   └── src/main/java/com/javaassign5/q47/core/GradeCalculator.java  (+ unit test)
└── q47-app/
    ├── pom.xml                     (depends on q47-core)
    └── src/main/java/com/javaassign5/q47/app/GradeApp.java
```
```xml
<!-- parent pom.xml -->
<groupId>com.javaassign5</groupId>
<artifactId>q47-multi-module</artifactId>
<version>1.0.0</version>
<packaging>pom</packaging>
<modules>
    <module>q47-core</module>
    <module>q47-app</module>
</modules>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.javaassign5</groupId>
            <artifactId>q47-core</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- q47-app/pom.xml -->
<parent>
    <groupId>com.javaassign5</groupId>
    <artifactId>q47-multi-module</artifactId>
    <version>1.0.0</version>
</parent>
<artifactId>q47-app</artifactId>
<dependencies>
    <dependency>
        <groupId>com.javaassign5</groupId>
        <artifactId>q47-core</artifactId>   <!-- no version needed -->
    </dependency>
</dependencies>
```

**Expected output of `mvn clean install`:**
```
[INFO] Reactor Build Order:
[INFO] q47-multi-module                                                   [pom]
[INFO] q47-core                                                           [jar]
[INFO] q47-app                                                            [jar]
...
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
...
[INFO] Reactor Summary for q47-multi-module 1.0.0:
[INFO] q47-multi-module ................................... SUCCESS
[INFO] q47-core ........................................... SUCCESS
[INFO] q47-app ............................................ SUCCESS
[INFO] BUILD SUCCESS
```
**Then `mvn -q -pl q47-app exec:java`:**
```
Mark  Grade   (GradeCalculator comes from module q47-core)
  92  HD
  78  DI
  66  CR
  55  PS
  41  FA
Average 66.4 -> CR
```
Useful flags: `-pl <module>` (only that module), `-am` (also make the modules it needs), `-rf <module>` (resume from). A bigger example with shared dependencies is in `05_Practical_Exercises/Exercise5_MultiModuleProject`.

---

## Q48. What is the difference between compile, provided, and runtime dependency scopes in Maven?

A **scope** says **when** a dependency is on the classpath and **whether it is packaged/passed on**.

| | `compile` (default) | `provided` | `runtime` |
|---|---|---|---|
| Available when **compiling** your code | ✅ | ✅ | ❌ |
| Available when **running tests** | ✅ | ✅ | ✅ |
| Available when **running** / packaged into WAR / fat JAR | ✅ | ❌ – someone else provides it | ✅ |
| **Transitive** (passed to projects using yours) | ✅ (as compile) | ❌ | ✅ (as runtime) |
| Meaning | "I need it always" | "I need it to compile, but the server/JDK will supply it at runtime" | "I don't call it directly in code, but it must be there when the app runs" |
| Typical examples | Spring, Guava, commons-lang3 | Servlet API (`jakarta.servlet-api`) on Tomcat, Lombok | JDBC drivers (MySQL, PostgreSQL), `slf4j-simple`, Jackson modules loaded by name |

```xml
<!-- compile: used everywhere -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.14.0</version>
</dependency>

<!-- provided: Tomcat already contains the servlet API -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>

<!-- runtime: code uses java.sql.* interfaces; the driver is only needed when running -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
    <scope>runtime</scope>
</dependency>
```
**Why it matters:** `provided` avoids shipping a second copy of a library the server already has (which can cause class conflicts); `runtime` stops developers from accidentally writing code that depends directly on an implementation.

The other scopes for completeness: **`test`** (only for compiling/running tests – JUnit), **`system`** (like provided but from a local file path – deprecated), **`import`** (only in `dependencyManagement`, imports a BOM). Real use of `provided`: `05_Practical_Exercises/Exercise4_WarWebApplication`.

---

## Q49. How do you deploy a Maven project to a remote repository?

`mvn deploy` runs the whole lifecycle up to the **deploy** phase, where **maven-deploy-plugin** uploads the JAR, POM (and any attached jars like `-sources.jar`) to a remote repository (Nexus, Artifactory, GitHub Packages, Maven Central…), so **other developers and projects** can depend on it.

**Steps:**
1. **Say where** – add `<distributionManagement>` to `pom.xml`, with a `<repository>` for releases and a `<snapshotRepository>` for `-SNAPSHOT` versions.
2. **Give credentials** – in `~/.m2/settings.xml` add a `<server>` whose `<id>` **matches** the repository id. (Never put passwords in `pom.xml` – it goes to Git. Use `mvn -ep` encryption or environment variables.)
3. **Run** `mvn clean deploy`.
4. Others add your coordinates as a normal `<dependency>` (with the repository in their `<repositories>` if it's private).

**Project:** `Q49_DeployToRemoteRepo/` – `pom.xml`, example `settings.xml`, a tiny library class.
```xml
<!-- pom.xml -->
<distributionManagement>
    <repository>
        <id>company-releases</id>
        <url>https://nexus.example.com/repository/maven-releases/</url>
    </repository>
    <snapshotRepository>
        <id>company-snapshots</id>
        <url>https://nexus.example.com/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```
```xml
<!-- ~/.m2/settings.xml -->
<servers>
    <server>
        <id>company-releases</id>
        <username>${env.NEXUS_USER}</username>
        <password>${env.NEXUS_PASSWORD}</password>
    </server>
</servers>
```
**Try it without a server** – deploy into a folder that acts as the remote repository:
```bash
mvn clean deploy -DaltDeploymentRepository=local-folder::file:./target/remote-repo
```
**Expected result:**
```
[INFO] --- deploy:3.1.2:deploy (default-deploy) @ q49-deploy-demo ---
Uploading to local-folder: file:./target/remote-repo/com/javaassign5/q49-deploy-demo/1.0.0/q49-deploy-demo-1.0.0.pom
Uploading to local-folder: file:./target/remote-repo/com/javaassign5/q49-deploy-demo/1.0.0/q49-deploy-demo-1.0.0.jar
Uploading to local-folder: file:./target/remote-repo/com/javaassign5/q49-deploy-demo/1.0.0/q49-deploy-demo-1.0.0-sources.jar
[INFO] BUILD SUCCESS
```
**Notes:**
- `install` = copy to **local** repo (`~/.m2`) only; `deploy` = upload to **remote**.
- **Releases** can normally be uploaded **once** per version; **snapshots** can be re-uploaded (they get timestamps).
- For Maven Central you also need GPG signatures, javadoc + sources jars, and an account on central.sonatype.com.
- A single file can be uploaded with `mvn deploy:deploy-file -Dfile=lib.jar -DgroupId=... -DartifactId=... -Dversion=... -Durl=... -DrepositoryId=...`.

---

## Q50. Write a Maven configuration to run unit tests using the maven-surefire-plugin.

**Surefire** runs unit tests in the **`test` phase** (`mvn test`, and before `package`/`install`). By default it finds classes named `Test*.java`, `*Test.java`, `*Tests.java`, `*TestCase.java` in `src/test/java`, runs them, writes reports to `target/surefire-reports/`, and **fails the build if a test fails**.

**Project:** `Q50_SurefireUnitTests/` – `Calculator.java`, `CalculatorTest.java` (fast tests incl. a parameterized test) and `SlowCalculatorTest.java` (tagged `slow`).
```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
            <configuration>
                <includes>
                    <include>**/Test*.java</include>
                    <include>**/*Test.java</include>
                    <include>**/*Tests.java</include>
                    <include>**/*TestCase.java</include>
                </includes>
                <excludes>
                    <exclude>**/*IT.java</exclude>          <!-- integration tests -> failsafe -->
                </excludes>
                <excludedGroups>${excluded.test.groups}</excludedGroups>   <!-- "slow" by default -->
                <systemPropertyVariables>
                    <app.environment>test</app.environment>
                    <discount.rate>0.10</discount.rate>
                </systemPropertyVariables>
                <forkCount>1</forkCount>
                <reuseForks>true</reuseForks>
                <argLine>-Xmx256m -Dfile.encoding=UTF-8</argLine>
                <properties>
                    <configurationParameters>
                        junit.jupiter.execution.parallel.enabled = true
                        junit.jupiter.execution.parallel.mode.default = concurrent
                    </configurationParameters>
                </properties>
                <testFailureIgnore>false</testFailureIgnore>
                <trimStackTrace>true</trimStackTrace>
            </configuration>
        </plugin>
    </plugins>
</build>

<profiles>
    <profile>
        <id>all-tests</id>                    <!-- mvn test -Pall-tests -->
        <properties>
            <excluded.test.groups></excluded.test.groups>
        </properties>
    </profile>
</profiles>
```

**Expected output of `mvn test`:**
```
[INFO] --- surefire:3.2.5:test (default-test) @ q50-surefire-unit-tests ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.javaassign5.q50.CalculatorTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Results:
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
(8 = 5 normal tests + 3 cases of the parameterized test. `SlowCalculatorTest` is excluded.)
With `mvn test -Pall-tests` it becomes `Tests run: 9`.

**Handy command-line options:**
| Command | Effect |
|---|---|
| `mvn test -Dtest=CalculatorTest` | Run one class |
| `mvn test -Dtest=CalculatorTest#divideByZeroThrows` | Run one method |
| `mvn test -Dgroups=slow -Dexcluded.test.groups=` | Run only tests tagged `slow` (the exclusion must be cleared too) |
| `mvn package -DskipTests` | Build without running tests |
| `mvn test -Dmaven.test.failure.ignore=true` | Don't fail the build on test failures |
