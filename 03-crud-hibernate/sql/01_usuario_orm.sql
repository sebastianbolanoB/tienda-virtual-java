-- GA7-220501096-AA3-EV01 · Usuario de base de datos para el módulo CRUD con Hibernate
-- Se ejecuta con root sobre la base tienda_virtual (GA6-220501096-AA2-EV03).
-- Permisos mínimos: leer categorías y marcas, y hacer el CRUD completo de productos.
DROP USER IF EXISTS 'tienda_orm'@'localhost';
CREATE USER 'tienda_orm'@'localhost' IDENTIFIED BY 'TiendaOrm3336103*';
GRANT SELECT ON tienda_virtual.categoria TO 'tienda_orm'@'localhost';
GRANT SELECT ON tienda_virtual.marca     TO 'tienda_orm'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON tienda_virtual.producto TO 'tienda_orm'@'localhost';
SHOW GRANTS FOR 'tienda_orm'@'localhost';
