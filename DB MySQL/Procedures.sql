USE MUEBLERIA;

DELIMITER //

CREATE PROCEDURE ENSAMBLAR (IN mueble VARCHAR(40), IN usuario VARCHAR(40), IN fecha DATE)
BEGIN
	DECLARE output VARCHAR(17) DEFAULT 'success';
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
			ELSE SET output = 'faltan piezas';
			END IF;
		ELSE SET output = 'falta el usuario';
        END IF;
	ELSE SET output = 'falta el mueble';
	END IF;
    SELECT output;
END;

DELIMITER //

CREATE PROCEDURE ENSAMBLAR_NOW (IN mueble VARCHAR(40), IN usuario VARCHAR(40))
BEGIN
	CALL ENSAMBLAR(mueble, usuario, CURDATE());
END;

DELIMITER //

CREATE PROCEDURE IS_USUARIO (IN usuario VARCHAR(40), IN contra VARCHAR(40))
BEGIN
	IF (EXISTS(SELECT usuario, contraseña FROM USUARIO WHERE nombre_usuario LIKE usuario AND contraseña LIKE BINARY contra))
	THEN
		SELECT tipo INTO @t FROM USUARIO WHERE nombre_usuario LIKE BINARY usuario;
		SELECT CONCAT("  ", @t);
	ELSE
		SELECT "0";
	END IF;
END;

DELIMITER //

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
		ELSE SET output = 'falta la pieza';
        END IF;
	ELSE SET output = 'falta el mueble';
	END IF;
    SELECT output;
END;

DELIMITER //

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

DELIMITER //

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

DELIMITER //

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

DELIMITER //

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

DELIMITER //

CREATE PROCEDURE GET_PIEZAS ()
BEGIN
	SET @num = 0;
	SELECT IF(cantidad < 4, 0, IF(cantidad < 13, 1, 2)), @num := @num + 1 AS ' ',
			tipo, costo, cantidad, IF(cantidad = 0, "Agotado", IF(cantidad < 13, "Agotandose", "Suficiente"))
		FROM MATERIA_PRIMA ORDER BY costo;
END;

DELIMITER //

CREATE PROCEDURE GET_CREADOS ()
BEGIN
	SET @num = 0;
	SELECT @num := @num + 1 AS ' ', tipo, ensamblador, fecha_ensamble, coste, precio
		FROM MATERIA_PRIMA INNER JOIN MUEBLE ON tipo = nombre ORDER BY fecha_ensamble;
END;

DELIMITER //

CREATE PROCEDURE GET_PIEZAS_AGOTADAS ()
BEGIN
	SET @num = 0;
	SELECT @num := @num + 1 AS ' ', tipo, costo, cantidad FROM MATERIA_PRIMA
		WHERE cantidad < 10 ORDER BY cantidad;
END;

DELIMITER //

CREATE PROCEDURE GET_MUEBLES ()
BEGIN
	SELECT nombre, IF(i.cantidad > mp.cantidad, "btn-danger", "b-box") FROM MUEBLE
		INNER JOIN INDICACIONES i ON nombre = mueble
		INNER JOIN MATERIA_PRIMA mp ON tipo_pieza = tipo AND costo_pieza = costo
			ORDER BY CAST(mp.cantidad AS SIGNED) - CAST(i.cantidad AS SIGNED) DESC;
END;

DELIMITER //

CREATE PROCEDURE ADD_PIEZA (IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2), IN cantidad_p INT)
BEGIN
	IF (EXISTS(SELECT tipo FROM MATERIA_PRIMA WHERE tipo = pieza))
    THEN
		UPDATE MATERIA_PRIMA SET cantidad = cantidad + cantidad_p WHERE tipo = pieza;
	ELSE
		INSERT INTO MATERIA_PRIMA VALUES (pieza, costo_p, cantidad_p);
	END IF;
END;

DELIMITER //

CREATE PROCEDURE ADD_PIEZA_UNO (IN pieza VARCHAR(40), IN costo_p DECIMAL(5, 2))
BEGIN
	CALL ADD_PIEZA (pieza, costo_p, 1);
END;

-- INSERT INTO MUEBLE VALUES ("Mesa Rustica", 150.00)
-- ICALL ADD_PIEZA ("Pata Cuadrada", 15.50, 4);
-- ICALL ADD_PIEZA ("Pata Cuadrada", 15.50, -1);
-- ICALL ADD_INDICACION ("Mesa Rustica", "Pata Cuadrada", 2)
-- ICALL GET_MUEBLES ()
-- ISELECT * FROM MUEBLE;
-- ISELECT * FROM MATERIA_PRIMA;
-- ISELECT * FROM INDICACIONES;
