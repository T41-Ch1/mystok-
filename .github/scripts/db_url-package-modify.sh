#!/bin/sh
sed -i _backup -e s@localhost:3306@127.0.0.1@g -e s@pac1@mystok@g src/main/java/*.java
rm -f *.java_backup
