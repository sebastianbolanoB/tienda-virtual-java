#!/usr/bin/env bash
# Descarga las librerías de librerias.txt desde Maven Central y verifica su código SHA-1.
set -e
cd "$(dirname "$0")"
mkdir -p lib
grep -v '^#' librerias.txt | while read -r ruta; do
    [ -z "$ruta" ] && continue
    version=${ruta##*/}; artefacto=$(basename "$(dirname "$ruta")"); jar="$artefacto-$version.jar"
    url="https://repo1.maven.org/maven2/$ruta/$jar"
    [ -f "lib/$jar" ] || curl -sSf -o "lib/$jar" "$url"
    esperado=$(curl -sSf "$url.sha1" | cut -c1-40)
    [ "$(sha1sum "lib/$jar" | cut -c1-40)" = "$esperado" ] && echo "OK  $jar" || { echo "SHA-1 incorrecto: $jar"; exit 1; }
done
