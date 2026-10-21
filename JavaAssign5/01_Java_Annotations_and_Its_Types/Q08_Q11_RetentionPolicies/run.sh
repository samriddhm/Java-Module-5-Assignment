#!/usr/bin/env bash
# Q8/Q11 demo: runtime check + bytecode check of the three retention policies.
cd "$(dirname "$0")"
rm -f *.class
javac *.java

echo "===== 1) Reflection (runtime) view ====="
java RetentionDemo

echo
echo "===== 2) Bytecode view: javap -v AnnotatedService.class ====="
echo "(SourceLevel does not appear at all; ClassLevel is 'Invisible'; RuntimeLevel is 'Visible')"
javap -v AnnotatedService.class | grep -A2 -E "^Runtime(In)?[Vv]isibleAnnotations"
rm -f *.class
