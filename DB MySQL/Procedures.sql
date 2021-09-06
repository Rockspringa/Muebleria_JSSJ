USE MUEBLERIA;

DELIMITER /

CREATE PROCEDURE ENSAMBLAR (IN mueble VARCHAR(40), IN usuario VARCHAR(40), IN fecha DATE)
BEGIN
	DECLARE output VARCHAR(25) DEFAULT 'success';
	IF (EXISTS(SELECT nombre FROM MUEBLE WHERE nombre = mueble))
    THEN
		IF (EXISTS(SELECT nombre_usuario FROM USUARIO WHERE usuario = nombre_usuario))
        THEN
			CREATE TEMPORARY TABLE TEMP AS
				SELECT tipo, mp.cantidad AS existencias, costo, i.cantidad AS necesitados
					FROM MATERIA_PRIMA mp
					INNER JOIN INDICACIONES i
						ON tipo = tipo_pieza AND costo = costo_pieza
					WHERE i.mueble = mueble;
			SELECT COUNT(*) INTO @falses FROM TEMP WHERE existencias < necesitados;
			SELECT COUNT(*) INTO @filas FROM TEMP;
			IF @falses = 0 AND @filas > 0
			THEN
				UPDATE MATERIA_PRIMA mp
					INNER JOIN TEMP t ON mp.tipo = t.tipo
					SET mp.cantidad = mp.cantidad - t.necesitados;
				INSERT INTO ENSAMBLADO (fecha_ensamble, ensamblador, tipo, coste)
					SELECT fecha, usuario, mueble, SUM(necesitados * costo) FROM TEMP;
			DROP TABLE TEMP;
			ELSE SET output = 'No hay piezas suficientes';
			END IF;
		ELSE SET output = 'No existe el usuario';
        END IF;
	ELSE SET output = 'El mueble no existe';
	END IF;
    SELECT output;
END;
/

CREATE PROCEDURE ENSAMBLAR_NOW (IN mueble VARCHAR(40), IN usuario VARCHAR(40))
BEGIN
	CALL ENSAMBLAR(mueble, usuario, CURDATE());
END;
/

CREATE PROCEDURE IS_USUARIO (IN usuario VARCHAR(40), IN contra VARCHAR(40))
BEGIN
	IF (EXISTS(SELECT usuario, contraseña FROM USUARIO WHERE nombre_usuario LIKE usuario AND contraseña LIKE BINARY contra))
	THEN
		SELECT tipo INTO @t FROM USUARIO WHERE nombre_usuario LIKE usuario;
		SELECT activo INTO @a FROM USUARIO WHERE nombre_usuario LIKE usuario;
		SELECT CONCAT("  ", @t, IF(@a = 1, "true", "false"));
	ELSE
		SELECT "0";
	END IF;
END;
/

CREATE PROCEDURE ADD_INDICACION (IN n_mueble VARCHAR(40), IN pieza VARCHAR(40), IN cant INT)
BEGIN
	DECLARE output VARCHAR(17) DEFAULT 'success';
	IF (EXISTS(SELECT nombre FROM MUEBLE WHERE nombre = n_mueble))
    THEN
		IF (EXISTS(SELECT tipo FROM MATERIA_PRIMA WHERE tipo = pieza))
        THEN
			SELECT COUNT(*) INTO @tipos FROM MATERIA_PRIMA WHERE tipo = pieza;
			IF @tipos = 1
			THEN
				SELECT costo INTO @coste FROM MATERIA_PRIMA WHERE tipo = pieza;
				INSERT INTO INDICACIONES (mueble, tipo_pieza, costo_pieza, cantidad)
					VALUES (n_mueble, pieza, @coste, cant);
			ELSE
				SET @num := 0;
				CREATE TEMPORARY TABLE TEMP AS
					SELECT costo, 0 AS canti, @num := @num + 1 AS num
						FROM MATERIA_PRIMA WHERE tipo = pieza ORDER BY costo;
                        
				SET @i := 0;
				WHILE (@i < cant)
                DO
					SET @i := @i + 1;
                    UPDATE TEMP SET canti = canti + 1 WHERE num = FLOOR(RAND()*(cantidad)) + 1;
                END WHILE;
                
				INSERT INTO INDICACIONES (mueble, tipo_pieza, costo_pieza, cantidad)
					SELECT n_mueble, pieza, costo, canti FROM TEMP;
                    
				DROP TABLE TEMP;
			END IF;
		ELSE SET output = 'No existe la pieza';
        END IF;
	ELSE SET output = 'No existe el mueble';
	END IF;
    SELECT output;
