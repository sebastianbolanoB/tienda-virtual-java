#!/usr/bin/env bash
# Compila el módulo web y genera build/tienda.war para desplegarlo en Apache Tomcat 11.
# Uso: ./construir.sh [desplegar]
set -e
cd "$(dirname "$0")"
TOMCAT="${CATALINA_HOME:-/c/Users/thebo/Tools/apache-tomcat-11.0.29}"

rm -rf build
mkdir -p build/tienda/WEB-INF/classes
cp -r web/. build/tienda/
javac -encoding UTF-8 -Xlint:all -d build/tienda/WEB-INF/classes \
      -cp "$TOMCAT/lib/servlet-api.jar" $(find src -name '*.java')
cp config/db.properties build/tienda/WEB-INF/classes/
(cd build/tienda && jar --create --file ../tienda.war .)
echo "WAR generado: build/tienda.war ($(du -h build/tienda.war | cut -f1))"

if [ "$1" = "desplegar" ]; then
    cp build/tienda.war "$TOMCAT/webapps/"
    echo "Copiado a $TOMCAT/webapps/tienda.war"
fi
