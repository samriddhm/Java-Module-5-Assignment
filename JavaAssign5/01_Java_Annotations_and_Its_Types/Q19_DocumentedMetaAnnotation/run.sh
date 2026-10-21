#!/usr/bin/env bash
# Q19 demo: generate javadoc and check which annotations appear in the HTML.
cd "$(dirname "$0")"
rm -rf *.class docs

echo "===== 1) Runtime view ====="
javac *.java && java DocumentedDemo

echo
echo "===== 2) Javadoc view ====="
javadoc -quiet -d docs ApiInfo.java InternalNote.java PaymentGateway.java > /dev/null 2>&1
HTML=docs/PaymentGateway.html
echo "Occurrences of '@ApiInfo'      in $HTML: $(grep -o 'ApiInfo.html' "$HTML" | wc -l)  (shown - it is @Documented)"
echo "Occurrences of '@InternalNote' in $HTML: $(grep -o 'InternalNote.html' "$HTML" | wc -l)  (hidden - it is not @Documented)"
echo "Open docs/PaymentGateway.html in a browser to see it yourself."
rm -f *.class