END;
/

CREATE PROCEDURE ADD_INDICACION_2 (IN n_mueble VARCHAR(40), IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2), IN cant INT)
BEGIN
	DECLARE output VARCHAR(17) DEFAULT 'success';
	IF (EXISTS(SELECT nombre FROM MUEBLE WHERE nombre = n_mueble))
    THEN
		IF (EXISTS(SELECT tipo FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p))
        THEN
			INSERT INTO INDICACIONES (mueble, tipo_pieza, costo_pieza, cantidad)
				VALUES (n_mueble, pieza, costo_p, cant);
		ELSE SET output = 'No existe la pieza';
        END IF;
	ELSE SET output = 'No existe el mueble';
	END IF;
    SELECT output;
END;
/

CREATE PROCEDURE EXISTS_INDICACION (IN n_mueble VARCHAR(40), IN pieza VARCHAR(40), IN cant INT)
BEGIN
	SET @output = 'false';
	IF (EXISTS(SELECT nombre FROM MUEBLE WHERE nombre = n_mueble))
    THEN
		IF (EXISTS(SELECT tipo FROM MATERIA_PRIMA WHERE tipo = pieza))
        THEN
			IF (EXISTS(SELECT * FROM INDICACIONES WHERE mueble = n_mueble AND tipo_pieza = pieza))
			THEN
				SET @output = 'true';
			END IF;
		ELSE SET @output = 'falta la pieza';
        END IF;
	ELSE SET @output = 'falta el mueble';
	END IF;
    SELECT @output;
END;
/

CREATE PROCEDURE GET_PIEZAS_IND (IN n_mueble VARCHAR(40))
BEGIN
    IF (EXISTS(SELECT nombre FROM MUEBLE WHERE nombre = n_mueble))
    THEN
		SELECT CONCAT(tipo_pieza, ' de Q', costo_pieza),
				CONCAT(IF(mp.cantidad < i.cantidad, mp.cantidad, i.cantidad), '/', i.cantidad),
                IF(mp.cantidad < i.cantidad, 0, IF(mp.cantidad < 10, 1, 2))
			FROM INDICACIONES i INNER JOIN MATERIA_PRIMA mp ON tipo_pieza = tipo AND costo = costo_pieza
			WHERE mueble = n_mueble;
	ELSE SELECT "falta el mueble";
	END IF;
END;
/

CREATE PROCEDURE GET_PIEZAS_IND_TABLA (IN n_mueble VARCHAR(40))
BEGIN
    IF (EXISTS(SELECT nombre FROM MUEBLE WHERE nombre = n_mueble))
    THEN
		SELECT IF(mp.cantidad < i.cantidad, 0, IF(mp.cantidad < 10, 1, 2)), tipo_pieza, CONCAT("Q.", costo_pieza), CONCAT(i.cantidad, " unidades")
			FROM INDICACIONES i INNER JOIN MATERIA_PRIMA mp ON tipo_pieza = tipo AND costo = costo_pieza
			WHERE mueble = n_mueble;
	ELSE SELECT "falta el mueble";
	END IF;
END;
/

CREATE PROCEDURE GET_PRECIOS_MUEBLE (IN n_mueble VARCHAR(40))
BEGIN
    IF (EXISTS(SELECT nombre FROM MUEBLE WHERE nombre = n_mueble))
    THEN
		SELECT CONCAT("costo: Q", SUM(costo_pieza * cantidad)), CONCAT("precio: Q", precio) FROM MUEBLE
			INNER JOIN INDICACIONES ON mueble = nombre
				WHERE mueble = n_mueble GROUP BY nombre;
	ELSE SELECT "falta el mueble";
	END IF;
