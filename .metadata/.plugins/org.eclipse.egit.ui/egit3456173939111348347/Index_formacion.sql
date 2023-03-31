rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/FORMACION.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-----------------------
-- VERSION FORMACION --
-----------------------

CREATE TABLE FOR_PRUEBAS (
	PRU_ID NUMBER(19,0) NOT NULL, 
	F_CREACION TIMESTAMP, 
	F_MODIFICACION TIMESTAMP, 
	N_VERSION NUMBER(19,0), 
	USU_CREACION VARCHAR2(25 CHAR), 
	USU_MODIFICACION VARCHAR2(25 CHAR), 
	L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, 
	C_CODIGO VARCHAR2(2 CHAR) NOT NULL, 
	D_DESCRIPCION VARCHAR2(255 CHAR) NOT NULL
) TABLESPACE GESTOR_DAT;

COMMENT ON TABLE FOR_PRUEBAS IS 'Tabla para ejercicio de pruebas';
COMMENT ON COLUMN FOR_PRUEBAS.PRU_ID IS 'PK';
COMMENT ON COLUMN FOR_PRUEBAS.F_CREACION IS 'Fecha de creación del registro';
COMMENT ON COLUMN FOR_PRUEBAS.L_ACTIVA IS 'Indica si el elemento está o no activo';
COMMENT ON COLUMN FOR_PRUEBAS.D_DESCRIPCION IS 'Descripción del registro';

CREATE TABLE FOR_PRUEBAS_H (
	PRU_ID NUMBER(19,0) NOT NULL, 
	REV NUMBER(19,0) NOT NULL, 
	REVTYPE NUMBER(3,0), 
	F_CREACION TIMESTAMP, 
	F_MODIFICACION TIMESTAMP, 
	N_VERSION NUMBER(19,0), 
	USU_CREACION VARCHAR2(25 CHAR), 
	USU_MODIFICACION VARCHAR2(25 CHAR), 
	L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, 
	C_CODIGO VARCHAR2(2 CHAR) NOT NULL, 
	D_DESCRIPCION VARCHAR2(255 CHAR) NOT NULL
) TABLESPACE GESTOR_DAT;

CREATE SEQUENCE S_FOR_PRUEBAS START WITH 1 INCREMENT BY  1;

ALTER TABLE FOR_PRUEBAS ADD CONSTRAINT FOR_PRUEBAS_PK PRIMARY KEY (PRU_ID) USING INDEX TABLESPACE GESTOR_IND;
ALTER TABLE FOR_PRUEBAS_H ADD CONSTRAINT FOR_PRUEBAS_H_PK PRIMARY KEY (PRU_ID, REV) USING INDEX TABLESPACE GESTOR_IND;

ALTER TABLE FOR_PRUEBAS_H ADD CONSTRAINT FOR_PRUEBAS_H_REVAUD_FK FOREIGN KEY (REV) REFERENCES GE_REVAUD;

INSERT INTO FOR_PRUEBAS (PRU_ID, F_CREACION, USU_CREACION, C_CODIGO, D_DESCRIPCION, N_VERSION) VALUES (S_FOR_PRUEBAS.NEXTVAL, SYSDATE, (SELECT USER FROM DUAL), 'P1', 'Prueba 1', 1);
INSERT INTO FOR_PRUEBAS (PRU_ID, F_CREACION, USU_CREACION, C_CODIGO, D_DESCRIPCION, N_VERSION) VALUES (S_FOR_PRUEBAS.NEXTVAL, SYSDATE, (SELECT USER FROM DUAL), 'P2', 'Prueba 2', 1);
INSERT INTO FOR_PRUEBAS (PRU_ID, F_CREACION, USU_CREACION, C_CODIGO, D_DESCRIPCION, N_VERSION) VALUES (S_FOR_PRUEBAS.NEXTVAL, SYSDATE, (SELECT USER FROM DUAL), 'P3', 'Prueba 3', 1);

GRANT SELECT, UPDATE, INSERT, DELETE ON FOR_PRUEBAS TO GE_ROL_GESTOR_DML;
GRANT SELECT, ALTER ON S_FOR_PRUEBAS TO GE_ROL_GESTOR_DML;
GRANT SELECT, UPDATE, INSERT, DELETE ON FOR_PRUEBAS_H TO GE_ROL_GESTOR_DML;

SPOOL OFF;