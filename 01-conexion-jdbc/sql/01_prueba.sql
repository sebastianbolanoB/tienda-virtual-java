-- Base de datos de práctica para la conexión JDBC (GA7-220501096-AA2-EV01)
-- Autor: Sebastián Bolaño Barraza - Ficha 3336103

DROP DATABASE IF EXISTS prueba;
CREATE DATABASE prueba CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE prueba;

CREATE TABLE usuario (
    user_id       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username      VARCHAR(30)  NOT NULL,
    user_password CHAR(64)     NOT NULL COMMENT 'Hash SHA-256 de la contraseña',
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_usuario_username (username)
) ENGINE = InnoDB;

-- Usuario de la aplicación con permisos solo sobre esta base (no se usa root)
DROP USER IF EXISTS 'adso_app'@'localhost';
CREATE USER 'adso_app'@'localhost' IDENTIFIED BY 'Adso3336103*';
GRANT SELECT, INSERT, UPDATE, DELETE ON prueba.* TO 'adso_app'@'localhost';