END;
/

CREATE PROCEDURE GET_PIEZAS ()
BEGIN
	SET @num = 0;
	SELECT IF(cantidad < 4, 0, IF(cantidad < 13, 1, 2)), @num := @num + 1 AS ' ',
			tipo, costo, cantidad, IF(cantidad = 0, "Agotado", IF(cantidad < 13, "Agotandose", "Suficiente"))
		FROM MATERIA_PRIMA ORDER BY costo;
END;
/

CREATE PROCEDURE GET_CREADOS ()
BEGIN
	SET @num = 0;
	SELECT @num := @num + 1 AS ' ', tipo, ensamblador, fecha_ensamble, coste, precio
		FROM ENSAMBLADO INNER JOIN MUEBLE ON tipo = nombre ORDER BY fecha_ensamble;
END;
/

CREATE PROCEDURE GET_PIEZAS_AGOTADAS ()
BEGIN
	SET @num = 0;
	SELECT @num := @num + 1 AS ' ', tipo, costo, cantidad FROM MATERIA_PRIMA
		WHERE cantidad < 10 ORDER BY cantidad;
END;
/

CREATE PROCEDURE GET_MUEBLES ()
BEGIN
	SELECT nombre, IF(i.cantidad > mp.cantidad, "btn-danger", "b-box") FROM MUEBLE
		INNER JOIN INDICACIONES i ON nombre = mueble
		INNER JOIN MATERIA_PRIMA mp ON tipo_pieza = tipo AND costo_pieza = costo
			ORDER BY CAST(mp.cantidad AS SIGNED) - CAST(i.cantidad AS SIGNED) DESC;
END;
/

CREATE PROCEDURE ADD_PIEZAS (IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2), IN cantidad_p INT)
BEGIN
	IF (EXISTS(SELECT tipo FROM MATERIA_PRIMA WHERE tipo = pieza))
    THEN
		IF (EXISTS(SELECT tipo, costo FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p))
		THEN
			UPDATE MATERIA_PRIMA SET cantidad = cantidad + cantidad_p WHERE tipo = pieza AND costo = costo_p;
            SELECT CONCAT('Completado, ahora hay ', cantidad, ' unidades de ', tipo) FROM MATERIA_PRIMA
				WHERE tipo = pieza AND costo = costo_p;
		ELSE 
            SELECT 'No se encontro un costo coincidente.';
		END IF;
	ELSE
		SELECT 'No se encontro un tipo coincidente.';
	END IF;
END;
/

CREATE PROCEDURE CREAR_PIEZA (IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2), IN cantidad_p INT)
BEGIN
	IF (EXISTS(SELECT tipo, costo FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p))
	THEN
		SELECT 'El tipo de pieza, con el mismo coste ya existe, el formulario de añadir o el de modificar le pueden ayudar.';
	ELSE
		INSERT INTO MATERIA_PRIMA VALUES (pieza, costo_p, cantidad_p);
		SELECT CONCAT('Completado, creado el tipo "', tipo, '" con coste de Q.', costo, ' y con existencias igual a ', cantidad)
			FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p;
	END IF;
END;
/

CREATE PROCEDURE UPDATE_PIEZA (IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2), IN pieza_n VARCHAR(40), IN costo_n DECIMAL(5, 2), IN cantidad_n INT)
BEGIN
	IF (pieza = pieza_n AND costo_p = costo_n)
	THEN
		SELECT cantidad INTO @cant FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p;
		UPDATE MATERIA_PRIMA SET cantidad = cantidad_n WHERE tipo = pieza_n AND costo = costo_n;
        
		SELECT CONCAT('Completado, se actualizo el tipo "', tipo, '" con coste de Q.', costo, ' y con existencias igual a ', @cant,
				', ahora tiene ', cantidad, ' unidades.') FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p;
                
	ELSEIF (EXISTS(SELECT tipo, costo FROM MATERIA_PRIMA WHERE tipo = pieza_n AND costo = costo_n))
    THEN
		SELECT 'El tipo y el coste nuevos ya existen, no se puede modificar la pieza solicitada.';
        
	ELSE
		SELECT cantidad INTO @cant FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p;
		UPDATE MATERIA_PRIMA SET tipo = pieza_n, costo = costo_n, cantidad = cantidad_n WHERE tipo = pieza AND costo = costo_p;
        
		SELECT CONCAT('Completado, se actualizo el tipo "', pieza, '" con coste de Q.', costo_p,
			' y con existencias igual a ', @cant, ', ahora es el tipo "', pieza_n, '" con coste de Q.',
			costo_n, ' y con existencias igual a ', cantidad_n);
	END IF;
