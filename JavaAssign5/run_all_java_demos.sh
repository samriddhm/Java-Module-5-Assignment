#!/usr/bin/env bash
# Runs every plain-Java demo in sections 01 and 02 (no Maven needed, JDK 11+).
# Usage:  bash run_all_java_demos.sh
# The Maven projects (sections 03-05) are run with the mvn commands listed in each Answers/Guide file.

ROOT="$(cd "$(dirname "$0")" && pwd)"

for dir in "$ROOT"/01_Java_Annotations_and_Its_Types/Q*/ "$ROOT"/02_Creating_Custom_Annotations/Q*/; do
    name="$(basename "$dir")"
    echo
    echo "################################################################"
    echo "  $name"
    echo "################################################################"
    if [ -f "$dir/run.sh" ]; then
        bash "$dir/run.sh"
    else
        (
            cd "$dir" || exit 1
            main="$(grep -l "static void main" *.java | head -1)"
            javac *.java && java "${main%.java}"
            rm -f *.class
        )
    fi
done
rm -rf "$ROOT"/01_Java_Annotations_and_Its_Types/Q19_DocumentedMetaAnnotation/docs \
       "$ROOT"/01_Java_Annotations_and_Its_Types/Q20_CompileTimeProcessing/out
