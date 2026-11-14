# TiendaVirtual – módulos en Java

Proyecto formativo del programa Análisis y Desarrollo de Software (SENA, ficha 3336103).
Autor: Sebastián Bolaño Barraza.

| Carpeta | Evidencia | Contenido |
|---|---|---|
| `01-conexion-jdbc` | GA7-220501096-AA2-EV01 | Aplicación de consola que se conecta a MySQL con JDBC y hace el CRUD de la tabla `usuario`. |
| `02-modulo-productos-web` | GA7-220501096-AA2-EV02 | Módulo web de productos con servlets, JSP y JDBC (patrón MVC) sobre la base `tienda_virtual`. |

## Requisitos

- Java 21 (JDK)
- MySQL 8 o superior
- Apache Tomcat 11 (solo para el módulo web)
- MySQL Connector/J (incluido en `lib/` y `web/WEB-INF/lib/`)

## 01-conexion-jdbc

```bash
cd 01-conexion-jdbc
mysql -u root -p < sql/01_prueba.sql
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp "bin;lib/mysql-connector-j-26.10.0.jar" co.edu.sena.adso.jdbc.App
```

En Linux o macOS el separador del classpath es `:` en lugar de `;`.

## 02-modulo-productos-web

1. Crear la base `tienda_virtual` con los scripts del modelo físico del proyecto (GA6-220501096-AA2-EV03).
2. Ejecutar `sql/01_usuario_web.sql` con un usuario administrador de MySQL.
3. Ajustar `config/db.properties` (URL, usuario y clave) al servidor MySQL.
4. Compilar y desplegar:

```bash
cd 02-modulo-productos-web
CATALINA_HOME=/ruta/a/apache-tomcat-11 ./construir.sh desplegar
```

5. Iniciar Tomcat y abrir http://localhost:8080/tienda

Rutas: `/catalogo` (GET), `/producto?id=N` (GET) y `/admin/productos` (GET y POST).

## Nota sobre las credenciales

Los archivos `db.properties` y los scripts SQL tienen las claves de usuarios de prueba de un MySQL local
(solo aceptan conexiones desde `localhost`). En un servidor real se deben cambiar y no subir al repositorio.
