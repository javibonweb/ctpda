rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/FORMACION.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


-------------------
-- VERSION FORMACION --
-------------------

CREATE TABLE PRU_FORMACION (
PRU_ID NUMBER(19,0) NOT NULL,
F_CREACION TIMESTAMP,
F_MODIFICACION TIMESTAMP,
N_VERSION NUMBER(20,1),
USU_CREACION VARCHAR2(225 CHAR),
USU_MODIFICACION VARCHAR2(25 CHAR),
L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL,
C_CODIGO VARCHAR2(5 CHAR) NOT NULL,
D_DESCRIPCION VARCHAR2(255 CHAR) NOT NULL)
TABLESPACE GESTOR_DAT;

COMMENT ON TABLE PRU_FORMACION IS 'Tabla para ejercicio de pruebas';
COMMENT ON COLUMN PRU_FORMACION.PRU_ID IS 'PK';
COMMENT ON COLUMN PRU_FORMACION.F_CREACION IS 'Fecha de creación del registro';
COMMENT ON COLUMN PRU_FORMACION.L_ACTIVA IS 'Indica si el elemento está o no activo';
COMMENT ON COLUMN PRU_FORMACION.D_DESCRIPCION IS 'Descripción del registro';

CREATE TABLE PRU_FORMACION_H (
PRU_ID NUMBER(19,0) NOT NULL,
REV NUMBER(19,0) NOT NULL,
REVTYPE NUMBER(3,0),
F_CREACION TIMESTAMP, 
F_MODIFICACION TIMESTAMP, 
N_VERSION NUMBER(19,0), 
USU_CREACION VARCHAR2(25 CHAR), 
USU_MODIFICACION VARCHAR2(25 CHAR), 
L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, 
C_CODIGO VARCHAR2(5 CHAR) NOT NULL, 
D_DESCRIPCION VARCHAR2(255 CHAR) NOT NULL)
TABLESPACE GESTOR_DAT;

CREATE SEQUENCE S_PRU_FORMACION START WITH 1 INCREMENT BY 1;

ALTER TABLE PRU_FORMACION ADD CONSTRAINT PRU_FORMACION_PK PRIMARY KEY (PRU_ID) USING INDEX TABLESPACE GESTOR_IND;
ALTER TABLE PRU_FORMACION_H ADD CONSTRAINT PRU_FORMACION_H_PK PRIMARY KEY (PRU_ID, REV) USING INDEX TABLESPACE GESTOR_IND;

ALTER TABLE PRU_FORMACION_H ADD CONSTRAINT PRU_FORMACION_H_REVAUD_FK FOREIGN KEY (REV) REFERENCES GE_REVAUD;

GRANT SELECT, UPDATE, INSERT, DELETE ON PRU_FORMACION TO GE_ROL_GESTOR_DML;
GRANT SELECT, ALTER ON S_PRU_FORMACION TO GE_ROL_GESTOR_DML;
GRANT SELECT, UPDATE, INSERT, DELETE ON PRU_FORMACION_H TO GE_ROL_GESTOR_DML;