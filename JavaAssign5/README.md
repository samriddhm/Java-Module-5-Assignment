# JavaAssign5 — Module 5 Assignment

**Topics:** Java Annotations · Custom Annotations · Maven · Repositories, Dependencies & Plugins · Practical Exercises
**Student:** Shreyas Murgai

Every question from the assignment document is answered. The folders follow the headings of the document:

| Folder | Heading in the assignment | Questions | Answers file |
|---|---|---|---|
| [`01_Java_Annotations_and_Its_Types`](01_Java_Annotations_and_Its_Types) | Java Annotations and Its Types | Q1–Q20 | [`Answers_Q01-Q20.md`](01_Java_Annotations_and_Its_Types/Answers_Q01-Q20.md) |
| [`02_Creating_Custom_Annotations`](02_Creating_Custom_Annotations) | Creating Custom Annotations | Q21–Q30 | [`Answers_Q21-Q30.md`](02_Creating_Custom_Annotations/Answers_Q21-Q30.md) |
| [`03_Maven_Framework`](03_Maven_Framework) | Maven Framework | Q31–Q40 | [`Answers_Q31-Q40.md`](03_Maven_Framework/Answers_Q31-Q40.md) |
| [`04_Repositories_Dependencies_and_Plugins`](04_Repositories_Dependencies_and_Plugins) | Repositories, Dependencies, and Plugins | Q41–Q50 | [`Answers_Q41-Q50.md`](04_Repositories_Dependencies_and_Plugins/Answers_Q41-Q50.md) |
| [`05_Practical_Exercises`](05_Practical_Exercises) | Practical Exercise Suggestions | 5 exercises | [`Practical_Exercises_Guide.md`](05_Practical_Exercises/Practical_Exercises_Guide.md) |

**How it is organised**
- Each section has **one Markdown answers file** that answers *every* question in that section (theory answers, code snippets and the real program output).
- Questions that need code have their **own folder named after the question number and topic** (e.g. `Q17_StringLengthValidation/`). Each one is a runnable demo.
- Sections 01–02 are plain Java (compile with `javac`). Sections 03–05 are Maven projects (each has a `pom.xml`).

---

## Question → file index

### 01 – Java Annotations and Its Types
| Q | Topic | Where |
|---|---|---|
| 1–3, 5–7, 10, 13–14 | Theory | `Answers_Q01-Q20.md` |
| 4 | `@SuppressWarnings` example | `Q04_SuppressWarnings/` (`bash run.sh` – shows warnings before/after) |
| 8, 11 | Retention policies; RUNTIME vs CLASS | `Q08_Q11_RetentionPolicies/` (`bash run.sh` – reflection + `javap`) |
| 9 | Creating a custom annotation | `Q09_CreateCustomAnnotation/` |
| 12 | Processing annotations with reflection | `Q12_ReflectionProcessing/` |
| 15 | Annotation inheritance (`@Inherited`) | `Q15_AnnotationInheritance/` |
| 16 | Default values | `Q16_DefaultValues/` |
| 17 | String length validation annotation | `Q17_StringLengthValidation/` |
| 18 | Repeatable annotations | `Q18_RepeatableAnnotations/` |
| 19 | `@Documented` | `Q19_DocumentedMetaAnnotation/` (`bash run.sh` – generates javadoc) |
| 20 | Compile-time annotation processing | `Q20_CompileTimeProcessing/` (`bash run.sh` – a real annotation processor) |

### 02 – Creating Custom Annotations
| Q | Topic | Where |
|---|---|---|
| 21 | `@MinLength` | `Q21_MinLengthAnnotation/` |
| 22 | Author & version annotation | `Q22_AuthorVersionAnnotation/` |
| 23 | Processing via the `Annotation` interface | `Q23_AnnotationInterfaceProcessing/` |
| 24 | Restrictions on annotation methods | `Answers_Q21-Q30.md` (with the exact compiler errors) |
| 25 | Array parameters | `Q25_ArrayParameters/` |
| 26 | `@JsonField` for JSON serialization | `Q26_JsonFieldAnnotation/` |
| 27 | Can annotations extend others? | `Q27_AnnotationCannotExtend/` |
| 28 | Custom annotations + reflection program | `Q28_ReflectionDemo/` (a mini unit-test framework) |
| 29 | Logging method execution time | `Q29_LogExecutionTime/` (dynamic proxy) |
| 30 | Missing `@Retention` | `Q30_MissingRetention/` |

### 03 – Maven Framework
| Q | Topic | Where |
|---|---|---|
| 31–33, 35–37, 39 | Theory | `Answers_Q31-Q40.md` |
| 34 | Adding a dependency | `Q34_AddDependency/` |
| 38 | Configuring the Java version | `Q38_JavaVersion/` |
| 40 | Spring Boot `pom.xml` | `Q40_SpringBootPom/` (full runnable Spring Boot app) |

### 04 – Repositories, Dependencies, and Plugins
| Q | Topic | Where |
|---|---|---|
| 41 | Version conflict resolution | `Q41_ConflictResolution/` |
| 42, 44, 48 | Theory | `Answers_Q41-Q50.md` |
| 43 | Excluding a transitive dependency | `Q43_ExcludeTransitive/` |
| 45 | Creating a JAR | `Q45_CreateJar/` (thin + fat jar) |
| 46 | Including & configuring plugins | `Q46_PluginConfiguration/` |
| 47 | Multi-module project | `Q47_MultiModuleProject/` |
| 49 | Deploying to a remote repository | `Q49_DeployToRemoteRepo/` (+ example `settings.xml`) |
| 50 | Surefire unit-test configuration | `Q50_SurefireUnitTests/` |

### 05 – Practical Exercise Suggestions
| Exercise | Where |
|---|---|
| Maven project using annotations to validate user input | `Exercise1_AnnotationInputValidation/` |
| Maven project demonstrating transitive dependencies | `Exercise2_TransitiveDependencies/` |
| Core Java app with custom annotations + Maven dependencies | `Exercise3_CustomAnnotationsCoreApp/` |
| Web application built with maven-war-plugin | `Exercise4_WarWebApplication/` |
| Multi-module project with shared dependencies in the parent pom | `Exercise5_MultiModuleProject/` |

---

## How to run

**Requirements:** JDK 17+ (sections 01–02 also work on JDK 11) and Maven 3.6.3+ for sections 03–05. Check with `java -version` and `mvn -v`.

**Sections 01–02 (plain Java):**
```bash
# run ALL demos at once
bash run_all_java_demos.sh

# or one demo
cd 01_Java_Annotations_and_Its_Types/Q17_StringLengthValidation
javac *.java && java LengthValidationDemo
```
Folders with a `run.sh` (Q04, Q08/Q11, Q19, Q20) need extra steps such as `javap`, `javadoc` or an annotation processor – run them with `bash run.sh`.

**Sections 03–05 (Maven):** open a terminal inside the project folder, e.g.
```bash
cd 05_Practical_Exercises/Exercise3_CustomAnnotationsCoreApp
mvn test                    # run the unit tests
mvn -q compile exec:java    # run the program
mvn package                 # build the jar / war
```
The exact command for every project is in the table at the top of each answers file.