END;
/

CREATE PROCEDURE DEL_PIEZA (IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2))
BEGIN
	IF (EXISTS(SELECT tipo, costo FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p))
	THEN
		DELETE FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p;
        
		SELECT CONCAT('Completado, se elimino con exito el tipo "', pieza, '" con coste de Q.', costo_p);
	ELSE
		SELECT 'No existe ninguna coincidencia con la pieza y el costo.';
	END IF;
END;
/

CREATE PROCEDURE ADD_PIEZA (IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2))
BEGIN
	IF (EXISTS(SELECT tipo, costo FROM MATERIA_PRIMA WHERE tipo = pieza AND costo = costo_p))
    THEN
		CALL ADD_PIEZAS (pieza, costo_p, 1);
	ELSE
		CALL CREAR_PIEZA (pieza, costo_p, 1);
	END IF;
END;
/

CREATE PROCEDURE REPORTE_VENTAS (IN inicio DATE, IN final DATE)
BEGIN
	SELECT f.num_factura, c.nombre, e.tipo, fo.precio, f.fecha_compra FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN CLIENTE c ON cliente = nit
				INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final;
END;
/

CREATE PROCEDURE REPORTE_DEVOLUCIONES (IN inicio DATE, IN final DATE)
BEGIN
	SELECT f.num_factura, c.nombre, e.tipo, fo.precio, f.fecha_compra, fo.fecha_devolucion, e.coste DIV 3 FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN CLIENTE c ON cliente = nit
				INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 1 AND fecha_devolucion BETWEEN inicio AND final;
END;
/

CREATE PROCEDURE REPORTE_GANANCIAS_TOTALES (IN inicio DATE, IN final DATE)
BEGIN
	SELECT fo.precio - e.coste FROM FACTURADO fo
		INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final;
END;
/

CREATE PROCEDURE REPORTE_GANANCIAS (IN inicio DATE, IN final DATE)
BEGIN
	SELECT e.tipo, e.coste, fo.precio, fo.precio - e.coste FROM FACTURADO fo
		INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final;
END;
/

CREATE PROCEDURE REPORTE_USUARIO_VENTAS (IN inicio DATE, IN final DATE)
BEGIN
	SELECT f.vendedor, COUNT(*) AS 'Cantidad Ventas' FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
        GROUP BY vendedor ORDER BY 'Cantidad Ventas' DESC LIMIT 1;
END;
/

CREATE PROCEDURE REPORTE_USUARIO_VENTAS_TABLA (IN inicio DATE, IN final DATE)
BEGIN
	CREATE TEMPORARY TABLE TEMP AS
		SELECT f.vendedor AS usuario, COUNT(*) AS 'Cantidad Ventas' FROM FACTURADO fo
			INNER JOIN FACTURA f ON factura = num_factura
				INNER JOIN ENSAMBLADO e ON mueble = mueble_id
			WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
			GROUP BY vendedor ORDER BY 'Cantidad Ventas' DESC LIMIT 1;
            
	SELECT usuario INTO @usuario FROM TEMP;
                    
	SELECT e.tipo, fo.precio FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND vendedor = @usuario AND fecha_compra BETWEEN inicio AND final;
        
	DROP TABLE TEMP;
END;
/

CREATE PROCEDURE REPORTE_USUARIO_GANANCIAS (IN inicio DATE, IN final DATE)
BEGIN
	SELECT f.vendedor, SUM(fo.precio - e.coste) AS Ganancias FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
        GROUP BY vendedor ORDER BY Ganancias DESC LIMIT 1;
END;
/

