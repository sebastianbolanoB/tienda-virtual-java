#!/usr/bin/env bash
# Compila y ejecuta el módulo CRUD con Hibernate. Requiere Java 21 y MySQL con la base tienda_virtual.
set -e
cd "$(dirname "$0")"
SEP=":"; case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac
CP="bin${SEP}lib/*"
rm -rf bin && mkdir -p bin
javac -encoding UTF-8 -Xlint:all -d bin -cp "lib/*" $(find src -name "*.java")
cp src/hibernate.cfg.xml src/logging.properties bin/
java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Djava.util.logging.config.file=bin/logging.properties -cp "$CP" co.edu.sena.adso.orm.App
