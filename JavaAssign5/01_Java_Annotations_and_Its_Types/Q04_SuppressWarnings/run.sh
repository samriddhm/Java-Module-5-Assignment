#!/usr/bin/env bash
# Q4 demo: shows the compiler warnings WITHOUT and WITH @SuppressWarnings.
cd "$(dirname "$0")"
rm -f *.class

echo "===== 1) Compiling WithWarnings.java (no @SuppressWarnings) ====="
javac -Xlint:all OldApi.java WithWarnings.java 2>&1

echo
echo "===== 2) Compiling SuppressWarningsDemo.java (with @SuppressWarnings) ====="
javac -Xlint:all OldApi.java SuppressWarningsDemo.java 2>&1 && echo "(no warnings)"

echo
echo "===== 3) Running SuppressWarningsDemo ====="
java SuppressWarningsDemo
rm -f *.class
