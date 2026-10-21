#!/usr/bin/env bash
# Q20 demo: an annotation processor running INSIDE the compiler.
cd "$(dirname "$0")"
rm -rf out

echo "===== STEP 1: compile the processor (and its annotations) ====="
javac -d out/processor processor/*.java && echo "processor compiled"

echo
echo "===== STEP 2: compile the app WITH the processor (watch the Notes) ====="
javac -cp out/processor -processor CompileTimeProcessor \
      -d out/app -s out/generated app/*.java

echo
echo "===== Generated source file: out/generated/GeneratedTodoList.java ====="
cat out/generated/GeneratedTodoList.java

echo
echo "===== STEP 3: run the app ====="
java -cp out/app AppMain

echo
echo "===== STEP 4: compile a class that breaks the @Immutable rule ====="
javac -cp out/processor -processor CompileTimeProcessor -d out/broken broken/MutablePoint.java \
  || echo ">>> Compilation FAILED because of the processor's error (this is expected)."
