/*
 * Creación de las tablas GE_TIPO_TRAMITE_JMC y GE_TIPO_TRAMITE_JMC que se basan en las columnas y todos los datos de las tablas originales
 */
CREATE TABLE GE_TIPO_TRAMITE_JMC AS SELECT * FROM GE_TIPO_TRAMITE;
CREATE TABLE GE_TIPO_TRAMITE_H_JMC AS SELECT * FROM GE_TIPO_TRAMITE_H;
/*
 * Se añade la columna L_ESPERA_DOC como boolean y empieza en 0, será una columna obligatoria.
 */
ALTER TABLE GE_TIPO_TRAMITE_JMC ADD L_ESPERA_DOC NUMBER(1,0) DEFAULT 0 NOT NULL;
/*
 * Se actualiza todos los datos de L_ESPERA_DOC a 1 donde los códigos sean ciertas probabilidades.
 */
UPDATE GE_TIPO_TRAMITE_JMC SET L_ESPERA_DOC = 1 WHERE T_CODIGO IN ('TRDPD', 'SUB', 'REQINF', 'PETINFRTE', 'PETINFTER', 'REQCUMPL', 'RREQCUMPL', 'RREQINF', 'TRREC');