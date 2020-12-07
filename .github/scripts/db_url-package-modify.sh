#!/bin/sh
sed -i _backup -e s@localhost:3306/j2a1b@127.0.0.1:3306/mystok@g -e s@pac1@mystok@g src/main/java/*.java
rm -f src/main/java/*.java_backup
sed -i _backup -e s@pac1@mystok@g src/main/java/*/*.java
rm -f src/main/java/*/*.jaca_backup
