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
	SELECT ENSAMBLAR(mueble, usuario, CURDATE());
END;

DELIMITER //

CREATE PROCEDURE IS_USUARIO (IN usuario VARCHAR(40), IN contra VARCHAR(40))
BEGIN
	IF (EXISTS(SELECT usuario, contraseña FROM USUARIO WHERE nombre_usuario = usuario AND contraseña = contra))
	THEN
		SELECT tipo INTO @t FROM USUARIO WHERE nombre_usuario = usuario;
		SELECT CONCAT("1  ", @t);
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
		SELECT CONCAT(tipo_pieza, ' de Q', costo_pieza), CONCAT(mp.cantidad, '/', i.cantidad), IF(mp.cantidad < i.cantidad, ' ', 'r')
			FROM INDICACIONES INNER JOIN MATERIA_PRIMA ON tipo_pieza = pieza AND costo = costo_pieza
			WHERE mueble = n_mueble;
	ELSE SELECT "falta el mueble";
	END IF;
END;

DELIMITER //

CREATE PROCEDURE GET_PIEZAS ()
BEGIN
	SET @num = 0;
	SELECT @num := @num + 1 AS ' ', tipo, costo, cantidad FROM MATERIA_PRIMA ORDER BY costo;
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

