DELETE FROM venta_detalles;
DELETE FROM ventas;
DELETE FROM pedido_detalles;
DELETE FROM pedidos;
DELETE FROM inventario_movimientos;
DELETE FROM productos;
DELETE FROM clientes;

INSERT INTO clientes (id, nombre, correo, telefono) VALUES
    (1, 'Construcciones Rivera', 'contacto@crivera.com', '555-0101'),
    (2, 'Ferretería Central', 'ventas@ferreteria-central.mx', '555-0102');

INSERT INTO productos (id, nombre, descripcion, precio, stock) VALUES
    (1, 'Taladro Industrial', 'Taladro de impacto de 1/2"', 2850.00, 25),
    (2, 'Caja de Tornillos 1/4"', 'Caja con 500 pzs galvanizadas', 320.00, 150),
    (3, 'Guantes de seguridad', 'Guantes anticorte nivel 5', 95.00, 80);

INSERT INTO inventario_movimientos (producto_id, tipo, cantidad, referencia, fecha) VALUES
    (1, 'ENTRADA', 25, 'Carga inicial', CURRENT_TIMESTAMP()),
    (2, 'ENTRADA', 150, 'Carga inicial', CURRENT_TIMESTAMP()),
    (3, 'ENTRADA', 80, 'Carga inicial', CURRENT_TIMESTAMP());
