rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/FORMACION_AJLF.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-----------------------
-- VERSION FORMACION --
-----------------------

CREATE TABLE FOR_PRUEBAS_AJLF (
	PRU_ID NUMBER(19,0) NOT NULL, 
	F_CREACION TIMESTAMP, 
	F_MODIFICACION TIMESTAMP, 
	N_VERSION NUMBER(19,0), 
	USU_CREACION VARCHAR2(25 CHAR), 
	USU_MODIFICACION VARCHAR2(25 CHAR), 
	L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, 
	C_CODIGO VARCHAR2(50 CHAR) NOT NULL, 
	D_DESCRIPCION VARCHAR2(255 CHAR),
	FORPRU_AJLF_USU NUMBER(19,0)
) TABLESPACE GESTOR_DAT;

COMMENT ON TABLE FOR_PRUEBAS_AJLF IS 'Tabla para ejercicio de pruebas';
COMMENT ON COLUMN FOR_PRUEBAS_AJLF.PRU_ID IS 'PK';
COMMENT ON COLUMN FOR_PRUEBAS_AJLF.F_CREACION IS 'Fecha de creación del registro';
COMMENT ON COLUMN FOR_PRUEBAS_AJLF.F_MODIFICACION IS 'Fecha de modificación del registro';
COMMENT ON COLUMN FOR_PRUEBAS_AJLF.L_ACTIVA IS 'Indica si el elemento está o no activo';
COMMENT ON COLUMN FOR_PRUEBAS_AJLF.D_DESCRIPCION IS 'Descripción del registro';
COMMENT ON COLUMN FOR_PRUEBAS_AJLF.FORPRU_AJLF_USU IS 'FK con GE_USUARIOS';

CREATE TABLE FOR_PRUEBAS_AJLF_H (
	PRU_ID NUMBER(19,0) NOT NULL, 
	REV NUMBER(19,0) NOT NULL, 
	REVTYPE NUMBER(3,0), 
	N_VERSION NUMBER(19,0), 
	L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, 
	C_CODIGO VARCHAR2(50 CHAR) NOT NULL, 
	D_DESCRIPCION VARCHAR2(255 CHAR),
	FORPRU_AJLF_USU NUMBER(19,0)
) TABLESPACE GESTOR_DAT;

CREATE SEQUENCE S_FOR_PRUEBAS_AJLF START WITH 1 INCREMENT BY  1;

ALTER TABLE FOR_PRUEBAS_AJLF ADD CONSTRAINT FOR_PRUEBAS_AJLF_PK PRIMARY KEY (PRU_ID) USING INDEX TABLESPACE GESTOR_IND;
ALTER TABLE FOR_PRUEBAS_AJLF_H ADD CONSTRAINT FOR_PRUEBAS_AJLF_H_PK PRIMARY KEY (PRU_ID, REV) USING INDEX TABLESPACE GESTOR_IND;

ALTER TABLE FOR_PRUEBAS_AJLF ADD CONSTRAINT FORPRU_AJLF_USU_FK FOREIGN KEY (FORPRU_AJLF_USU) REFERENCES GE_USUARIOS;
ALTER TABLE FOR_PRUEBAS_AJLF_H ADD CONSTRAINT FOR_PRUEBAS_AJLF_H_REVAUD_FK FOREIGN KEY (REV) REFERENCES GE_REVAUD;

INSERT INTO FOR_PRUEBAS_AJLF (PRU_ID, F_CREACION, USU_CREACION, C_CODIGO, D_DESCRIPCION, N_VERSION) VALUES (S_FOR_PRUEBAS_AJLF.NEXTVAL, SYSDATE, (SELECT USER FROM DUAL), 'P1', 'Prueba 1', 1);
INSERT INTO FOR_PRUEBAS_AJLF (PRU_ID, F_CREACION, USU_CREACION, C_CODIGO, D_DESCRIPCION, N_VERSION) VALUES (S_FOR_PRUEBAS_AJLF.NEXTVAL, SYSDATE, (SELECT USER FROM DUAL), 'P2', 'Prueba 2', 1);
INSERT INTO FOR_PRUEBAS_AJLF (PRU_ID, F_CREACION, USU_CREACION, C_CODIGO, D_DESCRIPCION, N_VERSION) VALUES (S_FOR_PRUEBAS_AJLF.NEXTVAL, SYSDATE, (SELECT USER FROM DUAL), 'P3', 'Prueba 3', 1);
INSERT INTO FOR_PRUEBAS_AJLF (PRU_ID, F_CREACION, USU_CREACION, C_CODIGO, D_DESCRIPCION, N_VERSION, FORPRU_AJLF_USU) VALUES (S_FOR_PRUEBAS_AJLF.NEXTVAL, SYSDATE, (SELECT USER FROM DUAL), 'P3', 'Prueba 4-Usuario', 1, (SELECT USU_ID FROM GE_USUARIOS WHERE T_LOGIN = 'beatriz.lamorena'));

GRANT SELECT, UPDATE, INSERT, DELETE ON FOR_PRUEBAS_AJLF TO GE_ROL_GESTOR_DML;
GRANT SELECT, ALTER ON S_FOR_PRUEBAS_AJLF TO GE_ROL_GESTOR_DML;
GRANT SELECT, UPDATE, INSERT, DELETE ON FOR_PRUEBAS_AJLF_H TO GE_ROL_GESTOR_DML;

SPOOL OFF;