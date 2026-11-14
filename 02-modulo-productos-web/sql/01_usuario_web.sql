-- GA7-220501096-AA2-EV02 · Usuario de base de datos para el módulo web de productos
-- Se ejecuta con root después de los scripts de GA6-220501096-AA2-EV03
-- (01_tienda_virtual_modelo_fisico.sql y 02_datos_ejemplo.sql).
-- Principio de mínimo privilegio: la aplicación web solo puede leer categorías y marcas,
-- y leer, crear y modificar productos. No tiene DELETE: un producto se desactiva (activo = 0)
-- para no romper los pedidos que ya lo referencian.

DROP USER IF EXISTS 'tienda_web'@'localhost';
CREATE USER 'tienda_web'@'localhost' IDENTIFIED BY 'TiendaWeb3336103*';

GRANT SELECT ON tienda_virtual.categoria TO 'tienda_web'@'localhost';
GRANT SELECT ON tienda_virtual.marca     TO 'tienda_web'@'localhost';
GRANT SELECT, INSERT, UPDATE ON tienda_virtual.producto TO 'tienda_web'@'localhost';

SHOW GRANTS FOR 'tienda_web'@'localhost';