CREATE PROCEDURE REPORTE_USUARIO_GANANCIAS_TABLA (IN inicio DATE, IN final DATE)
BEGIN
	CREATE TEMPORARY TABLE TEMP AS
		SELECT f.vendedor AS usuario, SUM(fo.precio - e.coste) AS Ganancias FROM FACTURADO fo
			INNER JOIN FACTURA f ON factura = num_factura
				INNER JOIN ENSAMBLADO e ON mueble = mueble_id
			WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
			GROUP BY vendedor ORDER BY Ganancias DESC LIMIT 1;
            
	SELECT usuario INTO @usuario FROM TEMP;
                    
	SELECT e.tipo, fo.precio, e.coste, fo.precio - e.coste FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND vendedor = @usuario AND fecha_compra BETWEEN inicio AND final;
        
	DROP TABLE TEMP;
END;
/

CREATE PROCEDURE REPORTE_MUEBLE_MAS (IN inicio DATE, IN final DATE)
BEGIN
	SELECT e.tipo, COUNT(*) AS Vendidos FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
        GROUP BY tipo ORDER BY Vendidos DESC LIMIT 1;
END;
/

CREATE PROCEDURE REPORTE_MUEBLE_MAS_TABLA (IN inicio DATE, IN final DATE)
BEGIN
	CREATE TEMPORARY TABLE TEMP AS
		SELECT e.tipo, COUNT(*) AS Vendidos FROM FACTURADO fo
			INNER JOIN FACTURA f ON factura = num_factura
				INNER JOIN ENSAMBLADO e ON mueble = mueble_id
			WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
			GROUP BY tipo ORDER BY Vendidos DESC LIMIT 1;
            
	SELECT tipo INTO @mueble FROM TEMP;
                    
	SELECT fo.precio, e.coste, f.fecha_compra FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND tipo = @mueble AND fecha_compra BETWEEN inicio AND final;
        
	DROP TABLE TEMP;
END;
/

CREATE PROCEDURE REPORTE_MUEBLE_MENOS (IN inicio DATE, IN final DATE)
BEGIN
	SELECT e.tipo, COUNT(*) AS Vendidos FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
        GROUP BY tipo ORDER BY Vendidos LIMIT 1;
END;
/

CREATE PROCEDURE REPORTE_MUEBLE_MENOS_TABLA (IN inicio DATE, IN final DATE)
BEGIN
	CREATE TEMPORARY TABLE TEMP AS
		SELECT e.tipo, COUNT(*) AS Vendidos FROM FACTURADO fo
			INNER JOIN FACTURA f ON factura = num_factura
				INNER JOIN ENSAMBLADO e ON mueble = mueble_id
			WHERE devuelto = 0 AND fecha_compra BETWEEN inicio AND final
			GROUP BY tipo ORDER BY Vendidos LIMIT 1;
            
	SELECT tipo INTO @mueble FROM TEMP;
                    
	SELECT fo.precio, e.coste, f.fecha_compra FROM FACTURADO fo
		INNER JOIN FACTURA f ON factura = num_factura
			INNER JOIN ENSAMBLADO e ON mueble = mueble_id
		WHERE devuelto = 0 AND tipo = @mueble AND fecha_compra BETWEEN inicio AND final;
        
	DROP TABLE TEMP;
END;
/

CREATE PROCEDURE GET_TIPOS_MUEBLE ()
BEGIN
	SELECT nombre FROM MUEBLE INNER JOIN ENSAMBLADO ON tipo = nombre
		LEFT JOIN FACTURADO ON mueble = mueble_id
        WHERE mueble IS NULL GROUP BY nombre;
END;

-- INSERT INTO MUEBLE VALUES ("Mesa Rustica", 150.00);
-- CALL CREAR_PIEZA ("Pata Cuadrada", 15.50, 4);
-- CALL ADD_INDICACION ("Mesa Rustica", "Pata Cuadrada", 2)
-- CALL GET_MUEBLES ()
-- SELECT * FROM MUEBLE;
-- SELECT * FROM MATERIA_PRIMA;
-- SELECT * FROM INDICACIONES;
